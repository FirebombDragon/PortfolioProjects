package edu.ncsu.csc.CoffeeMaker.services;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;

@RunWith ( SpringRunner.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class InventoryServiceTest {
    @Autowired
    private InventoryService inventoryService;

    @Before
    public void setup () {
        final Inventory ivt = inventoryService.getInventory();

        ivt.addIngredients( 500, 500, 500, 500 );

        inventoryService.save( ivt );
    }

    @Test
    @Transactional
    public void testGetInventory () {

        final Inventory i = inventoryService.getInventory();

        final Recipe recipe = new Recipe();
        recipe.setName( "Delicious Not-Coffee" );
        recipe.addIngredient( new Ingredient( "Coffee", 1 ) );
        recipe.addIngredient( new Ingredient( "Milk", 20 ) );
        recipe.addIngredient( new Ingredient( "Sugar", 5 ) );
        recipe.addIngredient( new Ingredient( "Chocolate", 10 ) );

        recipe.setPrice( 5 );

        i.useIngredients( recipe );

        assertEquals( 490, i.getAmountByType( "Chocolate" ) );
        assertEquals( 480, i.getAmountByType( "Milk" ) );
        assertEquals( 495, i.getAmountByType( "Sugar" ) );
        assertEquals( 499, i.getAmountByType( "Coffee" ) );
    }

    @Test
    @Transactional
    public void testGetInventoryWithNoInventory () {
        inventoryService.deleteAll();

        final Inventory i = inventoryService.getInventory();

        assertEquals( 0, i.getAmountByType( "Chocolate" ) );
        assertEquals( 0, i.getAmountByType( "Milk" ) );
        assertEquals( 0, i.getAmountByType( "Sugar" ) );
        assertEquals( 0, i.getAmountByType( "Coffee" ) );
    }
}

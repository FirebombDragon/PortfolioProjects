package edu.ncsu.csc.CoffeeMaker.unit;

import org.junit.Assert;
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
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;

@RunWith ( SpringRunner.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class InventoryTest {

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
    public void testConsumeInventory () {
        final Inventory i = inventoryService.getInventory();

        final Recipe recipe = new Recipe();
        recipe.setName( "Delicious Not-Coffee" );
        recipe.addIngredient( new Ingredient( "Coffee", 1 ) );
        recipe.addIngredient( new Ingredient( "Milk", 20 ) );
        recipe.addIngredient( new Ingredient( "Sugar", 5 ) );
        recipe.addIngredient( new Ingredient( "Chocolate", 10 ) );

        recipe.setPrice( 5 );

        i.useIngredients( recipe );

        /*
         * Make sure that all of the inventory fields are now properly updated
         */

        Assert.assertEquals( 490, i.getAmountByType( "Chocolate" ) );
        Assert.assertEquals( 480, i.getAmountByType( "Milk" ) );
        Assert.assertEquals( 495, i.getAmountByType( "Sugar" ) );
        Assert.assertEquals( 499, i.getAmountByType( "Coffee" ) );
    }

    @Test
    @Transactional

    public void testAddCustomIngredientToInventory () {
        final Inventory i = inventoryService.getInventory();

        Assert.assertEquals( 4, i.getIngredients().size() );

        final Ingredient ingre = new Ingredient( "Orange", 99 );

        i.addCustomIngredient( ingre );

        Assert.assertEquals( 5, i.getIngredients().size() );

        final Recipe recipe = new Recipe();
        recipe.setName( "Delicious Not-Coffee" );
        recipe.addIngredient( new Ingredient( "Coffee", 1 ) );
        recipe.addIngredient( new Ingredient( "Milk", 20 ) );
        recipe.addIngredient( new Ingredient( "Sugar", 5 ) );
        recipe.addIngredient( new Ingredient( "Chocolate", 10 ) );
        recipe.addIngredient( new Ingredient( "Orange", 10 ) );

        recipe.setPrice( 5 );

        i.useIngredients( recipe );

        /*
         * Make sure that all of the inventory fields are now properly updated
         */

        Assert.assertEquals( 490, i.getAmountByType( "Chocolate" ) );
        Assert.assertEquals( 480, i.getAmountByType( "Milk" ) );
        Assert.assertEquals( 495, i.getAmountByType( "Sugar" ) );
        Assert.assertEquals( 499, i.getAmountByType( "Coffee" ) );
        Assert.assertEquals( 89, i.getAmountByType( "Orange" ) );

        // try {
        // i.addCustomIngredient( ingre );
        // }
        // catch ( final IllegalArgumentException iae ) {
        //
        // }
    }

    @Test
    @Transactional
    public void testAddInventory1 () {
        Inventory ivt = inventoryService.getInventory();

        ivt.addIngredientAmount( 5, 3, 7, 2 );

        /* Save and retrieve again to update with DB */
        inventoryService.save( ivt );

        ivt = inventoryService.getInventory();

        Assert.assertEquals( "Adding to the inventory should result in correctly-updated values for coffee", 505,
                ivt.getAmountByType( "Coffee" ) );
        Assert.assertEquals( "Adding to the inventory should result in correctly-updated values for milk", 503,
                ivt.getAmountByType( "Milk" ) );
        Assert.assertEquals( "Adding to the inventory should result in correctly-updated values sugar", 507,
                ivt.getAmountByType( "Sugar" ) );
        Assert.assertEquals( "Adding to the inventory should result in correctly-updated values chocolate", 502,
                ivt.getAmountByType( "Chocolate" ) );

    }

    @Test
    @Transactional
    public void testAddInventory2 () {
        final Inventory ivt = inventoryService.getInventory();

        try {
            ivt.addIngredients( -5, 3, 7, 2 );
        }
        catch ( final IllegalArgumentException iae ) {
            Assert.assertEquals(
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- coffee",
                    500, ivt.getAmountByType( "Coffee" ) );
            Assert.assertEquals(
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- milk",
                    500, ivt.getAmountByType( "Milk" ) );
            Assert.assertEquals(
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- sugar",
                    500, ivt.getAmountByType( "Sugar" ) );
            Assert.assertEquals(
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- chocolate",
                    500, ivt.getAmountByType( "Chocolate" ) );
        }
    }

    @Test
    @Transactional
    public void testAddInventory3 () {
        final Inventory ivt = inventoryService.getInventory();

        try {
            ivt.addIngredients( 5, -3, 7, 2 );
        }
        catch ( final IllegalArgumentException iae ) {
            Assert.assertEquals(
                    "Trying to update the Inventory with an invalid value for milk should result in no changes -- coffee",
                    500, ivt.getAmountByType( "Coffee" ) );
            Assert.assertEquals(
                    "Trying to update the Inventory with an invalid value for milk should result in no changes -- milk",
                    500, ivt.getAmountByType( "Milk" ) );
            Assert.assertEquals(
                    "Trying to update the Inventory with an invalid value for milk should result in no changes -- sugar",
                    500, ivt.getAmountByType( "Sugar" ) );
            Assert.assertEquals(
                    "Trying to update the Inventory with an invalid value for milk should result in no changes -- chocolate",
                    500, ivt.getAmountByType( "Chocolate" ) );

        }

    }

    @Test
    @Transactional
    public void testAddInventory4 () {
        final Inventory ivt = inventoryService.getInventory();

        try {
            ivt.addIngredients( 5, 3, -7, 2 );
        }
        catch ( final IllegalArgumentException iae ) {
            Assert.assertEquals(
                    "Trying to update the Inventory with an invalid value for sugar should result in no changes -- coffee",
                    500, ivt.getAmountByType( "Coffee" ) );
            Assert.assertEquals(
                    "Trying to update the Inventory with an invalid value for sugar should result in no changes -- milk",
                    500, ivt.getAmountByType( "Milk" ) );
            Assert.assertEquals(
                    "Trying to update the Inventory with an invalid value for sugar should result in no changes -- sugar",
                    500, ivt.getAmountByType( "Sugar" ) );
            Assert.assertEquals(
                    "Trying to update the Inventory with an invalid value for sugar should result in no changes -- chocolate",
                    500, ivt.getAmountByType( "Chocolate" ) );

        }

    }

    @Test
    @Transactional
    public void testAddInventory5 () {
        final Inventory ivt = inventoryService.getInventory();

        try {
            ivt.addIngredients( 5, 3, 7, -2 );
        }
        catch ( final IllegalArgumentException iae ) {
            Assert.assertEquals(
                    "Trying to update the Inventory with an invalid value for chocolate should result in no changes -- coffee",
                    500, ivt.getAmountByType( "Coffee" ) );
            Assert.assertEquals(
                    "Trying to update the Inventory with an invalid value for chocolate should result in no changes -- milk",
                    500, ivt.getAmountByType( "Milk" ) );
            Assert.assertEquals(
                    "Trying to update the Inventory with an invalid value for chocolate should result in no changes -- sugar",
                    500, ivt.getAmountByType( "Sugar" ) );
            Assert.assertEquals(
                    "Trying to update the Inventory with an invalid value for chocolate should result in no changes -- chocolate",
                    500, ivt.getAmountByType( "Chocolate" ) );

        }

    }

    // // test added by njohnso5
    // @Test
    // @Transactional
    // public void testCheckInventory () {
    // final Inventory i = inventoryService.getInventory();
    // try {
    // Assert.assertEquals( "Checking a valid value for coffee", 100, (int)
    // i.checkCoffee( "100" ) );
    // Assert.assertEquals( "Checking a valid value for milk", 100, (int)
    // i.checkMilk( "100" ) );
    // Assert.assertEquals( "Checking a valid value for sugar", 100, (int)
    // i.checkSugar( "100" ) );
    // Assert.assertEquals( "Checking a valid value for chocolate", 100, (int)
    // i.checkChocolate( "100" ) );
    // }
    // catch ( final IllegalArgumentException e ) {
    // Assert.fail( e.getMessage() );
    // }
    //
    // try {
    // i.checkCoffee( "abc" );
    // Assert.fail( "A value was not supposed to be obtained from this
    // checkCoffee call" );
    // }
    // catch ( final IllegalArgumentException e ) {
    // Assert.assertEquals( "Checking that the correct error message was
    // thrown",
    // "Units of coffee must be a positive integer", e.getMessage() );
    // }
    //
    // try {
    // i.checkMilk( "abc" );
    // Assert.fail( "A value was not supposed to be obtained from this checkMilk
    // call" );
    // }
    // catch ( final IllegalArgumentException e ) {
    // Assert.assertEquals( "Checking that the correct error message was
    // thrown",
    // "Units of milk must be a positive integer", e.getMessage() );
    // }
    //
    // try {
    // i.checkSugar( "abc" );
    // Assert.fail( "A value was not supposed to be obtained from this
    // checkSugar call" );
    // }
    // catch ( final IllegalArgumentException e ) {
    // Assert.assertEquals( "Checking that the correct error message was
    // thrown",
    // "Units of sugar must be a positive integer", e.getMessage() );
    // }
    //
    // try {
    // i.checkChocolate( "abc" );
    // Assert.fail( "A value was not supposed to be obtained from this
    // checkChocolate call" );
    // }
    // catch ( final IllegalArgumentException e ) {
    // Assert.assertEquals( "Checking that the correct error message was
    // thrown",
    // "Units of chocolate must be a positive integer", e.getMessage() );
    // }
    // }

}

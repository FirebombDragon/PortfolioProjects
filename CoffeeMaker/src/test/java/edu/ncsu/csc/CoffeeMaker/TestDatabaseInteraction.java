package edu.ncsu.csc.CoffeeMaker;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@RunWith ( SpringRunner.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )

/**
 * Test Recipe,
 *
 */
public class TestDatabaseInteraction {
    @Autowired
    private RecipeService recipeService;

    /**
     * Test Add Recipe,Getting Recipe by Name
     *
     */
    @Test
    @Transactional
    public void testRecipes () {
        recipeService.deleteAll();

        final Recipe r = new Recipe();
        r.setPrice( 350 );
        r.addIngredient( new Ingredient( "Coffee", 2 ) );
        r.addIngredient( new Ingredient( "Sugar", 1 ) );
        r.addIngredient( new Ingredient( "Milk", 1 ) );
        r.addIngredient( new Ingredient( "Chocolate", 1 ) );
        r.setName( "Mocha" );

        recipeService.save( r );

        final List<Recipe> dbRecipes = recipeService.findAll();

        assertEquals( 1, dbRecipes.size() );

        final Recipe dbRecipe = dbRecipes.get( 0 );

        assertEquals( r.getName(), dbRecipe.getName() );
        assertEquals( r.getPrice(), dbRecipe.getPrice() );
        assertEquals( r.getAmountByType( "Coffee" ), dbRecipe.getAmountByType( "Coffee" ) );
        assertEquals( r.getAmountByType( "Sugar" ), dbRecipe.getAmountByType( "Sugar" ) );
        assertEquals( r.getAmountByType( "Milk" ), dbRecipe.getAmountByType( "Milk" ) );
        assertEquals( r.getAmountByType( "Chocolate" ), dbRecipe.getAmountByType( "Chocolate" ) );

    }

    // Team Task - Testing Getting Recipe by Name
    /**
     * Test EditRecipes
     *
     */
    @Test
    @Transactional
    public void testFindbyName () {
        recipeService.deleteAll();

        final Recipe r = new Recipe();
        r.setPrice( 350 );
        r.addIngredient( new Ingredient( "Coffee", 2 ) );
        r.addIngredient( new Ingredient( "Sugar", 1 ) );
        r.addIngredient( new Ingredient( "Milk", 1 ) );
        r.addIngredient( new Ingredient( "Chocolate", 1 ) );
        r.setName( "Mocha" );

        recipeService.save( r );

        final List<Recipe> dbRecipes = recipeService.findAll();

        assertEquals( 1, dbRecipes.size() );

        try {
            // find the recipe by name
            final Recipe find = recipeService.findByName( "Mocha" );

            // check all value to see we get back the one we expect.
            assertEquals( find.getName(), "Mocha" );
            assertEquals( 350, (int) find.getPrice() );
            assertEquals( 2, (int) find.getAmountByType( "Coffee" ) );
            assertEquals( 1, (int) find.getAmountByType( "Sugar" ) );
            assertEquals( 1, (int) find.getAmountByType( "Milk" ) );
            assertEquals( 1, (int) find.getAmountByType( "Chocolate" ) );

        }
        catch ( final Exception e ) {
            fail();
        }

    }

    // Team Task - Testing Edit Recipe
    /**
     * Test EditRecipes
     *
     */
    @Test
    @Transactional
    public void testEditRecipes () {
        recipeService.deleteAll();

        final Recipe r = new Recipe();
        r.setPrice( 5 );
        r.addIngredient( new Ingredient( "Coffee", 4 ) );
        r.addIngredient( new Ingredient( "Sugar", 3 ) );
        r.addIngredient( new Ingredient( "Milk", 2 ) );
        r.addIngredient( new Ingredient( "Chocolate", 1 ) );
        r.setName( "latte" );

        recipeService.save( r );

        final List<Recipe> dbRecipes = recipeService.findAll();

        assertEquals( 1, dbRecipes.size() );

        final Recipe dbRecipe = dbRecipes.get( 0 );

        // check all value to see we get back the one we expect.
        assertEquals( dbRecipe.getName(), "latte" );
        assertEquals( 5, (int) dbRecipe.getPrice() );
        assertEquals( 4, (int) dbRecipe.getAmountByType( "Coffee" ) );
        assertEquals( 3, (int) dbRecipe.getAmountByType( "Sugar" ) );
        assertEquals( 2, (int) dbRecipe.getAmountByType( "Milk" ) );
        assertEquals( 1, (int) dbRecipe.getAmountByType( "Chocolate" ) );

        // now change the fields
        dbRecipe.setPrice( 15 );
        r.setAmountByType( "Coffee", 14 );
        r.setAmountByType( "Milk", 13 );
        r.setAmountByType( "Sugar", 12 );
        r.setAmountByType( "Chocolate", 11 );
        // dbRecipe.setCoffee( 14 );
        // dbRecipe.setSugar( 13 );
        // dbRecipe.setMilk( 12 );
        // dbRecipe.setChocolate( 11 );

        // save it
        recipeService.save( dbRecipe );

        // Team Task - Verify Edited Recipe
        // check all value to see we get back the one we expect.
        assertEquals( dbRecipe.getName(), "latte" );
        assertEquals( 15, (int) dbRecipe.getPrice() );
        assertEquals( 14, (int) dbRecipe.getAmountByType( "Coffee" ) );
        assertEquals( 13, (int) dbRecipe.getAmountByType( "Milk" ) );
        assertEquals( 12, (int) dbRecipe.getAmountByType( "Sugar" ) );
        assertEquals( 11, (int) dbRecipe.getAmountByType( "Chocolate" ) );

    }

}

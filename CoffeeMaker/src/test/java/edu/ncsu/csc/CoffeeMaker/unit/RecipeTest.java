package edu.ncsu.csc.CoffeeMaker.unit;

import java.util.List;

import javax.validation.ConstraintViolationException;

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
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@RunWith ( SpringRunner.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class RecipeTest {

    @Autowired
    private RecipeService service;

    @Before
    public void setup () {
        service.deleteAll();
    }

    @Test
    @Transactional
    public void testAddRecipe () {

        final Recipe r1 = new Recipe();
        r1.setName( "Black Coffee" );
        r1.setPrice( 1 );
        r1.addIngredient( new Ingredient( "Coffee", 1 ) );
        r1.addIngredient( new Ingredient( "Milk", 0 ) );
        r1.addIngredient( new Ingredient( "Sugar", 0 ) );
        r1.addIngredient( new Ingredient( "Chocolate", 0 ) );
        // r1.setCoffee( 1 );
        // r1.setMilk( 0 );
        // r1.setSugar( 0 );
        // r1.setChocolate( 0 );
        service.save( r1 );

        final Recipe r2 = new Recipe();
        r2.setName( "Mocha" );
        r2.setPrice( 1 );
        r2.addIngredient( new Ingredient( "Coffee", 1 ) );
        r2.addIngredient( new Ingredient( "Milk", 1 ) );
        r2.addIngredient( new Ingredient( "Sugar", 1 ) );
        r2.addIngredient( new Ingredient( "Chocolate", 1 ) );
        // r2.setCoffee( 1 );
        // r2.setMilk( 1 );
        // r2.setSugar( 1 );
        // r2.setChocolate( 1 );
        service.save( r2 );

        final List<Recipe> recipes = service.findAll();
        Assert.assertEquals( "Creating two recipes should result in two recipes in the database", 2, recipes.size() );

        Assert.assertEquals( "The retrieved recipe should match the created one", r1, recipes.get( 0 ) );
    }

    @Test
    @Transactional
    public void testNoRecipes () {
        Assert.assertEquals( "There should be no Recipes in the CoffeeMaker", 0, service.findAll().size() );

        try {
            final Recipe r1 = new Recipe();
            r1.setName( "Tasty Drink" );
            r1.setPrice( 12 );
            r1.addIngredient( new Ingredient( "Coffee", -12 ) );
            r1.addIngredient( new Ingredient( "Milk", 0 ) );
            r1.addIngredient( new Ingredient( "Sugar", 0 ) );
            r1.addIngredient( new Ingredient( "Chocolate", 0 ) );
            // r1.setCoffee( -12 );
            // r1.setMilk( 0 );
            // r1.setSugar( 0 );
            // r1.setChocolate( 0 );

            final Recipe r2 = new Recipe();
            r2.setName( "Mocha" );
            r2.setPrice( 1 );
            r2.addIngredient( new Ingredient( "Coffee", 1 ) );
            r2.addIngredient( new Ingredient( "Milk", 1 ) );
            r2.addIngredient( new Ingredient( "Sugar", 1 ) );
            r2.addIngredient( new Ingredient( "Chocolate", 1 ) );
            // r2.setCoffee( 1 );
            // r2.setMilk( 1 );
            // r2.setSugar( 1 );
            // r2.setChocolate( 1 );

            final List<Recipe> recipes = List.of( r1, r2 );

            service.saveAll( recipes );
            Assert.assertEquals(
                    "Trying to save a collection of elements where one is invalid should result in neither getting saved",
                    0, service.count() );
        }
        catch ( final Exception e ) {
            // Assert.assertTrue( e instanceof ConstraintViolationException );
        }

    }

    @Test
    @Transactional
    public void testAddRecipe1 () {

        Assert.assertEquals( "There should be no Recipes in the CoffeeMaker", 0, service.findAll().size() );
        final String name = "Coffee";
        final Recipe r1 = createRecipe( name, 50, 3, 1, 1, 0 );

        service.save( r1 );

        Assert.assertEquals( "There should only one recipe in the CoffeeMaker", 1, service.findAll().size() );
        Assert.assertNotNull( service.findByName( name ) );

    }

    /* Test2 is done via the API for different validation */

    @Test
    @Transactional
    public void testAddRecipe3 () {
        Assert.assertEquals( "There should be no Recipes in the CoffeeMaker", 0, service.findAll().size() );
        final String name = "Coffee";
        final Recipe r1 = createRecipe( name, -50, 3, 1, 1, 0 );

        try {
            service.save( r1 );

            Assert.assertNull( "A recipe was able to be created with a negative price", service.findByName( name ) );
        }
        catch ( final ConstraintViolationException cvee ) {
            // expected
        }

    }

    @Test
    @Transactional
    public void testAddRecipe4 () {
        Assert.assertEquals( "There should be no Recipes in the CoffeeMaker", 0, service.findAll().size() );
        final String name = "Coffee";

        try {
            final Recipe r1 = createRecipe( name, 50, -3, 1, 1, 2 );
            service.save( r1 );

            Assert.assertNull( "A recipe was able to be created with a negative amount of coffee",
                    service.findByName( name ) );
        }
        catch ( final ConstraintViolationException cvee ) {
            // expected
        }

    }

    @Test
    @Transactional
    public void testAddRecipe5 () {
        Assert.assertEquals( "There should be no Recipes in the CoffeeMaker", 0, service.findAll().size() );
        final String name = "Coffee";

        try {
            final Recipe r1 = createRecipe( name, 50, 3, -1, 1, 2 );
            service.save( r1 );

            Assert.assertNull( "A recipe was able to be created with a negative amount of milk",
                    service.findByName( name ) );
        }
        catch ( final ConstraintViolationException cvee ) {
            // expected
        }

    }

    @Test
    @Transactional
    public void testAddRecipe6 () {
        Assert.assertEquals( "There should be no Recipes in the CoffeeMaker", 0, service.findAll().size() );
        final String name = "Coffee";

        try {
            final Recipe r1 = createRecipe( name, 50, 3, 1, -1, 2 );
            service.save( r1 );

            Assert.assertNull( "A recipe was able to be created with a negative amount of sugar",
                    service.findByName( name ) );
        }
        catch ( final ConstraintViolationException cvee ) {
            // expected
        }

    }

    @Test
    @Transactional
    public void testAddRecipe7 () {
        Assert.assertEquals( "There should be no Recipes in the CoffeeMaker", 0, service.findAll().size() );
        final String name = "Coffee";

        try {
            final Recipe r1 = createRecipe( name, 50, 3, 1, 1, -2 );
            service.save( r1 );

            Assert.assertNull( "A recipe was able to be created with a negative amount of chocolate",
                    service.findByName( name ) );
        }
        catch ( final ConstraintViolationException cvee ) {
            // expected
        }

    }

    @Test
    @Transactional
    public void testAddRecipe13 () {
        Assert.assertEquals( "There should be no Recipes in the CoffeeMaker", 0, service.findAll().size() );

        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        service.save( r1 );
        final Recipe r2 = createRecipe( "Mocha", 50, 3, 1, 1, 2 );
        service.save( r2 );

        Assert.assertEquals( "Creating two recipes should result in two recipes in the database", 2, service.count() );

    }

    @Test
    @Transactional
    public void testAddRecipe14 () {
        Assert.assertEquals( "There should be no Recipes in the CoffeeMaker", 0, service.findAll().size() );

        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        service.save( r1 );
        final Recipe r2 = createRecipe( "Mocha", 50, 3, 1, 1, 2 );
        service.save( r2 );
        final Recipe r3 = createRecipe( "Latte", 60, 3, 2, 2, 0 );
        service.save( r3 );

        Assert.assertEquals( "Creating three recipes should result in three recipes in the database", 3,
                service.count() );

    }

    @Test
    @Transactional
    public void testDeleteRecipe1 () {
        Assert.assertEquals( "There should be no Recipes in the CoffeeMaker", 0, service.findAll().size() );

        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        service.save( r1 );

        Assert.assertEquals( "There should be one recipe in the database", 1, service.count() );

        service.delete( r1 );
        Assert.assertEquals( "There should be no Recipes in the CoffeeMaker", 0, service.findAll().size() );
    }

    @Test
    @Transactional
    public void testDeleteRecipe2 () {
        Assert.assertEquals( "There should be no Recipes in the CoffeeMaker", 0, service.findAll().size() );

        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        service.save( r1 );
        final Recipe r2 = createRecipe( "Mocha", 50, 3, 1, 1, 2 );
        service.save( r2 );
        final Recipe r3 = createRecipe( "Latte", 60, 3, 2, 2, 0 );
        service.save( r3 );

        Assert.assertEquals( "There should be three recipes in the database", 3, service.count() );

        service.deleteAll();

        Assert.assertEquals( "`service.deleteAll()` should remove everything", 0, service.count() );

    }

    @Test
    @Transactional
    public void testEditRecipe1 () {
        Assert.assertEquals( "There should be no Recipes in the CoffeeMaker", 0, service.findAll().size() );

        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        service.save( r1 );

        r1.setPrice( 70 );

        service.save( r1 );

        final Recipe retrieved = service.findByName( "Coffee" );

        Assert.assertEquals( 70, (int) retrieved.getPrice() );
        Assert.assertEquals( 3, (int) retrieved.getAmountByType( "Coffee" ) );
        Assert.assertEquals( 1, (int) retrieved.getAmountByType( "Milk" ) );
        Assert.assertEquals( 1, (int) retrieved.getAmountByType( "Sugar" ) );
        Assert.assertEquals( 0, (int) retrieved.getAmountByType( "Chocolate" ) );

        Assert.assertEquals( "Editing a recipe shouldn't duplicate it", 1, service.count() );

    }

    // Test added by njohnso5
    @Test
    @Transactional
    public void testUpdateRecipe () {
        Assert.assertEquals( "There should be no recipes in the CoffeeMaker", 0, service.findAll().size() );
        // Initial recipe
        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        service.save( r1 );
        // New recipe
        final Recipe r2 = createRecipe( "Coffee", 50, 3, 1, 1, 1 );
        // Changes r1 to be the same as r2
        r1.updateRecipe( r2 );
        service.save( r1 );
        // Checks all fields
        final Recipe updated = service.findByName( "Coffee" );
        Assert.assertEquals( 50, (int) updated.getPrice() );
        Assert.assertEquals( 3, (int) updated.getAmountByType( "Coffee" ) );
        Assert.assertEquals( 1, (int) updated.getAmountByType( "Milk" ) );
        Assert.assertEquals( 1, (int) updated.getAmountByType( "Sugar" ) );
        Assert.assertEquals( 1, (int) updated.getAmountByType( "Chocolate" ) );
        Assert.assertEquals( 1, service.count() );
    }

    // Test added by zpan4
    @Test
    @Transactional
    public void testCheckRecipe () {
        final Recipe r0 = createRecipe( "r0", 0, 0, 0, 0, 0 );
        final Recipe r0b = createRecipe( "r0", 0, 0, 0, 0, 0 );
        final Recipe r1 = createRecipe( "r1", 1, 1, 1, 1, 1 );
        final Recipe r2 = createRecipe( "r2", 2, 2, 2, 2, 2 );

        Assert.assertTrue( r0.checkRecipe() );
        Assert.assertTrue( r0.checkRecipe() );

        r0.updateRecipe( r2 );
        Assert.assertFalse( r0.checkRecipe() );
        r0.updateRecipe( r0b );
        Assert.assertFalse( r1.checkRecipe() );
    }

    private Recipe createRecipe ( final String name, final Integer price, final Integer coffee, final Integer milk,
            final Integer sugar, final Integer chocolate ) {
        final Recipe recipe = new Recipe();
        recipe.setName( name );
        recipe.setPrice( price );

        recipe.addIngredient( new Ingredient( "Coffee", coffee ) );
        recipe.addIngredient( new Ingredient( "Milk", milk ) );
        recipe.addIngredient( new Ingredient( "Sugar", sugar ) );
        recipe.addIngredient( new Ingredient( "Chocolate", chocolate ) );
        // recipe.setMilk( milk );
        // recipe.setSugar( sugar );
        // recipe.setChocolate( chocolate );

        return recipe;
    }

}

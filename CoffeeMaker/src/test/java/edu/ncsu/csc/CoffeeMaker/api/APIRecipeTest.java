package edu.ncsu.csc.CoffeeMaker.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.CoffeeMaker.common.TestUtils;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@RunWith ( SpringRunner.class )
@SpringBootTest
@AutoConfigureMockMvc
public class APIRecipeTest {

    /**
     * MockMvc uses Spring's testing framework to handle requests to the REST
     * API
     */
    private MockMvc               mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private RecipeService         service;

    /**
     * Sets up the tests.
     */
    @Before
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();
        service.deleteAll();
    }

    @Test
    @Transactional
    public void ensureRecipe () throws Exception {
        service.deleteAll();

        final Recipe r = new Recipe();
        r.addIngredient( new Ingredient( "Coffee", 3 ) );
        r.addIngredient( new Ingredient( "Milk", 4 ) );
        r.addIngredient( new Ingredient( "Sugar", 8 ) );
        r.addIngredient( new Ingredient( "Chocolate", 5 ) );
        r.setPrice( 10 );
        r.setName( "Mocha" );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r ) ) ).andExpect( status().isOk() );
    }

    @Test
    @Transactional
    public void testRecipeAPI () throws Exception {

        service.deleteAll();

        final Recipe recipe = new Recipe();
        recipe.setName( "Delicious Not-Coffee" );
        recipe.addIngredient( new Ingredient( "Coffee", 1 ) );
        recipe.addIngredient( new Ingredient( "Milk", 20 ) );
        recipe.addIngredient( new Ingredient( "Sugar", 5 ) );
        recipe.addIngredient( new Ingredient( "Chocolate", 10 ) );

        recipe.setPrice( 5 );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( recipe ) ) );

        Assert.assertEquals( 1, (int) service.count() );

    }

    @Test
    @Transactional
    public void testAddRecipe2 () throws Exception {

        /* Tests a recipe with a duplicate name to make sure it's rejected */

        Assert.assertEquals( "There should be no Recipes in the CoffeeMaker", 0, service.findAll().size() );
        final String name = "Coffee";
        final Recipe r1 = createRecipe( name, 50, 3, 1, 1, 0 );

        service.save( r1 );

        final Recipe r2 = createRecipe( name, 50, 3, 1, 1, 0 );
        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r2 ) ) ).andExpect( status().is4xxClientError() );

        Assert.assertEquals( "There should only one recipe in the CoffeeMaker", 1, service.findAll().size() );
    }

    @Test
    @Transactional
    public void testAddRecipe15 () throws Exception {

        /* Tests to make sure that our cap of 3 recipes is enforced */

        Assert.assertEquals( "There should be no Recipes in the CoffeeMaker", 0, service.findAll().size() );

        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        service.save( r1 );
        final Recipe r2 = createRecipe( "Mocha", 50, 3, 1, 1, 2 );
        service.save( r2 );
        final Recipe r3 = createRecipe( "Latte", 60, 3, 2, 2, 0 );
        service.save( r3 );

        Assert.assertEquals( "Creating three recipes should result in three recipes in the database", 3,
                service.count() );

        final Recipe r4 = createRecipe( "Hot Chocolate", 75, 0, 2, 1, 2 );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r4 ) ) ).andExpect( status().isInsufficientStorage() );

        Assert.assertEquals( "Creating a fourth recipe should not get saved", 3, service.count() );
    }

    // test added by njohnso5
    @Test
    @Transactional
    public void testGetRecipe () throws Exception {
        Assert.assertEquals( 0, service.findAll().size() );
        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        service.save( r1 );
        final String notARecipe = mvc.perform( get( "/api/v1/recipes/Mocha" ) ).andDo( print() ).andReturn()
                .getResponse().getContentAsString();
        Assert.assertTrue( notARecipe.contains( "No recipe found with name Mocha" ) );
        final String recipe = mvc.perform( get( "/api/v1/recipes/Coffee" ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        Assert.assertTrue( recipe.contains( "Coffee" ) );

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

        return recipe;
    }

    @Test
    @Transactional
    public void testeditRecipe () throws Exception {
        /* Tests a recipe with a duplicate name to make sure it's rejected */
        // ? not coverage
        Assert.assertEquals( "There should be no Recipes in the CoffeeMaker", 0, service.findAll().size() );

        final String name = "Coffee";
        final Recipe r1 = createRecipe( name, 50, 3, 1, 1, 0 );

        // test a recipe is not exist
        mvc.perform( put( "/api/v1/recipes/Coffee" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r1 ) ) ).andExpect( status().is4xxClientError() );

        service.save( r1 );

        // Tests successfully editing a recipe
        final Recipe r2 = createRecipe( name, 50, 2, 2, 2, 2 );
        mvc.perform( put( "/api/v1/recipes/Coffee" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r2 ) ) ).andExpect( status().isOk() );
        Assert.assertEquals( "There should only one recipe in the CoffeeMaker", 1, service.findAll().size() );
        Assert.assertEquals( 50, (int) service.findByName( "Coffee" ).getPrice() );
        Assert.assertEquals( 2, (int) service.findByName( "Coffee" ).getAmountByType( "Coffee" ) );
        Assert.assertEquals( 2, (int) service.findByName( "Coffee" ).getAmountByType( "Milk" ) );
        Assert.assertEquals( 2, (int) service.findByName( "Coffee" ).getAmountByType( "Sugar" ) );
        Assert.assertEquals( 2, (int) service.findByName( "Coffee" ).getAmountByType( "Chocolate" ) );
    }
}

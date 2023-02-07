package edu.ncsu.csc.CoffeeMaker.api;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;

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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.CoffeeMaker.common.TestUtils;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;

@RunWith ( SpringRunner.class )
@SpringBootTest
@AutoConfigureMockMvc
public class APITest {

    /**
     * MockMvc uses Spring's testing framework to handle requests to the REST
     * API
     */
    private MockMvc               mvc;

    @Autowired
    private WebApplicationContext context;

    /**
     * Sets up the tests.
     */
    @Before
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();
    }

    @Test
    @Transactional
    public void sometest () throws UnsupportedEncodingException, Exception {
        String recipe = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();

        /* Figure out if the recipe we want is present */
        if ( !recipe.contains( "Mocha" ) ) {
            final Recipe r = new Recipe();
            r.setAmountByType( "Coffee", 3 );
            r.setAmountByType( "Milk", 4 );
            r.setAmountByType( "Sugar", 8 );
            r.setAmountByType( "Chocolate", 5 );
            r.setPrice( 10 );
            r.setName( "Mocha" );

            mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                    .content( TestUtils.asJsonString( r ) ) ).andExpect( status().isOk() );
        }

        recipe = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();
        // Team Task - Getting Recipes
        assertTrue( recipe.contains( "Mocha" ) );

        final Inventory i = new Inventory( 50, 50, 50, 50 );

        mvc.perform( put( "/api/v1/inventory" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( i ) ) ).andExpect( status().isOk() );

        mvc.perform( post( String.format( "/api/v1/makecoffee/%s", "Mocha" ) ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( 100 ) ) ).andExpect( status().isOk() ).andDo( print() );
    }

    @Test
    @Transactional
    // for getInventory in APIInventoryControllertest
    public void APIInventoryControllertest () throws UnsupportedEncodingException, Exception {

        String recipe = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();

        /* Figure out if the recipe we want is present */
        if ( !recipe.contains( "Latte" ) ) {
            final Recipe r = new Recipe();
            r.setAmountByType( "Coffee", 4 );
            r.setAmountByType( "Milk", 2 );
            r.setAmountByType( "Sugar", 1 );
            r.setAmountByType( "Chocolate", 0 );
            // r.setChocolate( 0 );
            // r.setCoffee( 4 );
            // r.setMilk( 2 );
            // r.setSugar( 1 );
            r.setPrice( 8 );
            r.setName( "Latte" );

            mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                    .content( TestUtils.asJsonString( r ) ) ).andExpect( status().isOk() );
        }

        recipe = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();
        // Team Task - Getting Recipes
        assertTrue( recipe.contains( "Latte" ) );

        mvc.perform( get( "/api/v1/inventory" ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() );

    }

    @Test
    @Transactional
    public void testDeleteAPI () throws UnsupportedEncodingException, Exception {
        final String recipe = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();

        if ( !recipe.contains( "Mocha" ) ) {
            // create a new Mocha recipe
            final Recipe r = new Recipe();
            r.setAmountByType( "Coffee", 3 );
            r.setAmountByType( "Milk", 4 );
            r.setAmountByType( "Sugar", 8 );
            r.setAmountByType( "Chocolate", 5 );
            // r.setChocolate( 5 );
            // r.setCoffee( 3 );
            // r.setMilk( 4 );
            // r.setSugar( 8 );
            r.setPrice( 10 );
            r.setName( "Mocha" );

            mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                    .content( TestUtils.asJsonString( r ) ) ).andExpect( status().isOk() );

        }

        final String recipe2 = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();

        // Team Task - Getting Recipes
        assertTrue( recipe2.contains( "Mocha" ) );
        mvc.perform( delete( "/api/v1/recipes/Mocha" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();
        final String recipeDel2 = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        assertFalse( recipeDel2.contains( "Mocha" ) );
    }
}

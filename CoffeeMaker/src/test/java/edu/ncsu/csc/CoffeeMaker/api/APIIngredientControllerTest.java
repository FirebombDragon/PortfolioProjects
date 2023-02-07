package edu.ncsu.csc.CoffeeMaker.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.CoffeeMaker.common.TestUtils;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.services.IngredientService;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;

@RunWith ( SpringRunner.class )
@SpringBootTest
@AutoConfigureMockMvc
public class APIIngredientControllerTest {
    private MockMvc               mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private IngredientService     ingService;

    @Autowired
    private InventoryService      invService;

    @Before
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();
        ingService.deleteAll();
    }

    @Test
    @Transactional
    public void testAddIngredient () throws Exception {

        // there shouldn't be anything in it
        assertEquals( 0, ingService.findAll().size() );

        // save the first ingredient
        final Ingredient ing1 = new Ingredient( "Sugar", 10 );
        ingService.save( ing1 );
        assertEquals( 1, ingService.findAll().size() );

        // save the second ingredient
        final Ingredient ing2 = new Ingredient( "Chocolate", 10 );
        ingService.save( ing2 );
        assertEquals( 2, ingService.findAll().size() );

        // make sure they're stored correctly
        assertEquals( ing1, ingService.findByIngredient( "Sugar" ) );
        assertEquals( ing2, ingService.findByIngredient( "Chocolate" ) );

        // adding one to inventory shouldn't affect its original status
        invService.getInventory().addCustomIngredient( ing2 );
        invService.save( invService.getInventory() );
        assertEquals( 2, ingService.findAll().size() );

        final Ingredient ing3 = new Ingredient( "Chocolate", 10 );

        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ing3 ) ) );
        assertEquals( ing1, ingService.findByIngredient( "Sugar" ) );
        assertEquals( 2, ingService.findAll().size() );
        assertEquals( 1, invService.findAll().size() );

        // Test for delete out put
        // @ author xliang8
        // delete exist(successfully)

        String deleted = mvc.perform( delete( "/api/v1/ingredients/Chocolate" ) ).andDo( print() ).andReturn()
                .getResponse().getContentAsString();
        Assert.assertFalse( deleted.contains( "No ingredient found for Chocolate" ) );
        Assert.assertTrue( deleted.contains( "Chocolate was deleted successfully" ) );

        // delete no exist(failing)
        deleted = mvc.perform( delete( "/api/v1/ingredients/asd" ) ).andDo( print() ).andReturn().getResponse()
                .getContentAsString();

        Assert.assertTrue( deleted.contains( "No ingredient found for " ) );
        Assert.assertFalse( deleted.contains( "asd was deleted successfully" ) );

        // assertEquals( 1, ingService.findAll().size() );
        // ?number is not update

        // get no exist

        String intGetName = mvc.perform( get( "/api/v1/ingredients/abc" ) ).andDo( print() ).andReturn().getResponse()
                .getContentAsString();
        Assert.assertTrue( intGetName.contains( "No ingredient found with" ) );

        // get exist
        intGetName = mvc.perform( get( "/api/v1/ingredients/Sugar" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        Assert.assertTrue( intGetName.contains( "Sugar" ) );

        // get list
        // final List<Ingredient> listIngre = ingService.findAll();
        // final List<Ingredient> listIngre2 = (List<Ingredient>) mvc.perform(
        // get( "/api/v1/ingredients" ) );
        // Assert.assertTrue( listIngre.get( 0 ).equals( listIngre2.get( 0 ) )
        // );
        // mvc.perform( get( "/api/v1/ingredients" ) ).andReturn().;

        final Ingredient newingre = new Ingredient( "Coco", 10 );

        final String newIngreReport = mvc
                .perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( newingre ) ) )
                .andDo( print() ).andReturn().getResponse().getContentAsString();

        Assert.assertTrue( newIngreReport.contains( "successfully created" ) );
    }

    @Test
    @Transactional
    public void testAddIngredient2 () throws Exception {

        // there shouldn't be anything in it
        assertEquals( 0, ingService.findAll().size() );

        final Inventory ivt = invService.getInventory();

        // add the first 4
        ivt.addIngredients( 500, 500, 500, 500 );

        // we have 4 ingredients
        assertEquals( 4, ingService.findAll().size() );

        final Ingredient ing5 = new Ingredient( "Orange", 10 );

        // add ingredient by api, store message to add,successfully
        String add = mvc
                .perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( ing5 ) ) )
                .andDo( print() ).andReturn().getResponse().getContentAsString();

        Assert.assertTrue( add.contains( "successfully created" ) );

        assertEquals( 5, ingService.findAll().size() );

        // assertEquals( ing5 , ingService.findByIngredient( "Orange"
        // ).getIngredient() );
        Assert.assertTrue( ingService.findByIngredient( "Orange" ).getIngredient().equals( ing5.getIngredient() ) );
        // Ingredient [ingredient=Orange, amount=10]
        // Ingredient [ingredient=Orange, amount=10]

        // add ingredient by api, store message to add,duplicate
        add = mvc
                .perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( ing5 ) ) )
                .andDo( print() ).andReturn().getResponse().getContentAsString();

        Assert.assertTrue( add.contains( "Ingredient with the name Orange already exists" ) );

        // check return error, conflict
        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( ing5 ) ) ).andExpect( status().isConflict() );

        assertEquals( 5, ingService.findAll().size() );

    }
}

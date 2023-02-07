package edu.ncsu.csc.CoffeeMaker.api;

import java.io.UnsupportedEncodingException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@RunWith ( SpringRunner.class )
@SpringBootTest
@AutoConfigureMockMvc
public class MappingTest {

    /**
     * MockMvc uses Spring's testing framework to handle requests to the REST
     * API
     */
    private MockMvc               mvc;

    @Autowired
    private WebApplicationContext context;
    // private RecipeService service;
    // private InventoryService inventoryService;

    /**
     * Sets up the tests.
     */
    @Before
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();
    }

    // By Xiaochun Liang
    @Test
    @Transactional
    public void testAPIInventoryController () throws UnsupportedEncodingException, Exception {

        // final String index = mvc.perform( get( "/api/v1/index" ) ).andExpect(
        // status().isOk() ).andReturn()
        // .getResponse().getContentAsString();
        //
        // assertTrue( "index".equals( index ) );

        // final String recipe = mvc.perform( get( "/api/v1/recipe" ) ).andDo(
        // print() ).andExpect( status().isOk() )
        // .andReturn().getResponse().getContentAsString();
        //
        // assertTrue( "recipe".equals( recipe ) );
        //
        // final String deleterecipe = mvc.perform( get( "/api/v1/deleterecipe"
        // ) ).andDo( print() )
        // .andExpect( status().isOk()
        // ).andReturn().getResponse().getContentAsString();
        //
        // assertTrue( "deleterecipe".equals( deleterecipe ) );
        //
        // final String inventory = mvc.perform( get( "/api/v1/inventory" )
        // ).andDo( print() ).andExpect( status().isOk() )
        // .andReturn().getResponse().getContentAsString();
        //
        // assertTrue( "inventory".equals( inventory ) );
        //
        // final String makecoffee = mvc.perform( get( "/api/v1/makecoffee" )
        // ).andDo( print() )
        // .andExpect( status().isOk()
        // ).andReturn().getResponse().getContentAsString();
        //
        // assertTrue( "makecoffee".equals( makecoffee ) );

    }
}

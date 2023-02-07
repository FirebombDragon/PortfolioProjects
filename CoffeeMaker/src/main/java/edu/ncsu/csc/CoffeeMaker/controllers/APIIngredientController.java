package edu.ncsu.csc.CoffeeMaker.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.services.IngredientService;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;

/**
 * This is the controller that holds the REST endpoints that handle CRUD
 * operations for Ingredients.
 *
 * Spring will automatically convert all of the ResponseEntity and List results
 * to JSON
 *
 * @author Zhicheng Pan
 *
 */
@SuppressWarnings ( { "unchecked", "rawtypes" } )
@RestController
public class APIIngredientController extends APIController {

    /**
     * IngredientService object, to be autowired in by Spring to allow for
     * manipulating the ingredient model
     */
    @Autowired
    private IngredientService svc;

    @Autowired
    private InventoryService  isvc;

    /**
     * REST API method to provide GET access to all ingredients in the system
     *
     * @return JSON representation of all ingredients
     */
    @GetMapping ( BASE_PATH + "/ingredients" )
    public List<Ingredient> getIngredients () {
        return svc.findAll();
    }

    /**
     * REST API method to provide GET access to a specific ingredient, as
     * indicated by the path variable provided (the name of the ingredient
     * desired)
     *
     * @param name
     *            ingredient name
     * @return response to the request
     */
    @GetMapping ( BASE_PATH + "/ingredients/{name}" )
    public ResponseEntity getIngredient ( @PathVariable ( "name" ) final String name ) {
        final Ingredient ingredient = svc.findByIngredient( name );
        return null == ingredient
                ? new ResponseEntity( errorResponse( "No ingredient found with " + name ), HttpStatus.NOT_FOUND )
                : new ResponseEntity( ingredient, HttpStatus.OK );
    }

    /**
     * REST API method to provide POST access to the Ingredient model. This is
     * used to create a new Ingredient by automatically converting the JSON
     * RequestBody provided to a Ingredient object. Invalid JSON will fail.
     *
     * @param ingredient
     *            The valid Ingredient to be saved.
     * @return ResponseEntity indicating success if the Ingredient could be
     *         saved to the inventory, or an error if it could not be
     */

    @PostMapping ( BASE_PATH + "/ingredients" )
    public ResponseEntity createIngredient ( @RequestBody final Ingredient ingredient ) {

        if ( null != svc.findByIngredient( ingredient.getIngredient() ) ) {
            return new ResponseEntity(
                    successResponse(
                            "Ingredient with the name " + ingredient.getIngredient().toString() + " already exists" ),
                    HttpStatus.CONFLICT );
        }
        // final Inventory inventoryCurrent = isvc.getInventory();
        // inventoryCurrent.addCustomIngredient( ingredient );

        // isvc.save( inventoryCurrent );
        svc.save( ingredient );
        return new ResponseEntity( successResponse( ingredient.getIngredient().toString() + " successfully created" ),
                HttpStatus.OK );

    }

    /**
     * REST API method to allow deleting a Ingredient from the CoffeeMaker's
     * Inventory, by making a DELETE request to the API endpoint and indicating
     * the ingredient to delete (as a path variable)
     *
     * @param name
     *            The name of the Ingredient to delete
     * @return Success if the ingredient could be deleted; an error if the
     *         ingredient does not exist
     */
    @DeleteMapping ( BASE_PATH + "/ingredients/{name}" )
    public ResponseEntity deleteIngredient ( @PathVariable final String name ) {
        final Ingredient ingredient = svc.findByIngredient( name );
        if ( null == ingredient ) {
            return new ResponseEntity( errorResponse( "No ingredient found for " + name ), HttpStatus.NOT_FOUND );
        }
        svc.delete( ingredient );

        return new ResponseEntity( successResponse( name + " was deleted successfully" ), HttpStatus.OK );
    }
}

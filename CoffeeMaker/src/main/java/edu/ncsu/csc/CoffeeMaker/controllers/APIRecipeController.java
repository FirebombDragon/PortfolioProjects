package edu.ncsu.csc.CoffeeMaker.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

/**
 * This is the controller that holds the REST endpoints that handle CRUD
 * operations for Recipes.
 *
 * Spring will automatically convert all of the ResponseEntity and List results
 * to JSON
 *
 * @author Kai Presler-Marshall
 * @author Michelle Lemons
 *
 */
@SuppressWarnings ( { "unchecked", "rawtypes" } )
@RestController
public class APIRecipeController extends APIController {

    /**
     * RecipeService object, to be autowired in by Spring to allow for
     * manipulating the Recipe model
     */
    @Autowired
    private RecipeService service;

    /**
     * REST API method to provide GET access to all recipes in the system
     *
     * @return JSON representation of all recipies
     */
    @GetMapping ( BASE_PATH + "/recipes" )
    public List<Recipe> getRecipes () {
        return service.findAll();
    }

    /**
     * REST API method to provide GET access to a specific recipe, as indicated
     * by the path variable provided (the name of the recipe desired)
     *
     * @param name
     *            recipe name
     * @return response to the request
     */
    @GetMapping ( BASE_PATH + "/recipes/{name}" )
    public ResponseEntity getRecipe ( @PathVariable ( "name" ) final String name ) {
        final Recipe recipe = service.findByName( name );
        return null == recipe
                ? new ResponseEntity( errorResponse( "No recipe found with name " + name ), HttpStatus.NOT_FOUND )
                : new ResponseEntity( recipe, HttpStatus.OK );
    }

    /**
     * REST API method to provide POST access to the Recipe model. This is used
     * to create a new Recipe by automatically converting the JSON RequestBody
     * provided to a Recipe object. Invalid JSON will fail.
     *
     * @param recipe
     *            The valid Recipe to be saved.
     * @return ResponseEntity indicating success if the Recipe could be saved to
     *         the inventory, or an error if it could not be
     */

    @PostMapping ( BASE_PATH + "/recipes" )
    public ResponseEntity createRecipe ( @RequestBody final Recipe recipe ) {

        if ( null != service.findByName( recipe.getName() ) ) {
            return new ResponseEntity(
                    successResponse( "Recipe with the name " + recipe.getName() + " already exists" ),
                    HttpStatus.CONFLICT );
        }

        if ( service.findAll().size() < 3 ) {
            service.save( recipe );
            return new ResponseEntity( successResponse( recipe.getName() + " successfully created" ), HttpStatus.OK );
        }
        else {
            return new ResponseEntity(
                    errorResponse( "Insufficient space in recipe book for recipe " + recipe.getName() ),
                    HttpStatus.INSUFFICIENT_STORAGE );
        }

    }

    /**
     * REST API method to allow deleting a Recipe from the CoffeeMaker's
     * Inventory, by making a DELETE request to the API endpoint and indicating
     * the recipe to delete (as a path variable)
     *
     * @param name
     *            The name of the Recipe to delete
     * @return Success if the recipe could be deleted; an error if the recipe
     *         does not exist
     */
    @DeleteMapping ( BASE_PATH + "/recipes/{name}" )
    public ResponseEntity deleteRecipe ( @PathVariable final String name ) {
        final Recipe recipe = service.findByName( name );
        if ( null == recipe ) {
            return new ResponseEntity( errorResponse( "No recipe found for name " + name ), HttpStatus.NOT_FOUND );
        }
        service.delete( recipe );

        return new ResponseEntity( successResponse( name + " was deleted successfully" ), HttpStatus.OK );
    }

    /**
     * REST API endpoint to provide update access to CoffeeMaker's singleton
     * Inventory. This will update the recipes of the CoffeeMaker by changing
     * amounts from the Inventory provided to the CoffeeMaker's stored inventory
     *
     * @author Xiaochun Liang
     * @param inventory
     *            amounts to add to inventory
     * @return response to the request
     */
    @PutMapping ( BASE_PATH + "/recipes/{name}" )
    public ResponseEntity editRecipes ( @PathVariable final String name, @RequestBody final Recipe recipe ) {
        // check if we don't have the recipe
        final Recipe rFind = service.findByName( name );
        // Alternative Flows: Cannot Edit
        if ( null == rFind ) {
            return new ResponseEntity(
                    successResponse( "Recipe with the name " + recipe.getName() + " does not exist" ),
                    HttpStatus.NOT_FOUND );
        }

        // if we have it
        try {
            // Alternative Flows: Invalid Price
            if ( recipe.getPrice() <= 0 ) {
                return new ResponseEntity( errorResponse( "Invalid Price while editing the recipe" ),
                        HttpStatus.BAD_REQUEST );
            }
            rFind.setPrice( recipe.getPrice() );
            // New recipe
            final List<Ingredient> ingredientsAdd = recipe.getIngredients();
            // Old recipe
            final List<Ingredient> ingredientsFind = rFind.getIngredients();

            // clean all
            ingredientsFind.clear();
            ingredientsFind.addAll( ingredientsAdd );

            // replace recipe
            rFind.updateRecipe( recipe );
            service.save( rFind );
            return new ResponseEntity( successResponse( recipe.getName() + " was edited successfully" ),
                    HttpStatus.OK );

        }
        catch ( final Exception e ) {
            return new ResponseEntity( "Something went wrong while editing the recipe :: " + e.getCause(),
                    HttpStatus.BAD_REQUEST );
        }

    }
}

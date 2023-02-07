package edu.ncsu.csc.CoffeeMaker.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.repositories.IngredientRepository;

@Component
@Transactional
public class IngredientService extends Service<Ingredient, Long> {
    @Autowired
    private IngredientRepository ingredientRepository;

    @Override
    protected JpaRepository<Ingredient, Long> getRepository () {
        return ingredientRepository;
    }

    /**
     * Find an ingredient with the provided name
     *
     * @param name
     *            Name of the ingredient to find
     * @return found ingredient, null if none
     */
    public Ingredient findByIngredient ( final String name ) {
        return ingredientRepository.findByIngredient( name );
    }
}

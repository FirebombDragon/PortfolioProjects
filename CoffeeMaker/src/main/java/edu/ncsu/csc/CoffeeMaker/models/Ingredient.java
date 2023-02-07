package edu.ncsu.csc.CoffeeMaker.models;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Min;

@Entity
public class Ingredient extends DomainObject {
    @Id
    @GeneratedValue
    long    id;
    // @Enumerated ( EnumType.STRING )
    String  ingredient;
    @Min ( 0 )
    Integer amount;

    public Ingredient () {
        super();
    }

    public Ingredient ( final String name, final Integer amount ) {
        super();
        setIngredient( name );
        setAmount( amount );
    }

    public String getIngredient () {
        return ingredient;
    }

    public void setIngredient ( final String name ) {
        this.ingredient = name;
    }

    public Integer getAmount () {
        return amount;
    }

    public void setAmount ( final Integer amount ) {
        this.amount = amount;
    }

    @Override
    public Serializable getId () {
        return id;
    }

    @Override
    public String toString () {
        return "Ingredient [ingredient=" + ingredient + ", amount=" + amount + "]";
    }

    // @Override
    // public boolean equals ( final Ingredient obj ) {
    // if ( this.ingredient.equals( obj.ingredient ) ) {
    // return true;
    // }
    // return false;
    // }

}

package edu.ncsu.csc.CoffeeMaker.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * Inventory for the coffee maker. Inventory is tied to the database using
 * Hibernate libraries. See InventoryRepository and InventoryService for the
 * other two pieces used for database support.
 *
 * @author Kai Presler-Marshall
 */
@Entity
public class Inventory extends DomainObject {

    /** id for inventory entry */
    @Id
    @GeneratedValue
    private Long                   id;
    /**
     * /** amount of coffee *
     *
     * @Min ( 0 ) private Integer coffee; /** amount of milk *
     * @Min ( 0 ) private Integer milk; /** amount of sugar *
     * @Min ( 0 ) private Integer sugar; /** amount of chocolate *
     * @Min ( 0 ) private Integer chocolate;
     */
    @OneToMany ( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    private final List<Ingredient> ingredients;

    /**
     * Empty constructor for Hibernate
     */
    public Inventory () {
        // Intentionally empty so that Hibernate can instantiate
        // Inventory object.
        ingredients = new ArrayList<Ingredient>();
    }

    /**
     * Use this to create inventory with specified amts.
     *
     * @param coffee
     *            amt of coffee
     * @param milk
     *            amt of milk
     * @param sugar
     *            amt of sugar
     * @param chocolate
     *            amt of chocolate
     */
    public Inventory ( final Integer coffee, final Integer milk, final Integer sugar, final Integer chocolate ) {
        ingredients = new ArrayList<Ingredient>();
        // ingredients.add( new Ingredient( "Coffee", coffee ) );
        // ingredients.add( new Ingredient( "Milk", milk ) );
        // ingredients.add( new Ingredient( "Sugar", sugar ) );
        // ingredients.add( new Ingredient( "Chocolate", chocolate ) );
        addIngredients( coffee, milk, sugar, chocolate );
    }

    /**
     * Returns the ID of the entry in the DB
     *
     * @return long
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * Set the ID of the Inventory (Used by Hibernate)
     *
     * @param id
     *            the ID
     */
    public void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Returns true if there are enough ingredients to make the beverage.
     *
     * @param r
     *            recipe to check if there are enough ingredients
     * @return true if enough ingredients to make the beverage
     */
    public boolean enoughIngredients ( final Recipe r ) {
        boolean isEnough = true;

        final List<Ingredient> ri = r.getIngredients();
        for ( int i = 0; i < ri.size(); i++ ) {

            final String rit = ri.get( i ).getIngredient();
            if ( r.getAmountByType( rit ) > ingredients.get( i ).getAmount() ) {
                isEnough = false;
            }
        }
        return isEnough;
    }

    public int getAmountByType ( final String it ) {
        for ( int i = 0; i < ingredients.size(); i++ ) {
            if ( ingredients.get( i ).getIngredient().equals( it ) ) {
                return ingredients.get( i ).getAmount();
            }
        }
        return 0;
    }

    public void changeAmountByType ( final String it, final Integer change ) {
        for ( int i = 0; i < ingredients.size(); i++ ) {
            if ( ingredients.get( i ).getIngredient().equals( it ) ) {
                ingredients.get( i ).setAmount( ingredients.get( i ).getAmount() + change );
                return;
            }
        }
    }

    public void setAmountByType ( final String it, final Integer change ) {
        for ( int i = 0; i < ingredients.size(); i++ ) {
            if ( ingredients.get( i ).getIngredient() == it ) {
                ingredients.get( i ).setAmount( change );
                return;
            }
        }
    }

    /**
     * Removes the ingredients used to make the specified recipe. Assumes that
     * the user has checked that there are enough ingredients to make
     *
     * @param r
     *            recipe to make
     * @return true if recipe is made.
     */
    public boolean useIngredients ( final Recipe r ) {
        if ( enoughIngredients( r ) ) {
            // setCoffee( getCoffee() - r.getCoffee() );
            // setMilk( getMilk() - r.getMilk() );
            // setSugar( getSugar() - r.getSugar() );
            // setChocolate( getChocolate() - r.getChocolate() );

            final List<Ingredient> ri = r.getIngredients();
            for ( int i = 0; i < ri.size(); i++ ) {
                changeAmountByType( ri.get( i ).getIngredient(), -ri.get( i ).getAmount() );
            }
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Adds ingredients to the inventory
     *
     * @param coffee
     *            amt of coffee
     * @param milk
     *            amt of milk
     * @param sugar
     *            amt of sugar
     * @param chocolate
     *            amt of chocolate
     * @return true if successful, false if not
     */
    public boolean addIngredients ( final Integer coffee, final Integer milk, final Integer sugar,
            final Integer chocolate ) {
        if ( coffee < 0 || milk < 0 || sugar < 0 || chocolate < 0 ) {
            throw new IllegalArgumentException( "Amount must be positive." );
        }

        addCustomIngredient( new Ingredient( "Coffee", coffee ) );
        addCustomIngredient( new Ingredient( "Milk", milk ) );
        addCustomIngredient( new Ingredient( "Sugar", sugar ) );
        addCustomIngredient( new Ingredient( "Chocolate", chocolate ) );

        return true;
    }

    // xliang8, add the first 4 ingredient amount
    public boolean addIngredientAmount ( final Integer coffee, final Integer milk, final Integer sugar,
            final Integer chocolate ) {
        // looking by name
        ingredients.get( 0 ).setAmount( ingredients.get( 0 ).getAmount() + coffee );
        ingredients.get( 1 ).setAmount( ingredients.get( 1 ).getAmount() + milk );
        ingredients.get( 2 ).setAmount( ingredients.get( 2 ).getAmount() + sugar );
        ingredients.get( 3 ).setAmount( ingredients.get( 3 ).getAmount() + chocolate );
        return true;
    }

    // add new custom ingredient object
    public boolean addCustomIngredient ( final Ingredient ing ) {
        final String it = ing.getIngredient();
        final int amt = ing.getAmount();

        // check duplicate
        for ( int i = 0; i < ingredients.size(); i++ ) {
            if ( ingredients.get( i ).getIngredient().equals( it ) ) {
                return false;
            }
        }
        // add
        if ( amt > 0 ) {
            ingredients.add( ing );
        }
        return true;
    }

    // add the one custom ingredient amount
    public boolean addCustomIngredientAmount ( final Ingredient ing ) {
        final String it = ing.getIngredient();
        final int amt = ing.getAmount();

        // looking by name
        for ( int i = 0; i < ingredients.size(); i++ ) {
            if ( ingredients.get( i ).getIngredient().equals( it ) ) {
                ingredients.get( i ).setAmount( ingredients.get( i ).getAmount() + amt );
                return true;
            }
        }
        return false;
    }

    public List<Ingredient> getIngredients () {
        return ingredients;
    }

    /**
     * Returns a string describing the current contents of the inventory.
     *
     * @return String
     */
    @Override
    public String toString () {
        final StringBuffer buf = new StringBuffer();
        for ( int i = 0; i < ingredients.size(); i++ ) {
            buf.append( ingredients.get( i ).toString() );
        }
        return buf.toString();
    }

}

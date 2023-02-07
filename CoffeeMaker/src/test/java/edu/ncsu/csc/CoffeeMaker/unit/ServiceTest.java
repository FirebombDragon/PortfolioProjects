package edu.ncsu.csc.CoffeeMaker.unit;

import java.util.List;

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
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

/**
 * @author Xiaochun Liang
 *
 */
@RunWith ( SpringRunner.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class ServiceTest {

    @Autowired
    private RecipeService service;

    @Before
    public void setup () {
        service.deleteAll();

    }

    @Test
    @Transactional
    public void testfindByName () {

        final Recipe r1 = new Recipe();
        r1.setName( "Black Coffee" );
        r1.setPrice( 1 );
        r1.setAmountByType( "Coffee", 1 );
        r1.setAmountByType( "Milk", 0 );
        r1.setAmountByType( "Sugar", 0 );
        r1.setAmountByType( "Chocolate", 0 );
        service.save( r1 );

        final Recipe r2 = new Recipe();
        r2.setName( "Mocha" );
        r2.setPrice( 1 );
        r2.setAmountByType( "Coffee", 1 );
        r2.setAmountByType( "Milk", 1 );
        r2.setAmountByType( "Sugar", 1 );
        r2.setAmountByType( "Chocolate", 1 );
        service.save( r2 );

        final List<Recipe> recipes = service.findAll();
        Assert.assertEquals( "Creating two recipes should result in two recipes in the database", 2, recipes.size() );

        Assert.assertEquals( "The retrieved recipe should match the created one", r1, recipes.get( 0 ) );

        Assert.assertEquals( null, service.findByName( "abc" ) );

        // find it
        final Recipe f1 = service.findByName( "Black Coffee" );

        Assert.assertEquals( r1.getPrice(), f1.getPrice() );
        Assert.assertEquals( r1.getAmountByType( "Coffee" ), f1.getAmountByType( "Coffee" ) );
        Assert.assertEquals( r1.getAmountByType( "Milk" ), f1.getAmountByType( "Milk" ) );
        Assert.assertEquals( r1.getAmountByType( "Sugar" ), f1.getAmountByType( "Sugar" ) );
        Assert.assertEquals( r1.getAmountByType( "Chocolate" ), f1.getAmountByType( "Chocolate" ) );

        Assert.assertEquals( r1, f1 );

        final Recipe f2 = service.findByName( "Mocha" );

        Assert.assertEquals( r2.getPrice(), f2.getPrice() );
        Assert.assertEquals( r2.getAmountByType( "Coffee" ), f2.getAmountByType( "Coffee" ) );
        Assert.assertEquals( r2.getAmountByType( "Milk" ), f2.getAmountByType( "Milk" ) );
        Assert.assertEquals( r2.getAmountByType( "Sugar" ), f2.getAmountByType( "Sugar" ) );
        Assert.assertEquals( r2.getAmountByType( "Chocolate" ), f2.getAmountByType( "Chocolate" ) );

        Assert.assertEquals( r2, f2 );

    }

    @Test
    @Transactional
    public void testExistsById () {
        Assert.assertEquals( "There should be no Recipes in the CoffeeMaker", 0, service.findAll().size() );

        // Assert.assertEquals( null, service.existsById( 0 ));

        final Recipe r1 = new Recipe();
        r1.setName( "Black Coffee" );
        r1.setPrice( 1 );
        r1.setAmountByType( "Coffee", 1 );
        r1.setAmountByType( "Milk", 0 );
        r1.setAmountByType( "Sugar", 0 );
        r1.setAmountByType( "Chocolate", 0 );
        service.save( r1 );

        final long idR1 = r1.getId();

        Assert.assertEquals( true, service.existsById( idR1 ) );

        final long idR2 = 0;
        Assert.assertEquals( false, service.existsById( idR2 ) );

    }

    @Test
    @Transactional
    public void testFindById () {
        Assert.assertEquals( "There should be no Recipes in the CoffeeMaker", 0, service.findAll().size() );

        final Recipe r1 = new Recipe();
        r1.setName( "Black Coffee" );
        r1.setPrice( 1 );
        r1.setAmountByType( "Coffee", 1 );
        r1.setAmountByType( "Milk", 0 );
        r1.setAmountByType( "Sugar", 0 );
        r1.setAmountByType( "Chocolate", 0 );
        service.save( r1 );

        final List<Recipe> recipes = service.findAll();

        Assert.assertEquals( "The retrieved recipe should match the created one", r1, recipes.get( 0 ) );

        Assert.assertEquals( null, service.findByName( "abc" ) );

        // find it

        final long idR1 = r1.getId();
        final Recipe f1 = service.findById( idR1 );

        Assert.assertEquals( r1.getPrice(), f1.getPrice() );
        Assert.assertEquals( r1.getAmountByType( "Coffee" ), f1.getAmountByType( "Coffee" ) );
        Assert.assertEquals( r1.getAmountByType( "Milk" ), f1.getAmountByType( "Milk" ) );
        Assert.assertEquals( r1.getAmountByType( "Sugar" ), f1.getAmountByType( "Sugar" ) );
        Assert.assertEquals( r1.getAmountByType( "Chocolate" ), f1.getAmountByType( "Chocolate" ) );

        Assert.assertEquals( r1, f1 );

        final long idR2 = 0;
        Assert.assertEquals( null, service.findById( null ) );
        Assert.assertEquals( null, service.findById( idR2 ) );

    }

}

const express = require('express');
const shoppingRouter = express.Router();
const database = require('./DBConnection');
const Shopping = require('./models/Shopping');
const Ingredient = require('./models/Ingredient'); 

const { TokenMiddleware } = require('./middleware/tokenMiddleware');

let shopping = require(__dirname + '/data/shopping.json');
let users = require(__dirname + '/data/users.json');

// Retrieves the shopping list for the current user
shoppingRouter.get('/users/current/shopping', TokenMiddleware, (req, res) => {
    database.query('SELECT * FROM user_shopping JOIN ingredient ON shop_ing_id = ingredient.ing_id WHERE shop_user_id = ?', [req.user.id]).then(({results}) => {
        console.log(results);
        res.json(results.map(shopitem => new Shopping(shopitem)));
    }).catch((err) => { 
        res.json(err);
    })
});

// Clears one item from the current user's shopping list
shoppingRouter.delete('/users/current/shopping/:id', TokenMiddleware, (req, res) => {
    database.query('DELETE FROM user_shopping WHERE shop_ing_id=?', [req.params.id]).then(() => {
        res.json({message: 'success'});
    });
});

// Completely clears the current user's' shopping list
shoppingRouter.delete('/users/current/shopping', TokenMiddleware, (req, res) => {
    database.query('DELETE FROM user_shopping WHERE shop_user_id = ?', [req.user.id]).then(() => {
        res.json({message: 'success'});
    });
});

// Adds an ingredient to the current user's shopping list
shoppingRouter.post('/users/current/shopping', TokenMiddleware, (req, res) => {
    database.query('INSERT INTO user_shopping VALUES (?, ?)', [req.user.id, req.body.id]).then(({results}) => {
        res.json({status: 'success'});
    });
});

module.exports = shoppingRouter;

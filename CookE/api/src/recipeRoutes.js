const express = require('express');
const recipeRouter = express.Router();

const db = require('./db/DBConnection');
const Recipe = require('./db/models/recipe');
const Instruction = require('./db/models/instruction');
const Ingredient = require('./db/models/ingredient');

const { TokenMiddleware } = require('./middleware/tokenMiddleware');

let recipes = require(__dirname + '/data/recipes.json');

// Retrieves a specified recipe owned by the current user
recipeRouter.get('/users/current/recipes/:id', TokenMiddleware, (req, res) => {
    db.query('SELECT * FROM recipe WHERE recipe_id=?', [req.params.id]).then(({results}) => {
        if(results[0]) {
            const recipeData = results[0];
            db.query('SELECT * FROM recipe_ingredient JOIN ingredient ON ri_ing_id = ingredient.ing_id WHERE ri_recipe_id=?', [req.params.id]).then(({results}) => {
                let ingredients = [];
                for(const ing of results) {
                    ingredients.push(new Ingredient(ing));
                }
                db.query('SELECT * FROM instruction WHERE instr_recipe_id=?', [req.params.id]).then(({results}) => {
                    let steps = [];
                    for(const step of results) {
                        steps.push(new Instruction(step));
                    }
                    steps.sort((a, b) => {
                        return a.index - b.index;
                    })
                    let recipe = new Recipe(recipeData, ingredients, steps)
                    console.log(recipe);
                    res.json(recipe);
                });
            });
        }
        else {
            res.status(404).json({error: 'Recipe not found'});
        }
    })
});

// Creates a new recipe, owned by the current user
recipeRouter.post('/users/current/recipes', TokenMiddleware, (req, res) => {
    db.query('INSERT INTO recipe (recipe_name, owner_id) VALUES (?, ?)', [req.body.name, req.user.id]).then(({results}) => {
        const recipeID = results.insertId;
        for(const ing of req.body.ingredients) {
            db.query('INSERT INTO ingredient (name, amount, unit) VALUES (?, ?, ?)', [ing.ingredient, ing.amount, ing.unit]).then(({results}) => {
                db.query('INSERT INTO recipe_ingredient (ri_recipe_id, ri_ing_id) VALUES (?, ?)', [recipeID, results.insertId]);
            });
        }
        for(const step of req.body.steps) {
            db.query('INSERT INTO instruction (instr_recipe_id, step_num, step) VALUES (?, ?, ?)', [recipeID, step.index, step.instruction]);
        }
        res.json({status: 'success'});
    })
    
})

// Edits a recipe with the given id owned by the current user
recipeRouter.put('/users/current/recipes/:id', TokenMiddleware, (req, res) => {
    db.query('UPDATE recipe SET recipe_name = ? WHERE recipe_id = ?', [req.body.name, req.params.id]).then(({results}) => {
        if(results.affectedRows > 0) {
            db.query('SELECT ri_ing_id FROM recipe_ingredient WHERE ri_recipe_id = ?', [req.params.id]).then(({results}) => {
                let keptIngIds = [];
                for(const ing of req.body.ingredients) {
                    if(ing.id == "-1") {
                        db.query('INSERT INTO ingredient (name, amount, unit) VALUES (?, ?, ?)', [ing.ingredient, ing.amount, ing.unit]).then(({results}) => {
                            db.query('INSERT INTO recipe_ingredient (ri_recipe_id, ri_ing_id) VALUES (?, ?)', [req.params.id, results.insertId]);
                        });
                    }
                    else {
                        keptIngIds.push(ing.id);
                        db.query('UPDATE ingredient SET name = ?, amount = ?, unit = ? WHERE ing_id = ?', [ing.ingredient, ing.amount, ing.unit, ing.id])  
                    }               
                }
                for(const id of results) {
                    if(!keptIngIds.includes('' + id.ri_ing_id)) {
                        db.query('DELETE FROM recipe_ingredient WHERE ri_ing_id = ?', [id.ri_ing_id]).then(({results}) => {
                            db.query('DELETE FROM user_shopping WHERE shop_ing_id = ?', [id.ri_ing_id]).then(({results}) => {
                                db.query('DELETE FROM ingredient WHERE ing_id = ?', [id.ri_ing_id])
                            });
                            
                        });
                    }
                }
            });
            for(const step of req.body.steps) {
                db.query('UPDATE instruction SET step = ? WHERE instr_recipe_id = ? AND step_num = ?', [step.instruction, req.params.id, step.index]).then(({results}) => {
                    if(results.affectedRows == 0) {
                        db.query('INSERT INTO instruction (instr_recipe_id, step_num, step) VALUES (?, ?, ?)', [req.params.id, step.index, step.instruction]);
                    }
                })
            }
            db.query('DELETE FROM instruction WHERE instr_recipe_id = ? AND step_num > ?', [req.params.id, req.body.steps.length]);
            db.query('SELECT * FROM recipe WHERE recipe_id=?', [req.params.id]).then(({results}) => {
                res.json(new Recipe(results[0]));
            });
        }
        else {
            res.status(404).json({error: 'Recipe not found'});
        }
    })
})

// Deletes a sepecified recipe owned by the current user
recipeRouter.delete('/users/current/recipes/:id', TokenMiddleware, (req, res) => {
    db.query('SELECT * FROM recipe_ingredient WHERE ri_recipe_id = ?', [req.params.id]).then(({results}) => {
        if(results[0]) {
            const ingredients = results
            db.query('DELETE FROM recipe_ingredient WHERE ri_recipe_id = ?', [req.params.id]).then(({results}) => {
                db.query('DELETE FROM instruction WHERE instr_recipe_id = ?', [req.params.id]).then(({results}) => {
                    db.query('DELETE FROM recipe WHERE recipe_id = ?', [req.params.id]);
                    for(const ing of ingredients) {
                        db.query('DELETE FROM user_shopping WHERE shop_ing_id = ?', [ing.ri_ing_id]).then(({results}) => {
                            db.query('DELETE FROM ingredient WHERE ing_id = ?', [ing.ri_ing_id]);
                        });
                    }
                    res.json({status: 'success'});
                });               
            });
        }
        else {
            res.status(404).json({error: 'Recipe not found'});
        }
    });
});

// Retrieves all of the current user's recipes
recipeRouter.get('/users/current/recipes', TokenMiddleware, (req, res) => {
    db.query('SELECT * FROM recipe where owner_id = ?', [req.user.id]).then(({results}) => {
        console.log(results);
        res.json(results.map(recipe => new Recipe(recipe)));
    }).catch((err) => { 
        res.json(err);
    })
});

module.exports = recipeRouter;
const express = require('express');
const userRouter = express.Router();

const crypto = require('crypto');

const db = require('./db/DBConnection');
const { TokenMiddleware, generateToken, removeToken } = require('./middleware/tokenMiddleware');

const User = require('./db/models/User');

let users = require(__dirname + '/data/users.json');

userRouter.get('/users/current', TokenMiddleware, (req, res) => {
    res.json(req.user);
})

// Creates a new user
userRouter.post('/users', (req, res) => {
    db.query('SELECT * FROM user WHERE username = ?', [req.body.username]).then(({results}) => {
        if(results[0]) {
            res.status(409).json({error: 'Invalid username'});
        }
        else {
            const characters = "abcdefghijklmnopqrstuvwxyz1234567890";
            let salt = "";
            for(let i = 0; i < 32; i++) {
                salt = salt + characters.charAt(Math.floor(Math.random() * characters.length))
            }
            crypto.pbkdf2(req.body.password, salt, 100000, 64, 'sha512', (err, derivedKey) => { 
                if(err) {
                    res.status(400).json({error: err});
                }
                const digest = derivedKey.toString('hex');
                db.query('INSERT INTO user (username, salt, password, first_name, last_name, email) VALUES (?, ?, ?, ?, ?, ?)', [req.body.username, salt, digest, req.body.first, req.body.last, req.body.email]).then(({results}) => {
                    db.query('SELECT * FROM user WHERE user_id = ?', [results.insertId]).then(({results}) => {
                        res.json(new User(results[0]));
                    });
                })
            });
        }
    });
});

// Attempts to log a user in if provided username and password match
userRouter.post('/login', (req, res) => {
    let credentials = req.body;
    console.log(credentials);
    db.query('SELECT * FROM user WHERE username = ?', [credentials.username]).then(({results}) => {
        console.log(results[0]);
        crypto.pbkdf2(credentials.password, results[0].salt, 100000, 64, 'sha512', (err, derivedKey) => {
            if(err) {
                res.status(400).json({error: err});
            }
            const digest = derivedKey.toString('hex');
            if (results[0].password == digest) {
                let user = new User(results[0]);
                console.log(user);
                generateToken(req, res, user);
                res.json(user);
            }
            else {
                res.status(401).json({error: 'Invalid username or password'});
            }
        });
    })
});

userRouter.post('/logout', (req, res) => {
    removeToken(req, res);
    res.json({success: true});
})

module.exports = userRouter;
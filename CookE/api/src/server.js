const express = require('express');

const app = express();
const PORT = process.env.PORT;

const cookieParser = require('cookie-parser');
app.use(cookieParser());

const userRouter = require(__dirname+ '/userRoutes');
const recipeRouter = require(__dirname+ '/recipeRoutes');
const shoppingRouter = require(__dirname+ '/shoppingRoutes');
app.use(express.urlencoded({extended: true}));
app.use(express.json());

app.use(userRouter);
app.use(recipeRouter);
app.use(shoppingRouter);

// As our server to listen for incoming connections
app.listen(PORT, () => console.log(`Server listening on port: ${PORT}`));
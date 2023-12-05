const express = require('express');

const app = express();
const PORT = process.env.PORT;

// Designate the static folder as serving static resources
app.use(express.static(__dirname + '/static'));

app.get('/recipelist', (req, res) => {
    res.sendFile(__dirname + '/templates/recipelist.html');
});

app.get('/shoppinglist', (req, res) => {
    res.sendFile(__dirname + '/templates/shoppinglist.html');
});

// Route to get the view recipe page.  Use query params of recipe id to view the desired recipe
app.get('/viewRecipe', (req, res) => {
    res.sendFile(__dirname + '/templates/viewrecipe.html');
});

// Route to get the add recipe page
app.get('/addRecipe', (req, res) => {
    res.sendFile(__dirname + '/templates/addrecipe.html');
});

// Route to get the edit recipe page.  Use query params of recipe id to edit the desired recipe
app.get('/editRecipe', (req, res) => {
    res.sendFile(__dirname + '/templates/editrecipe.html');
});

app.get('/login', (req, res) => {
    res.sendFile(__dirname + '/templates/login.html');
});

app.get('/register', (req, res) => {
    res.sendFile(__dirname + '/templates/registration.html');
});

app.get('/offline', (req, res) => {
    res.sendFile(__dirname + '/templates/offline.html');
});

// As our server to listen for incoming connections
app.listen(PORT, () => console.log(`Server listening on port: ${PORT}`));
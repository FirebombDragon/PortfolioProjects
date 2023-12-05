import api from '/js/APIClient.js';

const query = window.location.search;
let parameters = new URLSearchParams(query);
let id = parameters.get('id');

// Uses the backend API to retrieve the recipe with the requested ID, then fill out the page with the recipe data
api.getRecipeByID(id).then(recipe => {

    // Fill out recipe name
    document.querySelector(".recipe-name").textContent = recipe.name;

    // Fill out the list of ingredients (ingredient, amount, unit)
    let ingredients = document.querySelector(".ingredients");
    for(const item of recipe.ingredients) {
        ingredients.appendChild(document.createElement("tr"));

        let ingredient = document.createElement("td");
        ingredient.textContent = item.name;
        ingredients.lastChild.appendChild(ingredient);

        let amount = document.createElement("td");
        amount.textContent = item.amount + " " + item.unit;
        ingredients.lastChild.appendChild(amount);

        let hiddenId = document.createElement("td");
        hiddenId.classList = "d-none";
        hiddenId.textContent = item.id;
        ingredients.lastChild.appendChild(hiddenId);

        // Create a button to add the ingredient to the shopping list
        let shopping = document.createElement("td");
        shopping.className = "text-center align-middle py-lg-1";
        let shoppingBtn = document.createElement("span");
        shoppingBtn.textContent = "+";
        shoppingBtn.className = "text-center text-white px-3 pb-lg-1 pt-0 fw-bold rounded-3 btn btn-primary";
        shoppingBtn.addEventListener('click', (e) => {
            let ingr = e.target.parentElement.parentElement.firstChild;
            let amt = ingr.nextSibling;
            let id = amt.nextSibling;
            api.addToShoppingList(ingr.textContent, amt.textContent, id.textContent);
        });
        shopping.appendChild(shoppingBtn);
        ingredients.lastChild.appendChild(shopping);
    }

    // Fill out the list of recipe steps
    let steps = document.querySelector(".steps");
    for(const item of recipe.instructions) {
        let instruction = document.createElement("li");
        instruction.textContent = item.step;
        steps.appendChild(instruction);
    }
});

// Link to edit recipe page when the button is pressed
let editURL = "/editRecipe?id=" + id;
document.querySelector(".edit-recipe").addEventListener('click', (e) => {
    window.location = editURL;
});

// Delete the recipe when the button is pressed
document.querySelector(".delete-recipe").addEventListener('click', (e) => {
    api.deleteRecipe(api.getRecipeByID(id), id);
    window.location = '/recipelist';
});
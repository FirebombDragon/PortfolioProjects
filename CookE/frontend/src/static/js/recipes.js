import api from './APIClient.js';
const recipelist = document.querySelector("#recipelist");

function clearRecipes() {
    const recipes = document.querySelectorAll("#recipelist > *");
    recipes.forEach(element => {
        element.parentNode.removeChild(element);
    })
}

function updateRecipes(phrase) {
    clearRecipes();
    api.getRecipes().then(recipes => {
        recipes.forEach(recipe => {
            if (recipe.name.toLowerCase().includes(phrase.toLowerCase())) {
                const button = document.createElement('button');
                button.innerHTML = recipe.name;
                button.className = "btn btn-secondary border";
                const recipelink = document.createElement('a');
                recipelink.href = "/viewrecipe?id=" + recipe.id;
                recipelink.append(button);
                recipelist.append(recipelink);
                const br = document.createElement('br');
                recipelist.append(br);
            }
        });
    })
}

const search = document.querySelector("#searchbar");
search.addEventListener('change', e => {
    const phrase = search.value;
    updateRecipes(phrase);
});

updateRecipes("");
import api from './APIClient.js';
const shoppinglist = document.querySelector("#shoppinglist");

api.getShoppingList().then(ingredients => {
    ingredients.forEach(ingredient => {
        const listitem = document.createElement('li');
        listitem.className = "list-group-item";
        const checkbox = document.createElement('input');
        checkbox.type = "checkbox";
        const label = document.createElement('label');
        checkbox.addEventListener('click', e => {
            console.log(label.className);
            console.log(label.className == "text-decoration-line-through");
            if (label.className != "text-decoration-line-through") {
                console.log('Deleting ' + JSON.stringify(ingredient));
                label.className = "text-decoration-line-through";
                api.deleteIngredientFromList(ingredient.id);
            }
            else {
                console.log('Adding ' + JSON.stringify(ingredient));
                label.className = "";
                api.addToShoppingList(ingredient.ingredient, 1, ingredient.id);
            }
        })
        checkbox.className = "ingredient";
        label.append(checkbox);
        label.append(ingredient.ingredient);
        listitem.append(label);
        shoppinglist.append(listitem);
    });
});

let deleteAll = function() {
    api.deleteShoppingList();
    let items = document.querySelectorAll("#shoppinglist > li");
    items.forEach(element => {
        element.parentNode.removeChild(element);
    });
}

const clear = document.querySelector("#clearall");

clear.addEventListener('click', e => {
    deleteAll();
});
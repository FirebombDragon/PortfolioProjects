import api from '/js/APIClient.js';

const query = window.location.search;
let parameters = new URLSearchParams(query);
let id = parameters.get('id');

// Add a button to remove an ingredient row
function ingrRemoveButton(node) {
    let removeBtn = document.createElement("button");
    removeBtn.textContent = "-";
    removeBtn.classList = "btn btn-primary text-white fw-bold py-0 m-0";
    removeBtn.setAttribute("type", "button");
    removeBtn.addEventListener('click', (e) => {
        e.target.parentElement.parentElement.parentElement.removeChild(e.target.parentElement.parentElement);
    })
    node.appendChild(removeBtn);
}

// Add a field for a new ingredient name.  Fill it out if there is a pre-existing value for it
function ingrField(node, val) {
    let ingredientField = document.createElement("input");
    ingredientField.className = "w-100";
    ingredientField.setAttribute("placeholder", "Ingredient");
    ingredientField.setAttribute("required", "required");
    if(val) {
        ingredientField.value = val;
    }
    node.appendChild(ingredientField);
}

// Add fields for new ingredient amount and unit.  Fill them out if there are pre-existing values for it
function amtFields(node, amount, unit) {
    let amountField = document.createElement("input");
    amountField.className = "w-25";
    amountField.setAttribute("placeholder", "#");
    amountField.setAttribute("required", "required");
    amountField.setAttribute("type", "number");
    amountField.setAttribute("step", "any");
    let unitField = document.createElement("input");
    unitField.className = "w-50";
    unitField.setAttribute("placeholder", "Unit");
    unitField.setAttribute("required", "required");
    if(amount && unit) {
        amountField.value = amount;
        unitField.value = unit;
    }
    node.appendChild(amountField);
    node.appendChild(unitField);
}

// HTML document nodes containing the list of ingredients and steps
const ingredientList = document.querySelector(".ingredients");
const stepsList = document.querySelector(".steps");

// Uses the backend API to retrieve the recipe with the requested ID, then fill out the page with the recipe data
api.getRecipeByID(id).then(recipe => {
    // Fill out recipe name
    document.querySelector(".recipe-name").value = recipe.name;

    // Fill out the list of ingredients (ingredient, amount, unit)
    for(const item of recipe.ingredients) {
        ingredientList.appendChild(document.createElement("tr"));

        let remove = document.createElement("td");
        ingrRemoveButton(remove);
        ingredientList.lastChild.appendChild(remove);

        let ingredientData = document.createElement("td");
        ingrField(ingredientData, item.name);
        ingredientList.lastChild.appendChild(ingredientData);

        let amountData = document.createElement("td");
        amtFields(amountData, item.amount, item.unit);
        ingredientList.lastChild.appendChild(amountData);

        let hiddenId = document.createElement("td");
        hiddenId.classList = "d-none";
        hiddenId.textContent = item.id;
        ingredientList.lastChild.appendChild(hiddenId);
    }

    // Fill out the list of instructions
    for(const step of recipe.instructions) {
        stepsList.appendChild(document.createElement("li"));

        let instructField = document.createElement("input");
        instructField.className = "w-75 m-3";
        instructField.setAttribute("placeholder", "Instruction");
        instructField.setAttribute("required", "required");
        instructField.value = step.step;
        stepsList.lastChild.appendChild(instructField);

        let removeBtn = document.createElement("button");
        removeBtn.textContent = "-";
        removeBtn.classList = "btn btn-primary text-white fw-bold py-0 m-0";
        removeBtn.setAttribute("type", "button");
        removeBtn.addEventListener('click', (e) => {
            e.target.parentElement.parentElement.removeChild(e.target.parentElement);
        })
        stepsList.lastChild.appendChild(removeBtn);
    }
})

// Add a new ingredient input row
document.querySelector(".ingredient-button").addEventListener('click', (e) => {
    ingredientList.appendChild(document.createElement("tr"));

    // Add a removal button for this ingredient
    let remove = document.createElement("td");
    ingrRemoveButton(remove);
    ingredientList.lastChild.appendChild(remove);

    // Add ingredient name input field
    let ingredientData = document.createElement("td");
    ingrField(ingredientData, null);
    ingredientList.lastChild.appendChild(ingredientData);

    // Add ingredient amount input fields
    let amountData = document.createElement("td");
    amtFields(amountData, null, null);
    ingredientList.lastChild.appendChild(amountData);

    let hiddenId = document.createElement("td");
    hiddenId.classList = "d-none";
    hiddenId.textContent = "-1";
    ingredientList.lastChild.appendChild(hiddenId);
});

// Add a new instruction input row
document.querySelector(".step-button").addEventListener('click', (e) => {
    stepsList.appendChild(document.createElement("li"));

    // Add a new instrction input field
    let instructField = document.createElement("input");
    instructField.className = "w-75 m-3";
    instructField.setAttribute("placeholder", "Instruction");
    instructField.setAttribute("required", "required");
    stepsList.lastChild.appendChild(instructField);

    // Add a removal button for this step
    let removeBtn = document.createElement("button");
    removeBtn.textContent = "-";
    removeBtn.classList = "btn btn-primary text-white fw-bold py-0 m-0";
    removeBtn.setAttribute("type", "button");
    removeBtn.addEventListener('click', (e) => {
        e.target.parentElement.parentElement.removeChild(e.target.parentElement);
    })
    stepsList.lastChild.appendChild(removeBtn);
});

// Update the existing recipe based on the input data
document.querySelector("form").addEventListener('submit', (e) => {
    e.preventDefault();
    // Construct a recipe object
    let recipe = {
        name: "",
        ingredients: [],
        steps: []
    }

    // Fill out recipe name
    recipe.name = document.querySelector(".recipe-name").value;

    // Fill out recipe ingredients list
    for(const ingr of ingredientList.children) {
        let i = {
            id: "",
            ingredient: "",
            amount: "",
            unit: ""
        }
        i.ingredient = ingr.firstElementChild.nextElementSibling.firstElementChild.value;
        i.amount = ingr.firstElementChild.nextElementSibling.nextElementSibling.firstElementChild.value;
        i.unit = ingr.firstElementChild.nextElementSibling.nextElementSibling.lastElementChild.value;
        i.id = ingr.firstElementChild.nextElementSibling.nextElementSibling.nextElementSibling.textContent;
        recipe.ingredients.push(i);
    }

    // Fill out ingredient steps list
    let idx = 1;
    for(const step of stepsList.children) {
        let s = {
            index: idx,
            instruction: step.firstChild.value
        }
        recipe.steps.push(s);
        idx += 1;
    }

    // Add recipe using bckend API
    api.editRecipe(recipe, id).then(response => {
        const query2 = window.location.search;
        let parameters = new URLSearchParams(query2);
        window.location = `/viewrecipe?id=` + parameters.get('id');
    });

    
});
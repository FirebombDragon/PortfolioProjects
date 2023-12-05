import api from '/js/APIClient.js';

// HTML document nodes containing the list of ingredients and steps
const ingredientList = document.querySelector(".ingredients");
const stepsList = document.querySelector(".steps");

// Add a new ingredient input row
document.querySelector(".ingredient-button").addEventListener('click', (e) => {
    ingredientList.appendChild(document.createElement("tr"));

    // Add a removal button for this ingredient
    let remove = document.createElement("td");
    let removeBtn = document.createElement("button");
    removeBtn.textContent = "-";
    removeBtn.classList = "btn btn-primary text-white fw-bold py-0 m-0";
    removeBtn.setAttribute("type", "button");
    removeBtn.addEventListener('click', (e) => {
        e.target.parentElement.parentElement.parentElement.removeChild(e.target.parentElement.parentElement);
    })
    remove.appendChild(removeBtn);
    ingredientList.lastChild.appendChild(remove);

    // Add ingredient name input field
    let ingredientData = document.createElement("td");
    let ingredientField = document.createElement("input");
    ingredientField.className = "w-100";
    ingredientField.setAttribute("placeholder", "Ingredient");
    ingredientField.setAttribute("required", "required");
    ingredientData.appendChild(ingredientField);
    ingredientList.lastChild.appendChild(ingredientData);

    // Add ingredient amount input field
    let amountData = document.createElement("td");
    let amountField = document.createElement("input");
    amountField.className = "w-25";
    amountField.setAttribute("placeholder", "#");
    amountField.setAttribute("required", "required");
    amountField.setAttribute("type", "number");
    amountField.setAttribute("step", "any");
    //Add ingredient unit input field
    let unitField = document.createElement("input");
    unitField.className = "w-50";
    unitField.setAttribute("placeholder", "Unit");
    unitField.setAttribute("required", "required");
    amountData.appendChild(amountField);
    amountData.appendChild(unitField);
    ingredientList.lastChild.appendChild(amountData);
});

// Add a new instruction input row
document.querySelector(".step-button").addEventListener('click', (e) => {
    stepsList.appendChild(document.createElement("li"));

    // Add a new instruction input field
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

// Add a new recipe based on the input data
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
            ingredient: "",
            amount: "",
            unit: ""
        }
        i.ingredient = ingr.firstElementChild.nextElementSibling.firstElementChild.value;
        i.amount = ingr.firstElementChild.nextElementSibling.nextElementSibling.firstElementChild.value;
        i.unit = ingr.firstElementChild.nextElementSibling.nextElementSibling.lastElementChild.value;
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
    api.addRecipe(recipe);
});
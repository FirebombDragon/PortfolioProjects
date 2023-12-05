module.exports = class {
    id = null;
    name = null;
    ingredients = [];
    instructions = [];

    constructor(recipeData, ingList, instrList) {
        this.id = recipeData.recipe_id;
        this.name = recipeData.recipe_name;
        this.ingredients = ingList;
        this.instructions = instrList;
    }
}
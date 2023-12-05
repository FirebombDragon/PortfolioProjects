module.exports = class {
    id = null;
    name = null;
    first_step = null;

    constructor(data) {
        this.id = data.recipe_id;
        this.name = data.recipe_name;
        this.first_step = data.first_step_id;
    }
}
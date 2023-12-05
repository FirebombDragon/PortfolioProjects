module.exports = class {
    id = null;
    recipe = null;
    index = null;
    step = null;

    constructor(data) {
        this.id = data.instruct_id;
        this.recipe = data.instr_recipe_id;
        this.index = data.step_num;
        this.step = data.step;
    }
}
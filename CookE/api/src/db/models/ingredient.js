module.exports = class {
    id = null;
    name = null;
    amount = null;
    unit = null;

    constructor(data) {
        this.id = data.ing_id;
        this.name = data.name;
        this.amount = data.amount;
        this.unit = data.unit;
    }
}
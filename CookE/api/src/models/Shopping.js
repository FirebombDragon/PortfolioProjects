module.exports = class {
    id = null;
    ingredient = null;
    user = null;

    constructor(data) {
        this.id = data.shop_ing_id;
        this.user = data.shop_user_id;
        this.ingredient = data.name;
    }
}
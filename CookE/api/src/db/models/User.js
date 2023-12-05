module.exports = class {
    id = null;
    username = null;
    #password = null;;
    #salt = null;;
  
    constructor(data) {
      this.id = data.user_id;
      this.username = data.username;
      this.#salt = data.salt;
      this.#password = data.password;
    }

    toJSON() {
        return {
            id: this.id,
            username: this.username
        }
    }

}
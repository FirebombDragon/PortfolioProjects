module.exports = class {
    id = null;
    username = null;
    #password = null;;
    #salt = null;;
    firstName = null;
    lastName = null;
    email = null;
  
    constructor(data) {
      this.id = data.user_id;
      this.username = data.username;
      this.#salt = data.salt;
      this.#password = data.password;
      this.firstName = data.first_name;
      this.lastName = data.last_name;
      this.email = data.email;
    }

    toJSON() {
        return {
            id: this.id,
            username: this.username,
            first: this.firstName,
            last: this.lastName,
            email: this.email
        }
    }

}
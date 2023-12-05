export default {
    // Retrieves a recipe with the requested id from the backend, if it exists
    getRecipeByID: (id) => {
        return fetch(`/api/users/current/recipes/${id}`).then(res => {
            console.log(res.ok)
            if(!res.ok) {
                throw new Error("error in request");
              }
              return res.json();
        });
    },
    
    getRecipes: () => {
        return fetch('/api/users/current/recipes').then(res => {
            console.log(res.ok)
            if (!res.ok) {
                throw new Error("error in request");
            }
            return res.json();
        });
    },

    getShoppingList: () => {
        return fetch('/api/users/current/shopping').then(res => {
            console.log(res.ok);
            if (!res.ok) {
                throw new Error("error in request");
            }
            return res.json();
        })
    },

    deleteIngredientFromList: (id) => {
        return fetch(`/api/users/current/shopping/${id}`, {method: "DELETE"}).then(res => {
            console.log(res.ok);
            if (!res.ok) {
                throw new Error("error in request");
            }
            return res.json();
        })
    },

    deleteShoppingList: () => {
        return fetch(`/api/users/current/shopping`, {method: "DELETE"}).then(res => {
            console.log(res.ok);
            if (!res.ok) {
                throw new Error("error in request");
            }
            return res.json();
        })
    },
    
    // Adds an ingredient (item and amount) to the shopping list in the backend
    addToShoppingList: (item, amount, id) => {
        return fetch('/api/users/current/shopping', {
            method: "post",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                id: id,
                item: item,
                amount: amount
            })
        }).then(res => {
            console.log(res.ok)
            if(!res.ok) {
                throw new Error("error in request");
              }
              return res.json();
        });
    },
    // Adds a new recipe to the backend data
    addRecipe: (recipe) => {
        return fetch('/api/users/current/recipes', {
            method: "post",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(recipe)
        }).then(res => {
            console.log(res.ok)
            if(!res.ok) {
                throw new Error("error in request");
              }
              return res.json();
        });
    },
    // Updates a recipe with the given id in the backend, if it exists
    editRecipe: (recipe, id) => {
        return fetch(`/api/users/current/recipes/${id}`, {
            method: "put",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(recipe)
        }).then(res => {
            console.log(res.ok)
            if(!res.ok) {
                throw new Error("error in request");
              }
              return res.json();
        });
    },
    deleteRecipe: (recipe, id) => {
        return fetch(`/api/users/current/recipes/${id}`, {
            method: "delete",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(recipe)
        }).then(res => {
            console.log(res.ok)
            if(!res.ok) {
                throw new Error("error in request");
              }
              return res.json();
        });
    },
    getCurrentUser: () => {
        return fetch(`/api/users/current`).then(res => {
            if(!res.ok) {
                throw new Error("error in request");
            }
            return res.json();
        }).catch(err => {
            console.log(err);
        });
    },
    login: (username, password) => {
        return fetch(`/api/login`, {
            method: "post",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                username: username,
                password: password
            })
        }).then(res => {
            if(!res.ok) {
                throw new Error("error in request");
              }
            return res.json();
        })
    },
    logout: (username) => {
        return fetch(`/api/logout`, {
            method: "post",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                username: username
            })
        }).then(res => {
            if(!res.ok) {
                throw new Error("error in request");
              }
            return res.json();
        })
    },
    createUser:(username, password, first, last, email) => {
        return fetch(`/api/users`, {
            method: "post",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                username: username,
                password: password,
                first: first,
                last: last,
                email: email
            })
        }).then(res => {
            if(!res.ok) {
                throw new Error("error in request");
              }
            return res.json();
        }).catch(err => {
            console.log(err);
        });
    }
}
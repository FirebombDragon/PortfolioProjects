import api from '/js/APIClient.js'

document.querySelector("form").addEventListener('submit', (e)=> {
    e.preventDefault();
    const user = document.getElementById("username").value;
    const pass = document.getElementById("password").value;
    api.createUser(user, pass).then(user => {
        if(user) {
            window.location = `/login`;
        }
        else {
            console.log("invalid user information")
        }
    })
});
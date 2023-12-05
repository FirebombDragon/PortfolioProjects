import api from '/js/APIClient.js'

document.querySelector("form").addEventListener('submit', (e)=> {
    e.preventDefault();
    const user = document.getElementById("username").value;
    const pass = document.getElementById("password").value;
    const first = document.getElementById("firstName").value;
    const last = document.getElementById("lastName").value;
    const email = document.getElementById("email").value;
    api.createUser(user, pass, first, last, email).then(user => {
        if(user) {
            window.location = `/login`;
        }
        else {
            console.log("invalid user information")
        }
    })
});
import api from '/js/APIClient.js'

api.getCurrentUser().then(currentUser => {
    if(currentUser) {
        window.location = `/recipelist`;
    }
});

document.querySelector("form").addEventListener('submit', (e)=> {
    e.preventDefault();
    const user = document.querySelector(".user").value;
    const pass = document.querySelector(".pass").value;
    console.log(user);
    api.login(user, pass).then(user => {
        if(user) {
            window.location = `/recipelist`;
        }
        else {
            console.log("invalid login")
        }
    })
});
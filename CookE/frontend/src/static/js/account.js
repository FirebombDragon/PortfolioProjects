import api from '/js/APIClient.js'

api.getCurrentUser().then(currentUser => {
    document.getElementById("firstName").value = currentUser.first;
    document.getElementById("lastName").value = currentUser.last;
    document.getElementById("email").value = currentUser.email;
})
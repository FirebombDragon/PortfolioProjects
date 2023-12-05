# Cook-E
## Group Q: Milestone 2

### What is done
<b>The following pages are completed: </b>
- `recipelist.html`: Users can view a list of recipes
- `shoppinglist.html`: Users can view a list of items in the shopping list
- `viewrecipe.html`: Users can view an individual recipe (with a given recipe ID)
- `addrecipe.html`: Users can view the page to create a new recipe, but the new recipe is not yet saved
- `editrecipe.html`: Users can view the page to make changes to an individual recipe (with a given recipe ID), but the changes are not yet saved.<br>
- `login.html`: The user can view the page to log in by entering their username and password.  User credentials are verified yet upon loging using tokens.
- `registration.html`: The user can enter their first name, last name, username, and password, and a new user object will be created and signed into the application.
<b>The following backend elements are completed: </b>
- Database is set up
- All required API endpoints are implemented and utilize the database
- Users are authenticated using JWT tokens

### What is not done
<b>The following pages still need to be done:</b>
- An account page that will allow users to see their account information

<b>Offline Functionality:</b><br>
The current implementation does not yet allow any offline functionality.  In the future, we will need to provide some offline functionality for certain features, such as viewing the shopping list.

### User Authentication
Users are required to log in to the system by providing a username and password.  User information is stored in the database.  Stored passwords are securely hashed along with a salt that is also stored in the database.  When a user logs in, their password is hashed with the same salt and compared to the one in the database.  If they match, then the system will create a JWT token containing the current user and store it as a cookie.  To utilize any other page or API route for our system, the user's browser must provide a valid cookie.  Before performing any action, a middleware function ensures that the caller provides a valid cookie.

### Implementation Status
| Page | Progress | Wireframe |
|------|----------|-----------|
| Login | ✔ |  |
| New User | ✔ | |
| Recipe List | ✔ | |
| Shopping List | ✔ | |
| View Recipe | ✔ | |
| Add Recipe | ✔ | |
| Edit Recipe | ✔ | |
| Account | 0% | [Wireframe](https://github.ncsu.edu/engr-csc342/csc342-2023Fall-GroupQ/blob/master/Proposal/Wireframes/Registration.png) (Right)|

### API Endpoints:
| Method | Route | Description |
|--|--|-------------------------|
| POST | `/login` | Receives a username and password, verifies user and creates a token |
| POST | `/logout` | Logs out a user |
| POST | `/users` | Creates a new account and returns the new object |
| GET | `/users/current` | Retrieves the user object for the currently logged in user |
| GET | `/users/current/recipes` | Gets all recipes owned by the current user |
| GET | `/users/current/recipes/:id` | Gets a recipe  owned by the current user with the specified ID, if it exists |
| POST | `/users/current/recipes` | Adds a new recipe |
| PUT | `/users/current/recipes/:id` | Edits a recipe owned by the current user with the specified ID, if it exists |
| DELETE | `/users/current/recipes/:id` |Removes a recipe owned by the current user with the specified ID, if it exists |
| GET | `/users/current/shopping` | Gets all items in the current user's shopping list |
| POST | `/users/current/shopping` | Adds an item to the current user's shopping list |
| DELETE | `/users/current/shopping/:ingredient` | Removes an item from the current user's shopping list |
| DELETE | `/users/current/shopping` | Clears the current user's shopping list |

### Database ER Diagram
![ER Diagram](https://github.ncsu.edu/engr-csc342/csc342-2023Fall-GroupQ/blob/master/Milestone2/diagrams/Milestone%202%20ER%20Diagram.png)

### Individual team member contributions to this milestone
<b>Robbie</b>: Responsible for setting up the database and API routes for getting a recipe, adding a recipe, editing a recipe, logging in (using tokens), and logging out.<br>
<b>Nikolaus</b>: Responsible for API endpoints relating to getting all recipes and all shopping list routes.<br>
<b>Arjun</b>: Responsible for login.html and registration.html.
All group members worked together on the report and screencast





























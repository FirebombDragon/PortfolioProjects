# Cook-E

![image](https://github.com/FirebombDragon/PortfolioProjects/assets/91640916/5a5c485c-8911-47f7-985e-7aa3f7cdcc0f)


### Features
Users can create accounts and log in to our application.  After logging in, they will see a list of recipes they have created.  They can view the details of these recipes to see the ingredients and instructions.  Users are able to edit their own recipes and create new ones.  When viewing a recipe, users can add ingredients from those recipes to a shopping list.  When viewing their shopping list, users are able to check off items as they purchase them.  Users can also view information about their account, such as their name and email.

### User Authentication
Users are required to log in to the system by providing a username and password.  User information is stored in the database.  Stored passwords are securely hashed along with a salt that is also stored in the database.  When a user logs in, their password is hashed with the same salt and compared to the one in the database.  If they match, then the system will create a JWT token containing the current user and store it as a cookie.  To utilize any other page or API route for our system, the user's browser must provide a valid cookie.  Before performing any action, a middleware function ensures that the caller provides a valid cookie.

### Pages
<b>Login:</b> Users can provide their username and password to log in.  Click "Register" to create a new account.<br>
<b>Register:</b> Create a new user profile by specifying name, email, username, and password.<br>
<b>Account:</b> View user information.<br>
<b>Recipe List:</b> Users can view a list of their recipes.  Click a recipe to view its details.  Click "Add Recipe" to create a new recipe.<br>
<b>Recipe Page:</b> Users can view the ingredients and instructions for a recipe.  Click "Edit Recipe" to edit these details.  Click "Delete Recipe" to delete the recipe.<br>
<b>Add Recipe:</b> Create a new recipe by specifying name, ingredients, and instructions.<br>
<b>Edit Recipe:</b> Change a recipe by updating name, ingredients, or instructions.<br>
<b>Shopping List:</b> View a list of items in the shopping list, or remove items from the list.<br>
Users can navigate between Recipe List, Shopping List, and Account pages using the navbar at the top of all pages.  Click "Log out" on the navbar to log out.

### Offline Functionality
After a user has visited a page while online, they will be able to revisit that page if they go offline.  The shopping list page is fetched when the user logs in, so even if the user were to lose connection before visiting this page, they will still be able to see this list.  If an offline user attempts to visit another page that has not been visited previously, they will be redirected to a specific offline page.

### Caching Strategy
Our caching strategy is a hybrid approach.  For static resources, such as html and javascript files, we utilize a cache-first strategy.  Since these files are unlikely to change as a user uses our application, it is more efficient to store these requests in the cache for easier, repeated access.  For calls to our API, we utilize a netwrok-first strategy.  Since the data for our application is likely to change frequently as a user creates, edits, and deletes recipes and shopping list items, it requires that the system make calls to the API over the network to obtain the most recent data.

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
![ER Diagram](https://github.com/FirebombDragon/PortfolioProjects/blob/main/CookE/diagrams/Milestone%202%20ER%20Diagram.png)



























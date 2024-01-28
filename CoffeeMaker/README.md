# Coffee Maker

### Features
This application is a simple web application that simulates a coffee shop. Users can add ingredients and recipes, then use those ingredients and recipes to simulate making coffee.  Users can also edit or delete recipes as necessary.

### Pages
<b>Main Menu:</b> Users can select what page they want to go to.<br>
<b>Add Ingredient:</b> Users can introduce new ingredients into the program.<br>
<b>Update Inventory:</b> Users can update the numbers of ingredients that have been added.<br>
<b>Add A Recipe (Add a Custom Recipe):</b> Users can add new recipes for use in the program.<br>
<b>Edit Recipe:</b> Users can change existing recipes for use in the program.<br>
<b>Delete Recipe:</b> Users can delete recipes that are no longer necessary.<br>
<b>Make Coffee:</b> Users can use existing recipes and ingredients to order coffee.<br>

### API Endpoints:
| Method | Route | Description |
|--|--|-------------------------|
| GET | `/ingredients` | Gets all ingredients in the system |
| GET | `/ingredients/{name}` | Gets a specific ingredient by name |
| POST | `/ingredients` | Creates a new ingredient and adds it to the system |
| DELETE | `/ingredients/{name}` | Removes an ingredient with a specific name |
| GET | `/inventory` | Gets the current inventory |
| POST | `/inventory` | Makes changes to the inventory |
| GET | `/recipes` | Gets all recipes in the system |
| GET | `/recipes/{name}` | Gets a specific recipe by name |
| POST | `/recipes` | Creates a new recipe and adds it to the system |
| DELETE | `/recipes/{name}` | Removes a recipe with a specific name |
| PUT | `/recipes/{name}` | Makes edits to a recipe with a specific name |
| POST | `/makecoffee/{name}` | Puts an order for a coffee if the recipe is valid, the inventory contains the ingredients necessary, and the customer provides proper payment |

import java.util.List;

public class ShoppingList
{
    List<Ingredient> ingredients;
    List<WeeklyMenu> weeklyMenus;
    List<Recipe> recipes;

    public ShoppingList(List<Ingredient> ingredients, 
                        List<WeeklyMenu> weeklyMenus, 
                        List<Recipe> recipes) 
    {
        this.ingredients = ingredients;
        this.weeklyMenus = weeklyMenus;
        this.recipes = recipes;
    }

    public List<Ingredient> getIngredients() { return ingredients; }
    public void setIngredients(List<Ingredient> ingredients) { this.ingredients = ingredients; }
    public List<WeeklyMenu> getWeeklyMenus() { return weeklyMenus; }
    public void setWeeklyMenus(List<WeeklyMenu> weeklyMenus) { this.weeklyMenus = weeklyMenus; }
    public List<Recipe> getRecipes() { return recipes; }
    public void setRecipes(List<Recipe> recipes) { this.recipes = recipes; }
    
    public void addIngredient(Ingredient ingredient, short amount)
    {
        //TODO
    }

    public void removeIngredient(Ingredient ingredient, short amount)
    {
        //TODO
    }

    public void addMenu(WeeklyMenu menu)
    {
        //TODO
    }

    public void removeMenu(WeeklyMenu menu)
    {
        //TODO
    }

    public void addRecipe(Recipe recipe)
    {
        //TODO
    }

    public void removeRecipe(Recipe recipe)
    {
        //TODO
    }

    public void finalizeBuy()
    {
        //TODO
    }
}

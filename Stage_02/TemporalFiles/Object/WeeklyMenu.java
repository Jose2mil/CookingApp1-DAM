public class WeeklyMenu implements Comparable<WeeklyMenu>
{
    String name;
    Recipe[][] recipes;

    public WeeklyMenu(String name, Recipe[][] recipes)
    {
        this.name = name;
        this.recipes = recipes;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Recipe[][] getRecipes() { return recipes; }
    public void setRecipes(Recipe[][] recipes) { this.recipes = recipes; }

    @Override
    public String toString()
    {
        return "TODO toString WeeklyMenu";
    }

    @Override
    public int compareTo(WeeklyMenu weeklyMenu)
    {
        return 0;
    }

    public Recipe getRecipe(byte day, byte recipe)
    {
        return recipes[day][recipe];
    }

    public void setRecipe(byte day, byte recipe, Recipe newRecipe)
    {
        this.recipes[day][recipe] = newRecipe;
    }

    public void generateDailyMenu(byte day)
    {
        //TODO
    }

    public void showSummary()
    {
        //TODO
    }
}

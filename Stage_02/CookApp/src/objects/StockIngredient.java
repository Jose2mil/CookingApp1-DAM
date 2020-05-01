package objects;

public class StockIngredient
{
    Ingredient ingredient;
    short amount;

    public StockIngredient(Ingredient ingredient, short amount)
    {
        this.ingredient = ingredient;
        this.amount = amount;
    }

    public Ingredient getIngredient() { return ingredient; }

    public void setIngredient(Ingredient ingredient) { this.ingredient = ingredient; }

    public short getAmount() { return amount; }

    public void setAmount(short amount) { this.amount = amount; }


    @Override
    public String toString()
    {
        return ingredient.name + "  " + amount + " " + ingredient.unitOfMeasurement;
    }
}

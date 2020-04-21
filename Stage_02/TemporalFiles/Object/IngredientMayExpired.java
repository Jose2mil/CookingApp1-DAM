public class IngredientMayExpired extends Ingredient
{
    String expirationDate;

    public IngredientMayExpired(String name, short amount,
                                float kcal, float lipidFats,
                                float saturatedFats, float carbohydrates,
                                float sugars, float protein, float salt,
                                String expirationDate)
    {
        super(name, amount, kcal, lipidFats, saturatedFats, carbohydrates,
                sugars, protein, salt);
        this.expirationDate = expirationDate;
    }

    public String getExpirationDate() { return expirationDate; }
    public void setExpirationDate(String expirationDate) { this.expirationDate = expirationDate; }

    @Override
    public String toString()
    {
        return super.toString(); //TODO
    }

    public short getDaysToExpire()
    {
        return 0; //TODO
    }
}

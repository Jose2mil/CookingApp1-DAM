class Ingredient implements Comparable<Ingredient>
{
    String name;
    short amount;
    String unitOfMeasurement;
    float kcal;
    float lipidFats;
    float saturatedFats;
    float carbohydrates;
    float sugars;
    float protein;
    float salt;

    public Ingredient(String name, short amount, 
                      String unitOfMeasurement, float kcal, 
                      float lipidFats, float saturatedFats, 
                      float carbohydrates, float sugars, 
                      float protein, float salt) 
    {
        this.name = name;
        this.amount = amount;
        this.unitOfMeasurement = unitOfMeasurement;
        this.kcal = kcal;
        this.lipidFats = lipidFats;
        this.saturatedFats = saturatedFats;
        this.carbohydrates = carbohydrates;
        this.sugars = sugars;
        this.protein = protein;
        this.salt = salt;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public short getAmount() { return amount; }
    public void setAmount(short amount) { this.amount = amount; }
    public String getUnitOfMeasurement() { return unitOfMeasurement; }
    public void setUnitOfMeasurement(String unitOfMeasurement) { this.unitOfMeasurement = unitOfMeasurement; }
    public float getKcal() { return kcal; }
    public void setKcal(float kcal) { this.kcal = kcal; }
    public float getLipidFats() { return lipidFats; }
    public void setLipidFats(float lipidFats) { this.lipidFats = lipidFats; }
    public float getSaturatedFats() { return saturatedFats; }
    public void setSaturatedFats(float saturatedFats) { this.saturatedFats = saturatedFats; }
    public float getCarbohydrates() { return carbohydrates; }
    public void setCarbohydrates(float carbohydrates) { this.carbohydrates = carbohydrates; }
    public float getSugars() { return sugars; }
    public void setSugars(float sugars) { this.sugars = sugars; }
    public float getProtein() { return protein; }
    public void setProtein(float protein) { this.protein = protein; }
    public float getSalt() { return salt; }
    public void setSalt(float salt) { this.salt = salt; }

    @Override
    public String toString()
    {
        return "TODO toString Ingredient"; //TODO
    }

    @Override
    public int compareTo(Ingredient ingredient)
    {
        return 0; //TODO
    }

    public void modifyAmount(short amount)
    {
        //TODO
    }
}

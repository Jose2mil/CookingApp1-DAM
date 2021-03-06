package objects;

import objects.Ingredient;
import objects.StockIngredient;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Recipe implements Comparable<Recipe>
{
    String name;
    byte typeOfFood;
    ArrayList<String> instructions;
    ArrayList<StockIngredient> ingredients;
    ArrayList<String> gadgets;
    short timeNeeded;
    byte amountOfRations;
    HashSet<String> features;

    public Recipe(String name, byte typeOfFood, ArrayList<String> instructions,
                  ArrayList<StockIngredient> ingredients, ArrayList<String> gadgets,
                  short timeNeeded, byte amountOfRations,
                  HashSet<String> features)
    {
        this.name = name;
        this.typeOfFood = typeOfFood;
        this.instructions = instructions;
        this.ingredients = ingredients;
        this.gadgets = gadgets;
        this.timeNeeded = timeNeeded;
        this.amountOfRations = amountOfRations;
        this.features = features;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public byte getTypeOfFood() { return typeOfFood; }
    public void setTypeOfFood(byte typeOfFood) { this.typeOfFood = typeOfFood; }
    public ArrayList<String> getInstructions() { return instructions; }
    public void setInstructions(ArrayList<String> instructions) { this.instructions = instructions; }
    public ArrayList<StockIngredient> getIngredients() { return ingredients; }
    public void setIngredients(ArrayList<StockIngredient> ingredients) { this.ingredients = ingredients; }
    public ArrayList<String> getGadgets() { return gadgets; }
    public void setGadgets(ArrayList<String> gadgets) { this.gadgets = gadgets; }
    public short getTimeNeeded() { return timeNeeded; }
    public void setTimeNeeded(short timeNeeded) { this.timeNeeded = timeNeeded; }
    public byte getAmountOfRations() { return amountOfRations; }
    public void setAmountOfRations(byte amountOfRations) { this.amountOfRations = amountOfRations; }
    public HashSet<String> getFeatures() { return features; }
    public void setFeatures(HashSet<String> features) { this.features = features; }

    @Override
    public String toString()
    {
        return name;
    }

    @Override
    public int compareTo(Recipe recipe) {
        return name.compareTo(recipe.getName());
    }

    @Override
    public boolean equals(Object recipe)
    {
        if(recipe instanceof Recipe)
            return name.equals(((Recipe)recipe).name);

        return super.equals(recipe);
    }
}

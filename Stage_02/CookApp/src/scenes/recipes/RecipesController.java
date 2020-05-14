package scenes.recipes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import objects.Recipe;
import objects.StockIngredient;
import scenes.initial.InitialController;

import java.io.*;
import java.net.URL;
import java.util.*;

import static objects.IOUtils.*;

public class RecipesController implements Initializable
{
    public Button btnBack;
    public Button btnAdd;
    public Button btnEdit;
    public Button btnDelete;
    public Button btnView;
    public TextField txtSearch;
    public ListView<Recipe> lstRecipes;
    public CheckBox cbxVegetarian;
    public CheckBox cbxVegan;
    public CheckBox cbxNoLactose;
    public CheckBox cbxNoGluten;
    public RadioButton rbnOrderByNeededTime;
    public RadioButton rbtAlphabeticalOrder;
    ObservableList<Recipe> internalRecipeList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        internalRecipeList = FXCollections.observableArrayList();
        readRecipes();
        lstRecipes.setItems(internalRecipeList);
    }

    public void backToMainWindow(ActionEvent actionEvent) throws IOException
    {
        Parent home_page_parent = FXMLLoader.load(getClass().getResource("../initial/initial.fxml"));
        Scene home_page_scene = new Scene(home_page_parent);
        Stage app_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        app_stage.hide();
        app_stage.setScene(home_page_scene);
        app_stage.show();
    }

    private void readRecipes()
    {
        String path = "src/files/data/recipes.txt";
        try
        {
            BufferedReader file = new BufferedReader(new FileReader(path));
            String name, typeOfFood, instructions, ingredients,
                    gadgets, timeNeeded, amountOfRations, features;
            do
            {
                name = file.readLine();
                typeOfFood = file.readLine();
                instructions = file.readLine();
                ingredients = file.readLine();
                gadgets = file.readLine();
                timeNeeded = file.readLine();
                amountOfRations = file.readLine();
                features = file.readLine();

                if(name != null && name.trim() != "")
                {
                    ArrayList<String> listInstructions =
                            extractStringFromFormattedString(instructions);
                    ArrayList<StockIngredient> listIngredients =
                            extractIngredientsFromString(ingredients);
                    ArrayList<String> listGadgets =
                            extractStringFromFormattedString(gadgets);
                    HashSet<String> hashSetFeatures = extractFeaturesFromString(features);

                    internalRecipeList.add(
                            new Recipe(
                                    name,
                                    Byte.parseByte(typeOfFood),
                                    listInstructions,
                                    listIngredients,
                                    listGadgets,
                                    Short.parseShort(timeNeeded),
                                    Byte.parseByte(amountOfRations),
                                    hashSetFeatures));
                }
            }
            while(name != null && name != "");
            file.close();
        }
        catch (IOException e)
        {
            internalRecipeList.clear();
            showAlert("Error",e.getMessage(),"");
        }

        sortRecipes();
        lstRecipes.setItems(internalRecipeList);
    }

    private ArrayList<String> extractStringFromFormattedString(String formattedString)
    {
        ArrayList<String> listString = new ArrayList<>();
        if(! formattedString.isEmpty())
        {
            String[] arrayInstructions = formattedString.split(";");
            for (String instruction : arrayInstructions)
                listString.add(instruction);
        }

        return listString;
    }

    private ArrayList<StockIngredient> extractIngredientsFromString(String ingredients)
    {
        ArrayList<StockIngredient> listIngredients = new ArrayList<>();
        if(! ingredients.isEmpty())
        {
            String[] arrayIngredients = ingredients.split(";");
            for (String line : arrayIngredients)
                listIngredients.add(
                        new StockIngredient(
                                InitialController.ingredients.get(line.split(":")[0]),
                                Short.parseShort(line.split(":")[1])));
        }

        return listIngredients;
    }

    private HashSet<String> extractFeaturesFromString(String features)
    {
        HashSet<String> hashSetFeatures = new HashSet<>();
        if(! features.isEmpty())
        {
            String[] arrayFeatures = features.split(";");
            for (String feature : arrayFeatures)
                hashSetFeatures.add(feature);
        }

        return hashSetFeatures;
    }

    public void updateAndSaveRecipes()
    {
        sortRecipes();
        PrintWriter recipesFile = null;

        try
        {
            recipesFile = new PrintWriter("src/files/data/recipes.txt");
            for(int i = 0; i < internalRecipeList.size(); i++)
            {
                recipesFile.println(internalRecipeList.get(i).getName());
                recipesFile.println(internalRecipeList.get(i).getTypeOfFood());
                printStringListIntoFile(recipesFile,
                        internalRecipeList.get(i).getInstructions());
                printIngredientsIntoFile(recipesFile,
                        internalRecipeList.get(i).getIngredients());
                printStringListIntoFile(recipesFile,
                        internalRecipeList.get(i).getGadgets());
                recipesFile.println(internalRecipeList.get(i).getTimeNeeded());
                recipesFile.println(internalRecipeList.get(i).getAmountOfRations());
                printFeaturesIntoFile(recipesFile,
                        internalRecipeList.get(i).getFeatures());
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            if(recipesFile != null)
                recipesFile.close();
        }

        lstRecipes.setItems(internalRecipeList);
    }

    public void sortRecipes()
    {
        if(rbtAlphabeticalOrder.isSelected())
            Collections.sort(internalRecipeList);
        else if(rbnOrderByNeededTime.isSelected())
            Collections.sort(internalRecipeList, new Comparator<Recipe>() {
                @Override
                public int compare(Recipe recipe01, Recipe recipe02) {
                    return Integer.compare(recipe01.getTimeNeeded(),recipe02.getTimeNeeded());
                }
            });
    }

    private void printStringListIntoFile(PrintWriter recipesFile, ArrayList<String> instructions)
    {
        for(int j = 0; j < instructions.size(); j++)
        {
            recipesFile.print(instructions.get(j));
            if(j != instructions.size() - 1)
                recipesFile.print(";");
        }
        recipesFile.println("");
    }

    private void printIngredientsIntoFile(PrintWriter recipesFile, ArrayList<StockIngredient> ingredients)
    {
        for(int j = 0; j < ingredients.size(); j++)
        {
            recipesFile.print(ingredients.get(j).getIngredient().getName() +
                    ":" + ingredients.get(j).getAmount());
            if(j != ingredients.size() - 1)
                recipesFile.print(";");
        }
        recipesFile.println("");
    }

    private void printFeaturesIntoFile(PrintWriter recipesFile, HashSet<String> features)
    {
        boolean isTheFirst = true;
        for(String feature : features)
        {
            if(! isTheFirst)
                recipesFile.print(";");
            else
                isTheFirst = false;

            recipesFile.print(feature);
        }
        recipesFile.println("");
    }

    public void searchRecipe(KeyEvent keyEvent)
    {
        String subString = (txtSearch.getText() + keyEvent.getText()).toUpperCase();
        lstRecipes.setItems(getSearchRecipes(subString));
    }

    public void applyFilters()
    {
        String subString = txtSearch.getText().toUpperCase();
        sortRecipes();
        lstRecipes.setItems(getSearchRecipes(subString));
    }

    private ObservableList<Recipe> getSearchRecipes(String subString)
    {
        ArrayList<String> filterFeatures = extractActiveFilters();

        ObservableList<Recipe> listRecipesCopy = FXCollections.observableArrayList();
        boolean meetsTheFeatures;
        for(int i = 0; i < internalRecipeList.size(); i++)
        {
            meetsTheFeatures = false;
            Recipe recipe = internalRecipeList.get(i);
            if (recipe.getName().toUpperCase().contains(subString))
                meetsTheFeatures = true;

            if (meetsTheFeatures)
            {
                for (int j = 0; j < filterFeatures.size(); j++)
                    if (recipe.getFeatures().add(filterFeatures.get(j)))
                    {
                        meetsTheFeatures = false;
                        recipe.getFeatures().remove(filterFeatures.get(j));
                    }
            }

            if(meetsTheFeatures)
                listRecipesCopy.add(internalRecipeList.get(i));
        }

        return listRecipesCopy;
    }

    private ArrayList<String> extractActiveFilters()
    {
        ArrayList<String> filterFeatures = new ArrayList<>();
        if(cbxVegetarian.isSelected())
            filterFeatures.add(cbxVegetarian.getText());
        if(cbxVegan.isSelected())
            filterFeatures.add(cbxVegan.getText());
        if(cbxNoLactose.isSelected())
            filterFeatures.add(cbxNoLactose.getText());
        if(cbxNoGluten.isSelected())
            filterFeatures.add(cbxNoGluten.getText());

        return filterFeatures;
    }

    public void addRecipe(ActionEvent actionEvent)
    {
        AddDialogRecipe addDialogRecipes = new AddDialogRecipe();
        Recipe newRecipe = addDialogRecipes.showAndReturnNewRecipe();
        if(newRecipe != null)
        {
            if(internalRecipeList.contains(newRecipe))
                showAlert("Error",
                        "Repeated recipe",
                        "The new recipe is considered to be " +
                                "already on the list because its name " +
                                "matches an existing one");
            else
            {
                internalRecipeList.add(newRecipe);
                updateAndSaveRecipes();
            }
        }
    }

    public void deleteRecipe(ActionEvent actionEvent)
    {
        Recipe stockIngredientSelected = lstRecipes.getSelectionModel().getSelectedItem();

        if(stockIngredientSelected != null)
        {
            internalRecipeList.remove(stockIngredientSelected);
            updateAndSaveRecipes();
            applyFilters();
        }

        else
            showAlert("Error",
                    "Error removing recipe",
                    "No recipe selected");
    }

    public void editRecipe(ActionEvent actionEvent)
    {
        Recipe recipeSelected = lstRecipes.getSelectionModel().getSelectedItem();
        if(recipeSelected != null)
        {
            Recipe editedRecipe = new EditDialogRecipe(recipeSelected).showAndReturnRecipe();
            if(editedRecipe != null)
            {
                internalRecipeList.remove(recipeSelected);
                internalRecipeList.add(editedRecipe);
                updateAndSaveRecipes();
            }
        }
        else
            showAlert("Error",
                    "Error editing recipe",
                    "No recipe selected");
    }

    public void viewRecipe(ActionEvent actionEvent)
    {
        Recipe recipeSelected = lstRecipes.getSelectionModel().getSelectedItem();
        if(recipeSelected != null)
        {
            new ViewDialogRecipe(recipeSelected).showRecipe();
        }
        else
            showAlert("Error",
                    "Error viewing recipe",
                    "No recipe selected");
    }
}

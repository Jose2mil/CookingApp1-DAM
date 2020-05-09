package scenes.recipes;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

public class RecipesController implements Initializable
{
    public Button btnBack;
    public Button btnAdd;
    public Button btnSearch;
    public Button btnEdit;
    public Button btnDelete;
    public Button btnView;
    public TextField txtSearch;
    public ListView<Recipe> lstRecipes;
    public CheckBox cbxVegetarian;
    public CheckBox cbxVegan;
    public CheckBox cbxNoLactose;
    public CheckBox cbxNoGluten;
    ObservableList<Recipe> listRecipes;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        listRecipes = FXCollections.observableArrayList();
        readRecipes();
        saveAndUpdateRecipes();
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
        BufferedReader file = null;
        try
        {
            file = new BufferedReader(new FileReader(path));
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

                if(features != null)
                {
                    String[] arrayInstructions = instructions.split(";");
                    ArrayList<String> listInstructions = new ArrayList<>();
                    for(String instruction : arrayInstructions)
                        listInstructions.add(instruction);

                    String[] arrayIngredients = ingredients.split(";");
                    ArrayList<StockIngredient> listIngredients = new ArrayList<>();
                    for(String line : arrayIngredients)
                        listIngredients.add(
                                new StockIngredient(
                                        InitialController.ingredients.get(line.split(":")[0]),
                                        Short.parseShort(line.split(":")[1])));

                    String[] arrayGadgets = gadgets.split(";");
                    ArrayList<String> listGadgets = new ArrayList<>();
                    for(String gadget : arrayGadgets)
                        listGadgets.add(gadget);

                    String[] arrayFeatures = features.split(";");
                    HashSet<String> hashSetFeatures = new HashSet<>();
                    for(String feature : arrayFeatures)
                        hashSetFeatures.add(feature);

                    listRecipes.add(
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
            while(features != null);
            file.close();
        }
        catch (Exception e)
        {
            listRecipes.clear();
        }

        Collections.sort(listRecipes);
        lstRecipes.setItems(listRecipes);
    }

    public void saveAndUpdateRecipes()
    {
        Collections.sort(listRecipes);
        PrintWriter recipesFile = null;

        try
        {
            recipesFile = new PrintWriter("src/files/data/recipes.txt");
            for(int i = 0; i < listRecipes.size(); i++)
            {
                recipesFile.println(listRecipes.get(i).getName());
                recipesFile.println(listRecipes.get(i).getTypeOfFood());

                ArrayList<String> instructions = listRecipes.get(i).getInstructions();
                for(int j = 0; j < instructions.size(); j++)
                {
                    recipesFile.print(instructions.get(j));
                    if(j != instructions.size() - 1)
                        recipesFile.print(";");
                }
                recipesFile.println("");

                ArrayList<StockIngredient> ingredients = listRecipes.get(i).getIngredients();
                for(int j = 0; j < ingredients.size(); j++)
                {
                    recipesFile.print(ingredients.get(j).getIngredient().getName() +
                            ":" + ingredients.get(j).getAmount());
                    if(j != instructions.size() - 1)
                        recipesFile.print(";");
                }
                recipesFile.println("");

                ArrayList<String> gadgets = listRecipes.get(i).getGadgets();
                for(int j = 0; j < gadgets.size(); j++)
                {
                    recipesFile.print(gadgets.get(j));
                    if(j != gadgets.size() - 1)
                        recipesFile.print(";");
                }
                recipesFile.println("");

                recipesFile.println(listRecipes.get(i).getTimeNeeded());
                recipesFile.println(listRecipes.get(i).getAmountOfRations());

                HashSet<String> features = listRecipes.get(i).getFeatures();
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
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            if(recipesFile != null)
                recipesFile.close();
        }

        lstRecipes.setItems(listRecipes);
    }

    public void deleteRecipe(ActionEvent actionEvent)
    {
        Recipe stockIngredientSelected = lstRecipes.getSelectionModel().getSelectedItem();

        if(stockIngredientSelected != null)
        {
            listRecipes.remove(stockIngredientSelected);
            saveAndUpdateRecipes();
        }

        else
            showAlert("Error",
                    "Error removing recipe",
                    "No recipe selected");
    }

    public void searchRecipe(KeyEvent keyEvent)
    {
        String subString = (txtSearch.getText() + keyEvent.getText()).toUpperCase();
        lstRecipes.setItems(getSearchRecipes(subString));
    }

    public void applyFilters()
    {
        String subString = txtSearch.getText().toUpperCase();
        lstRecipes.setItems(getSearchRecipes(subString));
    }

    private ObservableList<Recipe> getSearchRecipes(String subString)
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

        ObservableList<Recipe> listRecipesCopy = FXCollections.observableArrayList();
        boolean meetsTheFeatures;
        for(int i = 0; i <listRecipes.size(); i++)
        {
            meetsTheFeatures = false;
            Recipe recipe = listRecipes.get(i);
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
                listRecipesCopy.add(listRecipes.get(i));
        }

        return listRecipesCopy;
    }

    private void showAlert(String title, String header, String context)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(context);
        alert.showAndWait();
    }
}

package scenes.recipes;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import objects.Ingredient;
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
    ObservableList<String> listGadgets;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        listRecipes = FXCollections.observableArrayList();
        readRecipes();
        lstRecipes.setItems(listRecipes);
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
                    ArrayList<String> listInstructions = new ArrayList<>();
                    if(! instructions.isEmpty())
                    {
                        String[] arrayInstructions = instructions.split(";");
                        for (String instruction : arrayInstructions)
                            listInstructions.add(instruction);
                    }

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

                    ArrayList<String> listGadgets = new ArrayList<>();
                    if(! gadgets.isEmpty())
                    {
                        String[] arrayGadgets = gadgets.split(";");
                        for (String gadget : arrayGadgets)
                            listGadgets.add(gadget);
                    }

                    HashSet<String> hashSetFeatures = new HashSet<>();
                    if(! features.isEmpty())
                    {
                        String[] arrayFeatures = features.split(";");
                        for (String feature : arrayFeatures)
                            hashSetFeatures.add(feature);
                    }

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
            while(name != null && name != "");
            file.close();
        }
        catch (IOException e)
        {
            listRecipes.clear();
            showAlert("Error",e.getMessage(),"");
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
                    if(j != ingredients.size() - 1)
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

    public void addRecipe(ActionEvent actionEvent)
    {

        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Recipes");
        dialog.setHeaderText("New recipe");
        dialog.setResizable(true);

        //Create controls
        Label lbName = new Label("Name: ");
        Label lbRations = new Label("Rations: ");
        Label lbType = new Label("Type: ");
        Label lbTime = new Label("Time (min): ");
        Label lbInstructions = new Label("Instructions: ");
        Label lbFeatures = new Label("Type of food ");

        TextField tName = new TextField();
        TextField tRations = new TextField();
        TextField tAmount   = new TextField();
        TextField tGadget  = new TextField();
        TextField tTime = new TextField();
        tName.setPromptText("Enter name");
        tRations.setPromptText("Enter rations");
        tTime.setPromptText("Elaboration time");
        tAmount.setPromptText("Amount");
        tGadget.setPromptText("Enter gadget");
        TextArea txtAreaInstructions = new TextArea();

        ChoiceBox<String> typeChoices = new ChoiceBox<>();
        typeChoices.getItems().add("Breakfast");
        typeChoices.getItems().add("Lunch");
        typeChoices.getItems().add("Dinner");
        typeChoices.getSelectionModel().select(1);

        ListView<Ingredient> ingredientsList = new ListView();
        fillListIngredientsWithIngredients(ingredientsList.getItems());
        ListView<StockIngredient> ingredientsSelected = new ListView();
        ListView<String> gadgetsList = new ListView();
        TextArea txInstructions = new TextArea();
        Button btnAddIngredient = new Button();
        btnAddIngredient.setText("Add ingredient");
        Button btnDeleteIngredient = new Button();
        btnDeleteIngredient.setText("Delete ingredient");
        Button btnAddGadget = new Button();
        btnAddGadget.setText("Add gadget");
        Button btnDeleteGadget = new Button();
        btnDeleteGadget.setText(("Delete gadget"));
        CheckBox cbVegetarian = new CheckBox();
        cbVegetarian.setText("Vegetarian");
        CheckBox cbVegan = new CheckBox();
        cbVegan.setText("Vegan");
        CheckBox cbLactose = new CheckBox();
        cbLactose.setText("No lactose");
        CheckBox cbGluten = new CheckBox();
        cbGluten.setText("No gluten");

        // force the field to be numeric only
        forceFieldToBbeNumeric(tAmount);
        forceFieldToBbeNumeric(tRations);
        forceFieldToBbeNumeric(tTime);

        //Add button events
        addEventToBtnAddIngredient(btnAddIngredient, ingredientsList,
                ingredientsSelected, tAmount);
        addEventToBtnDeleteIngredient(btnDeleteIngredient,ingredientsList,
                ingredientsSelected);
        addEventToBtnAddGadget(btnAddGadget, tGadget, gadgetsList);
        addEventToBtnDeleteGadget(btnDeleteGadget, gadgetsList);

        //Adding elements to the grid
        GridPane grid = new GridPane();
        addingElementsAndStyleToTheGrid(dialog, lbName, lbRations, lbType, lbTime,
                lbInstructions, lbFeatures, tName, tRations, tAmount, tGadget, tTime,
                txtAreaInstructions, typeChoices, ingredientsList, ingredientsSelected,
                gadgetsList, txInstructions, btnAddIngredient, btnDeleteIngredient,
                btnAddGadget, btnDeleteGadget, cbVegetarian, cbVegan, cbLactose, cbGluten, grid);

        ButtonType buttonAdd = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonAdd);

        Optional<String> result = dialog.showAndWait();

        //Collect data to add new recipe
        if(! result.isEmpty())
            collectDataAndAddNewRecipe(tName, typeChoices,
                    txInstructions, ingredientsSelected,
                    gadgetsList, tTime, tRations, cbVegetarian,
                    cbVegan, cbLactose, cbGluten);
    }

    private void addingElementsAndStyleToTheGrid(Dialog<String> dialog, Label lbName,
                                                 Label lbRations, Label lbType, Label lbTime,
                                                 Label lbInstructions, Label lbFeatures, TextField tName,
                                                 TextField tRations, TextField tAmount, TextField tGadget,
                                                 TextField tTime, TextArea txtAreaInstructions,
                                                 ChoiceBox<String> typeChoices,
                                                 ListView<Ingredient> ingredientsList,
                                                 ListView<StockIngredient> ingredientsSelected,
                                                 ListView<String> gadgetsList, TextArea txInstructions,
                                                 Button btnAddIngredient, Button btnDeleteIngredient,
                                                 Button btnAddGadget, Button btnDeleteGadget,
                                                 CheckBox cbVegetarian, CheckBox cbVegan, CheckBox cbLactose,
                                                 CheckBox cbGluten, GridPane grid)
    {
        grid.add(lbName, 1, 1);
        grid.add(tName, 2, 1);
        grid.add(lbRations, 3, 1);
        grid.add(tRations, 4, 1);
        grid.add(lbType, 1, 2);
        grid.add(typeChoices, 2, 2);
        grid.add(lbTime, 3, 2);
        grid.add(tTime, 4, 2);
        grid.add(btnAddIngredient,1,4);
        grid.add(tAmount,2,4);
        grid.add(btnDeleteIngredient,3,4);
        grid.add(ingredientsList, 1, 5,2,10);
        grid.add(ingredientsSelected, 3, 5,2,10);
        grid.add(lbInstructions,1,15);
        grid.add(txtAreaInstructions,1,16,4,4);
        grid.add(btnAddGadget,1,20);
        grid.add(tGadget,2,20);
        grid.add(btnDeleteGadget,3,20);
        grid.add(gadgetsList,1,21,4,2);
        grid.add(lbFeatures,1,24);
        grid.add(cbVegetarian,1,25);
        grid.add(cbVegan,2,25);
        grid.add(cbLactose,3,25);
        grid.add(cbGluten,4,25);

        //Style
        lbName.setPrefWidth(50);
        ingredientsList.setPrefHeight(180);
        ingredientsSelected.setPrefHeight(180);
        dialog.setHeight(450);
        tAmount.setMaxWidth(60);
        typeChoices.setMaxWidth(120);
        tName.setMaxWidth(120);
        tRations.setMaxWidth(120);
        tTime.setMaxWidth(120);
        tGadget.setMaxWidth(120);
        btnAddIngredient.setMinWidth(115);
        btnDeleteIngredient.setMinWidth(115);
        btnAddGadget.setMinWidth(115);
        btnDeleteGadget.setMinWidth(115);
        txInstructions.setPrefColumnCount(20);
        gadgetsList.setPrefHeight(100);
        grid.setPadding(new Insets(10,10,10,10));
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setStyle("-fx-background-color:#AFAADC;");


        ScrollPane sp = new ScrollPane();
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background-color:#AFAADC;");
        sp.setContent(grid);
        dialog.getDialogPane().setContent(sp);
    }

    private void addEventToBtnAddIngredient(Button btnAddIngredient, ListView<Ingredient> ingredientsList,
                                    ListView<StockIngredient> ingredientsSelected, TextField tAmount)
    {
        btnAddIngredient.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                Ingredient myIngredient = ingredientsList.getSelectionModel().getSelectedItem();
                if(myIngredient != null && isIntegerGreaterThanZero(tAmount.getText())) {
                    ingredientsSelected.getItems().add(
                            new StockIngredient(myIngredient,Short.parseShort(tAmount.getText())));
                    FXCollections.sort(ingredientsSelected.getItems());
                    ingredientsList.getItems().remove(myIngredient);
                    tAmount.clear();
                }
                else
                {
                    showAlert("Error",
                            "Ingredient and amount required",
                            "Select ingredient and add amount (greater than zero)");
                }
            }
        });
    }

    private void addEventToBtnDeleteIngredient(Button btnDeleteIngredient, ListView<Ingredient> ingredientsList,
                                            ListView<StockIngredient> ingredientsSelected)
    {
        //Event for DeleteIngredient button
        btnDeleteIngredient.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                StockIngredient ingredientToDelete = ingredientsSelected.getSelectionModel().getSelectedItem();
                if(ingredientToDelete != null) {
                    ingredientsList.getItems().add(ingredientToDelete.getIngredient());
                    FXCollections.sort(ingredientsList.getItems());
                    ingredientsSelected.getItems().remove(ingredientToDelete);
                }
                else
                {
                    showAlert("Error",
                            "Ingredient required",
                            "Select an ingredient to delete it");
                }
            }
        });
    }

    private void addEventToBtnAddGadget(Button btnAddGadget, TextField tGadget,
                                        ListView<String> gadgetsList)
    {
        btnAddGadget.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                String myGadget = tGadget.getText();
                if(!myGadget.isEmpty()) {
                    gadgetsList.getItems().add(myGadget);
                    tGadget.clear();
                }
                else
                {
                    showAlert("Error",
                            "Gadget is required",
                            "Add a new gadget");
                }
            }
        });
    }

    private void addEventToBtnDeleteGadget(Button btnDeleteGadget,
                                        ListView<String> gadgetsList)
    {
        btnDeleteGadget.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                String myGadget = gadgetsList.getSelectionModel().getSelectedItem();
                if(myGadget != null) {
                    gadgetsList.getItems().remove(myGadget);
                }
                else
                {
                    showAlert("Error",
                            "Gadget is required",
                            "Select a gadget");
                }
            }
        });
    }

    private void fillListIngredientsWithIngredients(ObservableList<Ingredient> listIng)
    {

        //ObservableList<String> listIng = FXCollections.observableArrayList();
        for(Ingredient keyValues : InitialController.ingredients.values())
            listIng.add(keyValues);

        FXCollections.sort(listIng);
    }

    private void forceFieldToBbeNumeric(TextField textField)
    {
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    textField.setText(newValue.replaceAll("[^\\d]|", ""));
                }
            }
        });
    }

    private void collectDataAndAddNewRecipe(TextField tName, ChoiceBox typeChoices,
                                            TextArea txInstructions, ListView<StockIngredient> ingredientsSelected,
                                            ListView<String> gadgetsList, TextField tTime,
                                            TextField tRations, CheckBox cbVegetarian,
                                            CheckBox cbVegan, CheckBox cbLactose,
                                            CheckBox cbGluten)
    {
        if(tName.getText().isEmpty())
            showAlert("Error","Error adding recipe","The name can't be empty");

        else
        {
            //Recipe name
            String myRecipeName = "";
            myRecipeName = tName.getText();
            //Type of food
            byte myTypeOfFood = (byte) typeChoices.getSelectionModel().getSelectedIndex();
            //Instructions
            ArrayList<String> myInstructions = new ArrayList<>();
            String[] arrayInstructions = txInstructions.getText().split(System.lineSeparator());
            for (String line : arrayInstructions)
                myInstructions.add(line);
            //Ingredients
            ArrayList<StockIngredient> myIngredients = (ArrayList<StockIngredient>)
                    transformObservableListIntoList(ingredientsSelected.getItems());
            //Gadgets
            ArrayList<String> myGadgets = (ArrayList<String>)
                            transformObservableListIntoList(gadgetsList.getItems());
            //Time needed
            Short myTime = (short) returnValueOfNumericTextField(tTime);
            //Rations
            byte myRations = (byte) returnValueOfNumericTextField(tRations);
            //Features
            HashSet<String> myFeatures = new HashSet<String>();
            fillFeaturesSet(cbVegetarian, myFeatures);
            fillFeaturesSet(cbVegan, myFeatures);
            fillFeaturesSet(cbLactose, myFeatures);
            fillFeaturesSet(cbGluten, myFeatures);

            listRecipes.add(
                    new Recipe(
                            myRecipeName,
                            myTypeOfFood,
                            myInstructions,
                            myIngredients,
                            myGadgets,
                            myTime,
                            myRations,
                            myFeatures));
            saveAndUpdateRecipes();
        }
    }

    private List transformObservableListIntoList(ObservableList observableList)
    {
        ArrayList list = new ArrayList<>();
        for (Object gadget : observableList)
            list.add(gadget);

        return list;
    }

    private int returnValueOfNumericTextField(TextField textField)
    {
        if(textField.getText().isEmpty())
            return 0;
        return Integer.parseInt(textField.getText());
    }

    private void fillFeaturesSet(CheckBox checkBox, HashSet<String> myFeatures)
    {
        if (checkBox.isSelected())
            myFeatures.add(checkBox.getText());
    }

    public static boolean isIntegerGreaterThanZero(String strNum) {
        int num;
        if (strNum == null) {
            return false;
        }
        try {
            num = Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        if(num <= 0 )
            return false;
        else
            return true;
    }

}

package scenes.recipes;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import objects.Ingredient;
import objects.Recipe;
import objects.StockIngredient;
import scenes.initial.InitialController;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class DialogToShowRecipes
{
    protected javafx.scene.control.Dialog<String> dialog;
    protected Label lbName;
    protected Label lbRations;
    protected Label lbType;
    protected Label lbTime;
    protected Label lbInstructions;
    protected Label lbFeatures;
    protected Label lbIngredients;
    protected Label lbGadgets;
    protected TextField tName;
    protected TextField tType;
    protected TextField tRations;
    protected TextField tAmount;
    protected TextField tGadget;
    protected TextField tTime;
    protected TextArea txtAreaInstructions;
    protected ChoiceBox<String> typeChoices;
    protected ListView<Ingredient> ingredientsList;
    protected ListView<StockIngredient> ingredientsSelected;
    protected ListView<String> gadgetsList;
    protected TextArea txInstructions;
    protected Button btnAddIngredient;
    protected Button btnDeleteIngredient;
    protected Button btnAddGadget;
    protected Button btnDeleteGadget;
    protected CheckBox cbVegetarian;
    protected CheckBox cbVegan;
    protected CheckBox cbLactose;
    protected CheckBox cbGluten;
    protected GridPane grid;

    public DialogToShowRecipes()
    {
        dialog = new javafx.scene.control.Dialog<>();
        dialog.setTitle("Recipes");
        dialog.setResizable(true);

        initializeControls();
        prepareContent();
    }

    protected void initializeControls()
    {
        lbName = new Label("Name: ");
        lbRations = new Label("Rations: ");
        lbType = new Label("Type: ");
        lbTime = new Label("Time (min): ");
        lbInstructions = new Label("Instructions");
        lbFeatures = new Label("Type of food ");
        lbIngredients = new Label("Ingredients ");
        lbGadgets = new Label("Gadgets ");

        tName = new TextField();
        tType = new TextField();
        tRations = new TextField();
        tAmount   = new TextField();
        tGadget  = new TextField();
        tTime = new TextField();
        txtAreaInstructions = new TextArea();
        typeChoices = new ChoiceBox<>();

        ingredientsList = new ListView();
        ingredientsSelected = new ListView();
        gadgetsList = new ListView();
        txInstructions = new TextArea();
        tType.setText(typeChoices.getSelectionModel().getSelectedItem());
        btnAddIngredient = new Button("Add ingredient");
        btnDeleteIngredient = new Button("Delete ingredient");
        btnAddGadget = new Button("Add gadget");
        btnDeleteGadget = new Button("Delete gadget");
        cbVegetarian = new CheckBox("Vegetarian");
        cbVegan = new CheckBox("Vegan");
        cbLactose = new CheckBox("No Lactose");
        cbGluten = new CheckBox("No Gluten");
        grid = new GridPane();
    }

    protected void prepareContent()
    {
        setControlProperties();

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

        addingElementsAndStyleToTheGrid();
    }

    protected void setControlProperties()
    {
        tName.setPromptText("Enter name");
        tRations.setPromptText("Enter rations");
        tTime.setPromptText("Elaboration time");
        tAmount.setPromptText("Amount");
        tGadget.setPromptText("Enter gadget");

        typeChoices.getItems().add("Breakfast");
        typeChoices.getItems().add("Lunch");
        typeChoices.getItems().add("Dinner");
        typeChoices.getSelectionModel().select(1);

        fillListIngredientsWithIngredients(ingredientsList.getItems());
    }

    protected void addingElementsAndStyleToTheGrid()
    {
        positionControlsOnGridPane();
        styleGridPaneControls();

        ScrollPane sp = new ScrollPane();
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background-color:#AFAADC;");
        sp.setContent(grid);
        dialog.getDialogPane().setContent(sp);

        ButtonType buttonAdd = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonAdd);
    }

    protected void positionControlsOnGridPane()
    {
        grid.add(lbName, 1, 1);
        grid.add(tName, 2, 1);
        grid.add(lbRations, 3, 1);
        grid.add(tRations, 4, 1);
        grid.add(lbType, 1, 2);
        grid.add(tType, 2, 2);
        grid.add(lbTime, 3, 2);
        grid.add(tTime, 4, 2);
        grid.add(lbIngredients,1,4);
        //grid.add(tAmount,2,4);
        //grid.add(btnDeleteIngredient,3,4);
        grid.add(ingredientsSelected, 1, 5,2,5);
       //grid.add(ingredientsSelected, 3, 5,2,10);
        grid.add(lbInstructions,1,10);
        grid.add(txtAreaInstructions,1,11,4,3);
        grid.add(lbGadgets,1,14);
       // grid.add(tGadget,2,20);
       // grid.add(btnDeleteGadget,3,20);
        grid.add(gadgetsList,1,15,4,2);
        grid.add(lbFeatures,1,18);
        grid.add(cbVegetarian,1,19);
        grid.add(cbVegan,2,19);
        grid.add(cbLactose,3,19);
        grid.add(cbGluten,4,19);
    }

    protected void styleGridPaneControls()
    {
        lbName.setPrefWidth(50);
        ingredientsList.setPrefHeight(180);
        ingredientsSelected.setPrefHeight(90);
        dialog.setHeight(450);
        tAmount.setMaxWidth(60);
        typeChoices.setMaxWidth(120);
        tName.setMaxWidth(120);
        tName.setStyle("-fx-background-color:#D6D0F0;");
        tName.setAlignment(Pos.CENTER);
        tType.setMaxWidth(120);
        tType.setStyle("-fx-background-color:#D6D0F0;");
        tType.setAlignment(Pos.CENTER);
        tRations.setMaxWidth(120);
        tRations.setStyle("-fx-background-color:#D6D0F0;");
        tRations.setAlignment(Pos.CENTER);
        tTime.setMaxWidth(120);
        tTime.setStyle("-fx-background-color:#D6D0F0;");
        tTime.setAlignment(Pos.CENTER);
        tGadget.setMaxWidth(120);
        btnAddIngredient.setMinWidth(115);
        btnDeleteIngredient.setMinWidth(115);
        btnAddGadget.setMinWidth(115);
        btnDeleteGadget.setMinWidth(115);
        txInstructions.setPrefColumnCount(20);
        txtAreaInstructions.setMaxHeight(120);
        gadgetsList.setPrefHeight(100);
        grid.setPadding(new Insets(10,10,10,10));
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setStyle("-fx-background-color:#AFAADC;");
    }

    protected void addEventToBtnAddIngredient(Button btnAddIngredient, ListView<Ingredient> ingredientsList,
                                              ListView<StockIngredient> ingredientsSelected, TextField tAmount)
    {
        btnAddIngredient.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                Ingredient myIngredient = ingredientsList.getSelectionModel().getSelectedItem();
                if(myIngredient != null && returnValueOfNumericTextField(tAmount) > 0) {
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

    protected void addEventToBtnDeleteIngredient(Button btnDeleteIngredient, ListView<Ingredient> ingredientsList,
                                                 ListView<StockIngredient> ingredientsSelected)
    {
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

    protected void addEventToBtnAddGadget(Button btnAddGadget, TextField tGadget,
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

    protected void addEventToBtnDeleteGadget(Button btnDeleteGadget,
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

    protected void fillListIngredientsWithIngredients(ObservableList<Ingredient> listIng)
    {
        for(Ingredient keyValues : InitialController.ingredients.values())
            listIng.add(keyValues);

        FXCollections.sort(listIng);
    }

    protected void forceFieldToBbeNumeric(TextField textField)
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

    protected int returnValueOfNumericTextField(TextField textField)
    {
        if(textField.getText().isEmpty())
            return 0;
        return Integer.parseInt(textField.getText());
    }

    protected Recipe collectDataAndReturnRecipe()
    {
        //Recipe name
        String myRecipeName = "";
        myRecipeName = tName.getText();

        //Type of food
        byte myTypeOfFood = (byte) typeChoices.getSelectionModel().getSelectedIndex();

        //Instructions
        ArrayList<String> myInstructions = new ArrayList<>();
        String[] arrayInstructions = txtAreaInstructions.getText().split("\n");
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

        return new Recipe(
                myRecipeName,
                myTypeOfFood,
                myInstructions,
                myIngredients,
                myGadgets,
                myTime,
                myRations,
                myFeatures);
    }

    protected List transformObservableListIntoList(ObservableList observableList)
    {
        ArrayList list = new ArrayList<>();
        for (Object gadget : observableList)
            list.add(gadget);

        return list;
    }

    protected void fillFeaturesSet(CheckBox checkBox, HashSet<String> myFeatures)
    {
        if (checkBox.isSelected())
            myFeatures.add(checkBox.getText());
    }

    protected void showAlert(String title, String header, String context)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(context);
        alert.showAndWait();
    }
}

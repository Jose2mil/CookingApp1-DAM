package scenes.recipes;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import objects.Ingredient;
import objects.Recipe;
import objects.StockIngredient;
import scenes.initial.InitialController;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static objects.IOUtils.*;

public abstract class IODialogRecipes
{
    protected javafx.scene.control.Dialog<String> dialog;
    protected Label lblName;
    protected Label lblNumberOfRations;
    protected Label lblTypeOfFood;
    protected Label lblNeededTime;
    protected Label lblInstructions;
    protected Label lblFeatures;
    protected TextField txtName;
    protected TextField txtRations;
    protected TextField txtAmountOfNewIngredient;
    protected TextField txtGadget;
    protected TextField txtNeededTime;
    protected TextArea txaInstructions;
    protected ChoiceBox<String> chbTypeOfFood;
    protected ListView<Ingredient> lstAvailableIngredients;
    protected ListView<StockIngredient> lstSelectedIngredients;
    protected ListView<String> lstGadgets;
    protected Button btnAddIngredient;
    protected Button btnDeleteIngredient;
    protected Button btnAddGadget;
    protected Button btnDeleteGadget;
    protected CheckBox cbxVegetarian;
    protected CheckBox cbxVegan;
    protected CheckBox cbxNoLactose;
    protected CheckBox cbxNoGluten;
    protected GridPane gridPane;

    public IODialogRecipes()
    {
        dialog = new javafx.scene.control.Dialog<>();
        dialog.setTitle("Recipes");
        dialog.setResizable(true);

        initializeControls();
        prepareControlEvents();
    }

    protected void initializeControls()
    {
        lblName = new Label("Name: ");
        lblNumberOfRations = new Label("Rations: ");
        lblTypeOfFood = new Label("Type: ");
        lblNeededTime = new Label("Time (min): ");
        lblInstructions = new Label("Instructions: ");
        lblFeatures = new Label("Type of food ");

        txtName = new TextField();
        txtRations = new TextField();
        txtAmountOfNewIngredient = new TextField();
        txtGadget = new TextField();
        txtNeededTime = new TextField();
        txaInstructions = new TextArea();
        chbTypeOfFood = new ChoiceBox<>();

        lstAvailableIngredients = new ListView();
        lstSelectedIngredients = new ListView();
        lstGadgets = new ListView();
        btnAddIngredient = new Button("Add ingredient");
        btnDeleteIngredient = new Button("Delete ingredient");
        btnAddGadget = new Button("Add gadget");
        btnDeleteGadget = new Button("Delete gadget");
        cbxVegetarian = new CheckBox("Vegetarian");
        cbxVegan = new CheckBox("Vegan");
        cbxNoLactose = new CheckBox("No Lactose");
        cbxNoGluten = new CheckBox("No Gluten");
        gridPane = new GridPane();
    }

    protected void prepareControlEvents()
    {
        setControlProperties();

        forceFieldToBbeNumeric(txtAmountOfNewIngredient);
        forceFieldToBbeNumeric(txtRations);
        forceFieldToBbeNumeric(txtNeededTime);

        addEventToBtnAddIngredient();
        addEventToBtnDeleteIngredient();
        addEventToBtnAddGadget();
        addEventToBtnDeleteGadget();

        addControlsWithStyleIntoTheGridPane();
    }

    protected void setControlProperties()
    {
        txtName.setPromptText("Enter name");
        txtRations.setPromptText("Enter rations");
        txtNeededTime.setPromptText("Elaboration time");
        txtAmountOfNewIngredient.setPromptText("Amount");
        txtGadget.setPromptText("Enter gadget");

        chbTypeOfFood.getItems().add("Breakfast");
        chbTypeOfFood.getItems().add("Lunch");
        chbTypeOfFood.getItems().add("Dinner");
        chbTypeOfFood.getSelectionModel().select(1);

        fillListIngredientsWithIngredients();
    }

    protected void addControlsWithStyleIntoTheGridPane()
    {
        positionControlsOnGridPane();
        setStyleGridPaneControls();

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color:#AFAADC;");
        scrollPane.setContent(gridPane);
        dialog.getDialogPane().setContent(scrollPane);
    }

    protected void positionControlsOnGridPane()
    {
        gridPane.add(lblName, 1, 1);
        gridPane.add(txtName, 2, 1);
        gridPane.add(lblNumberOfRations, 3, 1);
        gridPane.add(txtRations, 4, 1);
        gridPane.add(lblTypeOfFood, 1, 2);
        gridPane.add(chbTypeOfFood, 2, 2);
        gridPane.add(lblNeededTime, 3, 2);
        gridPane.add(txtNeededTime, 4, 2);
        gridPane.add(btnAddIngredient,1,4);
        gridPane.add(txtAmountOfNewIngredient,2,4);
        gridPane.add(btnDeleteIngredient,3,4);
        gridPane.add(lstAvailableIngredients, 1, 5,2,10);
        gridPane.add(lstSelectedIngredients, 3, 5,2,10);
        gridPane.add(lblInstructions,1,15);
        gridPane.add(txaInstructions,1,16,4,4);
        gridPane.add(btnAddGadget,1,20);
        gridPane.add(txtGadget,2,20);
        gridPane.add(btnDeleteGadget,3,20);
        gridPane.add(lstGadgets,1,21,4,2);
        gridPane.add(lblFeatures,1,24);
        gridPane.add(cbxVegetarian,1,25);
        gridPane.add(cbxVegan,2,25);
        gridPane.add(cbxNoLactose,3,25);
        gridPane.add(cbxNoGluten,4,25);
    }

    protected void setStyleGridPaneControls()
    {
        lblName.setPrefWidth(50);
        lstAvailableIngredients.setPrefHeight(180);
        lstSelectedIngredients.setPrefHeight(180);
        dialog.setHeight(450);
        txtAmountOfNewIngredient.setMaxWidth(60);
        chbTypeOfFood.setMaxWidth(120);
        txtName.setMaxWidth(120);
        txtRations.setMaxWidth(120);
        txtNeededTime.setMaxWidth(120);
        txtGadget.setMaxWidth(120);
        btnAddIngredient.setMinWidth(115);
        btnDeleteIngredient.setMinWidth(115);
        btnAddGadget.setMinWidth(115);
        btnDeleteGadget.setMinWidth(115);
        lstGadgets.setPrefHeight(100);
        gridPane.setPadding(new Insets(10,10,10,10));
        gridPane.setHgap(20);
        gridPane.setVgap(20);
        gridPane.setStyle("-fx-background-color:#AFAADC;");
    }

    protected void addEventToBtnAddIngredient()
    {
        btnAddIngredient.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override public void handle(ActionEvent e)
            {
                Ingredient newIngredient = lstAvailableIngredients.getSelectionModel().getSelectedItem();
                if(newIngredient != null && returnValueOfNumericTextField(txtAmountOfNewIngredient) > 0)
                {
                    lstSelectedIngredients.getItems().add(
                            new StockIngredient(newIngredient,Short.parseShort(txtAmountOfNewIngredient.getText())));
                    FXCollections.sort(lstSelectedIngredients.getItems());
                    lstAvailableIngredients.getItems().remove(newIngredient);
                    txtAmountOfNewIngredient.clear();
                }

                else
                    showAlert("Error",
                            "Ingredient and amount required",
                            "Select ingredient and add amount (greater than zero)");
            }
        });
    }

    protected void addEventToBtnDeleteIngredient()
    {
        btnDeleteIngredient.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override public void handle(ActionEvent e)
            {
                StockIngredient ingredientToDelete = lstSelectedIngredients.getSelectionModel().getSelectedItem();
                if(ingredientToDelete != null)
                {
                    lstAvailableIngredients.getItems().add(ingredientToDelete.getIngredient());
                    FXCollections.sort(lstAvailableIngredients.getItems());
                    lstSelectedIngredients.getItems().remove(ingredientToDelete);
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

    protected void addEventToBtnAddGadget()
    {
        btnAddGadget.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override public void handle(ActionEvent e)
            {
                String newGadget = txtGadget.getText();
                if(!newGadget.isEmpty())
                {
                    lstGadgets.getItems().add(newGadget);
                    txtGadget.clear();
                }

                else
                    showAlert("Error",
                            "Gadget is required",
                            "Add a new gadget");
            }
        });
    }

    protected void addEventToBtnDeleteGadget()
    {
        btnDeleteGadget.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override public void handle(ActionEvent e)
            {
                String gadgetToDelete = lstGadgets.getSelectionModel().getSelectedItem();
                if(gadgetToDelete != null)
                    lstGadgets.getItems().remove(gadgetToDelete);

                else
                    showAlert("Error",
                            "Gadget is required",
                            "Select a gadget");
            }
        });
    }

    protected void fillListIngredientsWithIngredients()
    {
        for(Ingredient newIngredient : InitialController.ingredients.values())
            lstAvailableIngredients.getItems().add(newIngredient);

        FXCollections.sort(lstAvailableIngredients.getItems());
    }

    protected void forceFieldToBbeNumeric(TextField textField)
    {
        textField.textProperty().addListener(new ChangeListener<String>()
        {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newTextContent)
            {
                if (!newTextContent.matches("\\d*"))
                    textField.setText(newTextContent.replaceAll("[^\\d]|", ""));
            }
        });
    }

    protected int returnValueOfNumericTextField(TextField textField)
    {
        if(textField.getText().isEmpty())
            return 0;
        return Integer.parseInt(textField.getText());
    }

    protected Recipe collectDataAndReturnTheNewRecipe()
    {
        //Recipe name
        String newRecipeName = "";
        newRecipeName = txtName.getText();

        //Type of food
        byte newRecipeTypeOfFood = (byte) chbTypeOfFood.getSelectionModel().getSelectedIndex();

        //Instructions
        ArrayList<String> newRecipeInstructions = new ArrayList<>();
        String[] arrayInstructions = txaInstructions.getText().split("\n");
        for (String instruction : arrayInstructions)
            newRecipeInstructions.add(instruction);

        //Ingredients
        ArrayList<StockIngredient> newRecipeIngredients = (ArrayList<StockIngredient>)
                transformObservableListIntoList(lstSelectedIngredients.getItems());

        //Gadgets
        ArrayList<String> newRecipeGadgets = (ArrayList<String>)
                transformObservableListIntoList(lstGadgets.getItems());

        //Needed time
        Short newRecipeNeededTime = (short) returnValueOfNumericTextField(txtNeededTime);

        //Number of rations
        byte newRecipeNumberOfRations = (byte) returnValueOfNumericTextField(txtRations);

        //Features
        HashSet<String> newRecipeFeatures = new HashSet<String>();
        fillFeaturesSet(cbxVegetarian, newRecipeFeatures);
        fillFeaturesSet(cbxVegan, newRecipeFeatures);
        fillFeaturesSet(cbxNoLactose, newRecipeFeatures);
        fillFeaturesSet(cbxNoGluten, newRecipeFeatures);

        return new Recipe(
                newRecipeName,
                newRecipeTypeOfFood,
                newRecipeInstructions,
                newRecipeIngredients,
                newRecipeGadgets,
                newRecipeNeededTime,
                newRecipeNumberOfRations,
                newRecipeFeatures);
    }

    protected List transformObservableListIntoList(ObservableList observableList)
    {
        ArrayList list = new ArrayList<>();
        for (Object object : observableList)
            list.add(object);

        return list;
    }

    protected void fillFeaturesSet(CheckBox checkBox, HashSet<String> myFeatures)
    {
        if (checkBox.isSelected())
            myFeatures.add(checkBox.getText());
    }
}

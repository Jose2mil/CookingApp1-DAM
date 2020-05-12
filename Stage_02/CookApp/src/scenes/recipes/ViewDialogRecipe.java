package scenes.recipes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import objects.Recipe;
import objects.StockIngredient;

import java.util.HashSet;

public class ViewDialogRecipe
{
    Recipe recipe;
    protected javafx.scene.control.Dialog<String> dialog;
    protected Label lblName;
    protected Label lblNumberOfRations;
    protected Label lblTypeOfFood;
    protected Label lblNeededTime;
    protected Label lblInstructions;
    protected Label lblFeatures;
    protected Label lblIngredients;
    protected Label lblGadgets;
    protected TextField txtName;
    protected TextField txtNumberOfRations;
    protected TextField txtNeededTime;
    protected ListView<StockIngredient> lstIngredients;
    protected ListView<String> lstGadgets;
    protected TextArea txaInstructions;
    protected TextField txtTypeOfFood;
    protected CheckBox cbxVegetarian;
    protected CheckBox cbxVegan;
    protected CheckBox cbxNoLactose;
    protected CheckBox cbxNoGluten;
    protected GridPane gridPane;

    public ViewDialogRecipe(Recipe recipe)
    {
        dialog = new javafx.scene.control.Dialog<>();
        dialog.setTitle("Recipes");
        dialog.setHeaderText("View recipe");
        dialog.setResizable(true);

        initializeControls();
        addControlsWithStyleToTheGrid();
        blockFields();

        this.recipe = recipe;
        fillFields();
    }

    protected void initializeControls()
    {
        lblName = new Label("Name: ");
        lblNumberOfRations = new Label("Rations: ");
        lblTypeOfFood = new Label("Type: ");
        lblNeededTime = new Label("Time (min): ");
        lblInstructions = new Label("Instructions");
        lblFeatures = new Label("Type of food ");
        lblIngredients = new Label("Ingredients ");
        lblGadgets = new Label("Gadgets ");

        txtName = new TextField();
        txtNumberOfRations = new TextField();
        txtNeededTime = new TextField();
        txaInstructions = new TextArea();
        txtTypeOfFood = new TextField();

        lstIngredients = new ListView();
        lstGadgets = new ListView();
        cbxVegetarian = new CheckBox("Vegetarian");
        cbxVegan = new CheckBox("Vegan");
        cbxNoLactose = new CheckBox("No Lactose");
        cbxNoGluten = new CheckBox("No Gluten");
        gridPane = new GridPane();
    }

    protected void addControlsWithStyleToTheGrid()
    {
        positionControlsOnGridPane();
        setStyleToGridPaneControls();

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color:#AFAADC;");
        scrollPane.setContent(gridPane);
        dialog.getDialogPane().setContent(scrollPane);

        ButtonType btnGoBack = new ButtonType("Go back", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(btnGoBack);
    }

    protected void positionControlsOnGridPane()
    {
        gridPane.add(lblName, 1, 1);
        gridPane.add(txtName, 2, 1);
        gridPane.add(lblNumberOfRations, 3, 1);
        gridPane.add(txtNumberOfRations, 4, 1);
        gridPane.add(lblTypeOfFood, 1, 2);
        gridPane.add(txtTypeOfFood, 2, 2);
        gridPane.add(lblNeededTime, 3, 2);
        gridPane.add(txtNeededTime, 4, 2);
        gridPane.add(lblIngredients,1,4);
        gridPane.add(lstIngredients, 1, 5,2,5);
        gridPane.add(lblInstructions,1,10);
        gridPane.add(txaInstructions,1,11,4,3);
        gridPane.add(lblGadgets,1,14);
        gridPane.add(lstGadgets,1,15,4,2);
        gridPane.add(lblFeatures,1,18);
        gridPane.add(cbxVegetarian,1,19);
        gridPane.add(cbxVegan,2,19);
        gridPane.add(cbxNoLactose,3,19);
        gridPane.add(cbxNoGluten,4,19);
    }

    protected void setStyleToGridPaneControls()
    {
        lblName.setPrefWidth(50);
        lstIngredients.setPrefHeight(90);
        dialog.setHeight(450);
        setTextFieldStyle(txtName);
        setTextFieldStyle(txtTypeOfFood);
        setTextFieldStyle(txtNumberOfRations);
        setTextFieldStyle(txtNeededTime);
        txaInstructions.setMaxHeight(120);
        lstGadgets.setPrefHeight(100);
        gridPane.setPadding(new Insets(10,10,10,10));
        gridPane.setHgap(20);
        gridPane.setVgap(20);
        gridPane.setStyle("-fx-background-color:#AFAADC;");
    }

    private void setTextFieldStyle(TextField textField)
    {
        textField.setMaxWidth(120);
        textField.setStyle("-fx-background-color:#D6D0F0;");
        textField.setAlignment(Pos.CENTER);
    }

    private void blockFields()
    {
        txtName.setEditable(false);
        txtNumberOfRations.setEditable(false);
        txtNeededTime.setEditable(false);
        txaInstructions.setEditable(false);
        txtTypeOfFood.setDisable(true);
        cbxVegetarian.setDisable(true);
        cbxVegan.setDisable(true);
        cbxNoGluten.setDisable(true);
        cbxNoLactose.setDisable(true);
    }

    private void fillFields()
    {
        txtName.setText(recipe.getName());
        txtNumberOfRations.setText(recipe.getAmountOfRations() + "");
        txtNeededTime.setText(recipe.getTimeNeeded() + "");

        int typeOfFoodNumber = recipe.getTypeOfFood();
        String[] typeOfFoodStrings = {"Breakfast","Lunch","Dinner"};
        txtTypeOfFood.setText(typeOfFoodStrings[typeOfFoodNumber]);

        lstIngredients.getItems().setAll(recipe.getIngredients());

        int indexOfInstruction = 1;
        for(String instruction : recipe.getInstructions())
        {
            txaInstructions.appendText(instruction);
            if(indexOfInstruction != recipe.getInstructions().size())
                txaInstructions.appendText("\n");

            indexOfInstruction++;
        }

        lstGadgets.getItems().setAll(recipe.getGadgets());

        selectCheckBox(cbxVegetarian);
        selectCheckBox(cbxVegan);
        selectCheckBox(cbxNoLactose);
        selectCheckBox(cbxNoGluten);
    }

    private void selectCheckBox(CheckBox feature)
    {
        HashSet<String> features = recipe.getFeatures();
        if(features.add(feature.getText()))
            features.remove(feature.getText());
        else
            feature.setSelected(true);
    }

    public void showRecipe()
    {
        dialog.showAndWait();
    }
}

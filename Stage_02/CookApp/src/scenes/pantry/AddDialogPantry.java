package scenes.pantry;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import objects.Ingredient;
import objects.StockIngredient;
import scenes.initial.InitialController;

import java.util.Optional;

import static objects.IOUtils.*;

public class AddDialogPantry
{
    Dialog dialog;
    GridPane gridPane;
    TextField txtAmount;
    ChoiceBox<Ingredient> chbIngredients;
    Label lblMeasureUnits;
    ButtonType btnAdd;
    ObservableList<StockIngredient> internalStockIngredientList;

    public AddDialogPantry(ObservableList<StockIngredient> internalStockIngredientList)
    {
        this.internalStockIngredientList = internalStockIngredientList;
        dialog = new Dialog<>();
        initializeControls();
        addBasicDialogProperties();
        addControlsStyle();
        createGridPaneStructureAndAppendIntoDialog();
        setInputDataChecks();
    }

    private void initializeControls()
    {
        gridPane = new GridPane();
        txtAmount = new TextField();
        chbIngredients = new ChoiceBox<>();
        lblMeasureUnits = new Label("");
        btnAdd = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
    }

    private void addBasicDialogProperties()
    {
        dialog.setTitle("New ingredient");
        dialog.setHeaderText("Add a new ingredient to your pantry");
        dialog.setGraphic(new ImageView(this.getClass().getResource("../../files/img/apple.png").toString()));
        dialog.getDialogPane().getButtonTypes().addAll(btnAdd, ButtonType.CANCEL);
    }

    private void addControlsStyle()
    {
        txtAmount.setMaxWidth(90);
        txtAmount.setPromptText("Amount");
        txtAmount.setDisable(true);
        chbIngredients.setPrefWidth(150);
        txtAmount.setPrefWidth(150);
        fillChoiceBoxWithIngredients();
        lblMeasureUnits.setPadding(new Insets(0, 0, 0, 100));
    }

    private void fillChoiceBoxWithIngredients()
    {
        for(String keyName : InitialController.ingredients.keySet())
        {
            boolean found = false;
            int index = 0;

            while(! found && index < internalStockIngredientList.size())
            {
                if(keyName.equals(internalStockIngredientList.get(index).getIngredient().getName()))
                    found = true;
                index++;
            }

            if(! found)
                chbIngredients.getItems().add(InitialController.ingredients.get(keyName));
        }

        FXCollections.sort(chbIngredients.getItems());
    }

    private void createGridPaneStructureAndAppendIntoDialog()
    {
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 10, 10, 10));

        gridPane.add(new Label("Ingredient:"), 0, 0);
        gridPane.add(chbIngredients, 1, 0);
        gridPane.add(new Label("Amount:"), 0, 1);
        gridPane.add(lblMeasureUnits, 1, 1);
        gridPane.add(txtAmount, 1, 1);

        dialog.getDialogPane().setContent(gridPane);
    }

    private void setInputDataChecks()
    {
        addChbIngredientsEvent();
        addEnableDoneButtonEvent();
        forceTxtAmountToBeNumeric();
        checkAmountBeforeClicking();
    }

    private void addChbIngredientsEvent()
    {
        chbIngredients.valueProperty().addListener(new ChangeListener<Ingredient>()
        {
            @Override
            public void changed(ObservableValue<? extends Ingredient> observableValue,
                                Ingredient oldIngredient, Ingredient newIngredient)
            {
                if(newIngredient != null)
                {
                    lblMeasureUnits.setText(newIngredient.getUnitOfMeasurement());
                    txtAmount.setDisable(false);
                }

                else
                    txtAmount.setDisable(true);
            }
        });
    }

    private void addEnableDoneButtonEvent()
    {
        Node activeButton = dialog.getDialogPane().lookupButton(btnAdd);
        activeButton.setDisable(true);

        txtAmount.textProperty().addListener((observable, oldValue, newValue) -> {
            activeButton.setDisable(newValue.trim().isEmpty());
        });
    }

    private void forceTxtAmountToBeNumeric()
    {
        txtAmount.textProperty().addListener(new ChangeListener<String>()
        {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue)
            {
                if (!newValue.matches("\\d*"))
                    txtAmount.setText(newValue.replaceAll("[^\\d]|", ""));
            }
        });
    }

    private void checkAmountBeforeClicking()
    {
        dialog.setResultConverter(dialogButton ->
        {
            if (dialogButton == btnAdd)
            {
                if(Integer.parseInt(txtAmount.getText()) == 0)
                {
                    showAlert("Error",
                            "Error adding ingredient",
                            "Amount can't be 0");
                }

                else
                    return new StockIngredient(
                            chbIngredients.getValue(),
                            Short.parseShort(txtAmount.getText()));
            }

            return null;
        });
    }

    public void showAndAddNewStockIngredient(ObservableList<StockIngredient> internalStockIngredientList)
    {
        Optional<StockIngredient> result = dialog.showAndWait();
        result.ifPresent(newStockIngredient ->
        {
            internalStockIngredientList.add(newStockIngredient);
        });
    }
}

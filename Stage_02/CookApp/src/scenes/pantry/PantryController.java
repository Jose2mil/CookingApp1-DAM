package scenes.pantry;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;
import objects.Ingredient;
import objects.StockIngredient;
import scenes.initial.InitialController;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class PantryController implements Initializable
{
    public ListView<StockIngredient> listPantry;
    public Label lbKcal;
    public Label lbSugars;
    public Label lbFat;
    public Label lbFibre;
    public Label lbSaturates;
    public Label lbProtein;
    public Label lbCarbs;
    public Label lbSalt;
    public TextField txtSearchIngredient;
    ObservableList<StockIngredient> stockList;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        stockList = FXCollections.observableArrayList();
        readPantryList();
        listPantry.setItems(stockList);

        listPantry.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<StockIngredient>()
                {
                    @Override
                    public void changed(ObservableValue<? extends StockIngredient> observableValue,
                                        StockIngredient ing, StockIngredient ing1)
                    {
                        if(ing1 != null)
                            showNutritionalInformation(ing1.getIngredient());
                    }
                }
        );

    }

    public void backToMainWindow(ActionEvent actionEvent) throws IOException {
        Parent home_page_parent = FXMLLoader.load(getClass().getResource("../initial/initial.fxml"));
        Scene home_page_scene = new Scene(home_page_parent);
        Stage app_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        app_stage.hide();
        app_stage.setScene(home_page_scene);
        app_stage.show();
    }

    private void showNutritionalInformation(Ingredient ingredient)
    {
        lbKcal.setText(String.valueOf(ingredient.getKcal()));
        lbSugars.setText(String.valueOf(ingredient.getSugars()));
        lbFat.setText(String.valueOf(ingredient.getLipidFats()));
        lbFibre.setText(String.valueOf(ingredient.getFibre()));
        lbSaturates.setText(String.valueOf(ingredient.getSaturatedFats()));
        lbProtein.setText(String.valueOf(ingredient.getProtein()));
        lbCarbs.setText(String.valueOf(ingredient.getCarbohydrates()));
        lbSalt.setText(String.valueOf(ingredient.getSalt()));
    }

    private void readPantryList()
    {
        try
        {
            List<String> lines = Files.readAllLines(Paths.get("src/files/data/stock.txt"));
            for(String line : lines)
                stockList.add(
                        new StockIngredient(
                                InitialController.ingredients.get(line.split(";")[0]),
                                Short.parseShort(line.split(";")[1])));

            FXCollections.sort(stockList);
        }
        catch (Exception e)
        {
            stockList.clear();
        }
    }

    public void saveAndUpdateStockIngredients()
    {
        PrintWriter stockFile = null;
        try
        {
            stockFile = new PrintWriter("src/files/data/stock.txt");
            for(int i = 0; i < stockList.size(); i++)
                stockFile.println(stockList.get(i).getIngredient().getName() + ";"
                 + stockList.get(i).getAmount());
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            if(stockFile != null)
                stockFile.close();
        }

        FXCollections.sort(stockList);
        listPantry.setItems(stockList);
    }

    public void deleteStockIngredient(ActionEvent actionEvent)
    {
        StockIngredient stockIngredientSelected = listPantry.getSelectionModel().getSelectedItem();

        if(stockIngredientSelected != null)
        {
            stockList.remove(stockIngredientSelected);
            saveAndUpdateStockIngredients();
        }

        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error removing ingredient");
            alert.setContentText("No ingredient selected");
            alert.showAndWait();
        }
    }

    public void addIngredient(ActionEvent actionEvent)
    {
        // Create the custom dialog and his controls and containers.
        Dialog dialog = new Dialog<>();
        ButtonType addButton = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        GridPane grid = new GridPane();
        TextField amount = new TextField();
        ChoiceBox<Ingredient> choiceBox = new ChoiceBox<>();
        Label lblUnit = new Label("");

        createDialogStructure(
                dialog, addButton, grid, amount, choiceBox, lblUnit);
        setInputDataChecksInDialog(
                dialog, choiceBox, lblUnit, amount, addButton);
        dialogOutputDataPreparation(
                dialog, addButton, choiceBox, amount);
    }


    private void createDialogStructure(Dialog dialog, ButtonType addButton,
                                       GridPane grid, TextField amount,
                                       ChoiceBox<Ingredient> choiceBox, Label lblUnit)
    {
        dialog.setTitle("New ingredient");
        dialog.setHeaderText("Add a new ingredient to your pantry");
        dialog.setGraphic(new ImageView(this.getClass().getResource("../../files/img/apple.png").toString()));

        dialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 10, 10, 10));

        amount.setMaxWidth(90);
        amount.setPromptText("Amount");
        amount.setDisable(true);

        choiceBox.setPrefWidth(150);
        amount.setPrefWidth(150);

        fillChoiceBoxWithIngredients(choiceBox);

        lblUnit.setPadding(new Insets(0, 0, 0, 100));

        grid.add(new Label("Ingredient:"), 0, 0);
        grid.add(choiceBox, 1, 0);
        grid.add(new Label("Amount:"), 0, 1);
        grid.add(lblUnit, 1, 1);
        grid.add(amount, 1, 1);

        dialog.getDialogPane().setContent(grid);
    }

    public void editIngredient(ActionEvent actionEvent)
    {
        StockIngredient stockIngredientSelected = listPantry.getSelectionModel().getSelectedItem();
        if(stockIngredientSelected != null)
        {
            TextInputDialog dialog = new TextInputDialog(stockIngredientSelected.getAmount() + "");
            dialog.setTitle("Edit amount");
            dialog.setHeaderText(stockIngredientSelected.getIngredient().getName());
            dialog.setContentText("Enter the new amount:");

            Optional<String> result = dialog.showAndWait();

            if(isIntegerGreaterThanZero(result.get()))
            {
                result.ifPresent(newStockIngredient -> {
                    for (StockIngredient s : stockList) {
                        if(s.equals(stockIngredientSelected)) {
                            s.setAmount(Short.parseShort(result.get()));
                        }
                    }
                    saveAndUpdateStockIngredients();
                });
            }
            else
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error editing ingredient");
                alert.setContentText("Amount must be an integer greater than 0");
                alert.showAndWait();

            }
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error editing ingredient");
            alert.setContentText("No ingredient selected");
            alert.showAndWait();
        }
    }

    private void setInputDataChecksInDialog(Dialog dialog, ChoiceBox choiceBox,
                                            Label lblUnit, TextField amount,
                                            ButtonType addButton)
    {
        choiceBox.valueProperty().addListener(new ChangeListener<Ingredient>() {
            @Override
            public void changed(ObservableValue<? extends Ingredient> observableValue,
                                Ingredient oldIngredient, Ingredient newIngredient) {
                if(newIngredient != null)
                {
                    lblUnit.setText(newIngredient.getUnitOfMeasurement());
                    amount.setDisable(false);
                }

                else
                    amount.setDisable(true);
            }
        });

        // Enable/Disable done button depending on whether an amount was entered.
        Node activeButton = dialog.getDialogPane().lookupButton(addButton);
        activeButton.setDisable(true);

        // Do some validation (using the Java 8 lambda syntax).
        amount.textProperty().addListener((observable, oldValue, newValue) -> {
            activeButton.setDisable(newValue.trim().isEmpty());
        });

        // force the field to be numeric only
        amount.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    amount.setText(newValue.replaceAll("[^\\d]|", ""));
                }
            }
        });
    }

    private void dialogOutputDataPreparation(Dialog dialog, ButtonType addButton,
                                             ChoiceBox choiceBox, TextField amount)
    {
        // Convert the result when the button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButton)
            {
                if(Integer.parseInt(amount.getText()) == 0)
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Error adding ingredient");
                    alert.setContentText("Amount can't be 0");
                    alert.showAndWait();
                }

                else
                    return new Pair<>(choiceBox.getValue(), amount.getText());
            }
            return null;
        });

        Optional<Pair<Ingredient, String>> result = dialog.showAndWait();
        result.ifPresent(newStockIngredient -> {
            stockList.add(
                    new StockIngredient(
                            newStockIngredient.getKey(),
                            Short.parseShort(newStockIngredient.getValue())));

            saveAndUpdateStockIngredients();
        });
    }

    private void fillChoiceBoxWithIngredients(ChoiceBox choiceBox)
    {
        for(String keyName : InitialController.ingredients.keySet())
        {
            boolean found = false;
            int index = 0;

            while(! found && index < stockList.size())
            {
                if(keyName.equals(stockList.get(index).getIngredient().getName()))
                    found = true;
                index++;
            }

            if(! found)
                choiceBox.getItems().add(InitialController.ingredients.get(keyName));
        }
    }

    public void searchIngredient(KeyEvent keyEvent)
    {
        ObservableList<StockIngredient> stockListCopy = FXCollections.observableArrayList();
        String subString = (txtSearchIngredient.getText() + keyEvent.getText()).toUpperCase();

        for(int i = 0; i <stockList.size(); i++)
            if(stockList.get(i).getIngredient().getName().toUpperCase().contains(subString))
                stockListCopy.add(stockList.get(i));

        listPantry.setItems(stockListCopy);
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
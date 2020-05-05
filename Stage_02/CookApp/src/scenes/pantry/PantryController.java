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
        // Create the custom dialog.
        Dialog dialog = new Dialog<>();

        dialog.setTitle("New ingredient");
        dialog.setHeaderText("Add a new ingredient to your pantry");

        // Set the icon (must be included in the project).
        dialog.setGraphic(new ImageView(this.getClass().getResource("../../files/img/apple.png").toString()));

        // Set the button types.
        ButtonType addButton = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

        // Create labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 10, 10, 10));

        TextField amount = new TextField();
        amount.setMaxWidth(90);
        amount.setPromptText("Amount");

        ChoiceBox<Ingredient> choiceBox = new ChoiceBox<>();
        choiceBox.setPrefWidth(150);
        amount.setPrefWidth(150);

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

        Label lblUnit = new Label("");
        lblUnit.setPadding(new Insets(0, 0, 0, 100));

        grid.add(new Label("Ingredient:"), 0, 0);
        grid.add(choiceBox, 1, 0);
        grid.add(new Label("Amount:"), 0, 1);
        grid.add(lblUnit, 1, 1);
        grid.add(amount, 1, 1);

        amount.setDisable(true);

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
                    amount.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        dialog.getDialogPane().setContent(grid);

        // Convert the result when the button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButton)
            {
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

    public void searchIngredient(KeyEvent keyEvent)
    {
        ObservableList<StockIngredient> stockListCopy = FXCollections.observableArrayList();
        String subString = (txtSearchIngredient.getText() + keyEvent.getText()).toUpperCase();

        for(int i = 0; i <stockList.size(); i++)
            if(stockList.get(i).getIngredient().getName().toUpperCase().contains(subString))
                stockListCopy.add(stockList.get(i));

        listPantry.setItems(stockListCopy);
    }
}
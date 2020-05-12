package scenes.pantry;

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
import objects.Ingredient;
import objects.StockIngredient;
import scenes.initial.InitialController;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import static objects.IOUtils.*;

public class PantryController implements Initializable
{
    public ListView<StockIngredient> lstPantry;
    public Label lblKcal;
    public Label lblSugars;
    public Label lblFat;
    public Label lblFibre;
    public Label lblSaturates;
    public Label lblProtein;
    public Label lblCarbohydrates;
    public Label lblSalt;
    public TextField txtSearchIngredient;
    ObservableList<StockIngredient> internalStockIngredientList;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        internalStockIngredientList = FXCollections.observableArrayList();
        readPantryList();
        lstPantry.setItems(internalStockIngredientList);
        addEventSelectIngredient();
    }

    private void addEventSelectIngredient()
    {
        lstPantry.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<StockIngredient>()
                {
                    @Override
                    public void changed(ObservableValue<? extends StockIngredient> observableValue,
                                        StockIngredient ing, StockIngredient selectedStockIngredient)
                    {
                        if(selectedStockIngredient != null)
                            showNutritionalInformation(selectedStockIngredient.getIngredient());
                    }
                }
        );
    }

    public void backToInitialScene(ActionEvent actionEvent)
            throws IOException
    {
        Parent home_page_parent = FXMLLoader.load(getClass().getResource("../initial/initial.fxml"));
        Scene home_page_scene = new Scene(home_page_parent);
        Stage app_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        app_stage.hide();
        app_stage.setScene(home_page_scene);
        app_stage.show();
    }

    private void showNutritionalInformation(Ingredient ingredient)
    {
        lblKcal.setText(String.valueOf(ingredient.getKcal()));
        lblSugars.setText(String.valueOf(ingredient.getSugars()));
        lblFat.setText(String.valueOf(ingredient.getLipidFats()));
        lblFibre.setText(String.valueOf(ingredient.getFibre()));
        lblSaturates.setText(String.valueOf(ingredient.getSaturatedFats()));
        lblProtein.setText(String.valueOf(ingredient.getProtein()));
        lblCarbohydrates.setText(String.valueOf(ingredient.getCarbohydrates()));
        lblSalt.setText(String.valueOf(ingredient.getSalt()));
    }

    private void readPantryList()
    {
        try
        {
            List<String> lines = Files.readAllLines(Paths.get("src/files/data/stock.txt"));
            for(String line : lines)
                internalStockIngredientList.add(
                        new StockIngredient(
                                InitialController.ingredients.get(line.split(";")[0]),
                                Short.parseShort(line.split(";")[1])));

            FXCollections.sort(internalStockIngredientList);
        }
        catch (Exception e)
        {
            internalStockIngredientList.clear();
        }

        FXCollections.sort(internalStockIngredientList);
        lstPantry.setItems(internalStockIngredientList);
    }

    public void updateAndSaveStockIngredients()
    {
        FXCollections.sort(internalStockIngredientList);
        lstPantry.setItems(internalStockIngredientList);

        PrintWriter stockFile = null;
        try
        {
            stockFile = new PrintWriter("src/files/data/stock.txt");
            for(int i = 0; i < internalStockIngredientList.size(); i++)
                stockFile.println(internalStockIngredientList.get(i).getIngredient().getName() + ";"
                 + internalStockIngredientList.get(i).getAmount());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(stockFile != null)
                stockFile.close();
        }
    }

    public void deleteStockIngredient(ActionEvent actionEvent)
    {
        StockIngredient stockIngredientSelected = lstPantry.getSelectionModel().getSelectedItem();

        if(stockIngredientSelected != null)
        {
            internalStockIngredientList.remove(stockIngredientSelected);
            updateAndSaveStockIngredients();
        }

        else
            showAlert("Error",
                    "Error removing ingredient",
                    "No ingredient selected");
    }

    public void addIngredient(ActionEvent actionEvent)
    {
        new AddDialogPantry(internalStockIngredientList).
                showAndAddNewStockIngredient(internalStockIngredientList);
        updateAndSaveStockIngredients();
    }

    public void editIngredient(ActionEvent actionEvent)
    {
        StockIngredient stockIngredientSelected = lstPantry.getSelectionModel().getSelectedItem();

        if(stockIngredientSelected != null)
        {
            EditDialogPantry editDialogPantry = new EditDialogPantry(stockIngredientSelected);
            editDialogPantry.showAndEditStockIngredient(
                    internalStockIngredientList.get(
                            lstPantry.getSelectionModel().getSelectedIndex()));

            updateAndSaveStockIngredients();
        }

        else
            showAlert("Error",
                    "Error editing ingredient",
                    "No ingredient selected");
    }

    public void searchIngredient(KeyEvent keyEvent)
    {
        ObservableList<StockIngredient> stockListCopy = FXCollections.observableArrayList();
        String subString = (txtSearchIngredient.getText() + keyEvent.getText()).toUpperCase();

        for(int i = 0; i < internalStockIngredientList.size(); i++)
            if(internalStockIngredientList.get(i).getIngredient().getName().toUpperCase().contains(subString))
                stockListCopy.add(internalStockIngredientList.get(i));

        lstPantry.setItems(stockListCopy);
    }
}
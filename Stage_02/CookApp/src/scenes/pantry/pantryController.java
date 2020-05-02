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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class pantryController implements Initializable
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
    ObservableList<Ingredient> ingredients;
    ObservableList<Short> amountList;
    ObservableList<Short> amountListUpdated;
    ObservableList<StockIngredient> stockList;
    ObservableList<StockIngredient> stockListCopy;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        ingredients = FXCollections.observableArrayList(readIngredients());
        ingredients.sorted();
        amountList = FXCollections.observableArrayList(readStock());

        stockList = FXCollections.observableArrayList();
        stockListCopy = FXCollections.observableArrayList();

        for(int i=0; i < ingredients.size(); i++)
        {
            if(amountList.get(i) != 0) {
                stockList.add(new StockIngredient(ingredients.get(i), amountList.get(i)));
                stockListCopy.add(new StockIngredient(ingredients.get(i), amountList.get(i)));
            }
        }
        listPantry.setItems(stockList);

        listPantry.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<StockIngredient>()
                {
                    @Override
                    public void changed(ObservableValue<? extends StockIngredient> observableValue,
                                        StockIngredient ing, StockIngredient ing1)
                    {
                        if(ing1 != null)
                        {
                            lbKcal.setText(String.valueOf(ing1.getIngredient().getKcal()));
                            lbSugars.setText(String.valueOf(ing1.getIngredient().getSugars()));
                            lbFat.setText(String.valueOf(ing1.getIngredient().getLipidFats()));
                            lbFibre.setText(String.valueOf(ing1.getIngredient().getFibre()));
                            lbSaturates.setText(String.valueOf(ing1.getIngredient().getSaturatedFats()));
                            lbProtein.setText(String.valueOf(ing1.getIngredient().getProtein()));
                            lbCarbs.setText(String.valueOf(ing1.getIngredient().getCarbohydrates()));
                            lbSalt.setText(String.valueOf(ing1.getIngredient().getSalt()));
                        }
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

    private List<Ingredient> readIngredients()
    {

        try
        {
            List<String> lines = Files.readAllLines(Paths.get("src/files/data/ingredients.txt"));
            return lines.stream()
                    .map(line -> new Ingredient(line.split(";")[0], //Name
                            line.split(";")[1], //UnitOfMeasurement
                            Float.parseFloat(line.split(";")[2]), //Kcal
                            Float.parseFloat(line.split(";")[3]), //LipidFats
                            Float.parseFloat(line.split(";")[4]), //SaturatedFats
                            Float.parseFloat(line.split(";")[5]), //Carbohydrates
                            Float.parseFloat(line.split(";")[6]), //Sugars
                            Float.parseFloat(line.split(";")[7]), //Protein
                            Float.parseFloat(line.split(";")[8]), //Fibre
                            Float.parseFloat(line.split(";")[9])) //Salt
                    )
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private List<Short> readStock()
    {
        try
        {
            List<String> lines = Files.readAllLines(Paths.get("src/files/data/stock.txt"));
            return lines.stream()
                    .map(line ->    Short.parseShort(line.split(";")[1]) //Salt
                    )
                    .collect(Collectors.toList());
        }
        catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public void deleteIngredient(ActionEvent actionEvent)
    {

        StockIngredient ingredientSelected = listPantry.getSelectionModel().getSelectedItem();
        if(ingredientSelected != null)
        {

            PrintWriter listOfIngredients = null;
            try
            {
                listOfIngredients = new PrintWriter("src/files/data/stock.txt");
                for(int i = 0; i < ingredients.size(); i++)
                {
                    if(ingredients.get(i).getName().equals(ingredientSelected.getIngredient().getName()))
                        listOfIngredients.println(
                                ingredients.get(i).getName() + ";0");
                    else
                        listOfIngredients.println(ingredients.get(i).getName() + ";"
                                + amountList.get(i));
                }

                stockList.remove(ingredientSelected);
                stockListCopy.remove(ingredientSelected);

            }

            catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                listOfIngredients.close();
            }

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
        //TODO
    }

    public void searchIngredient(KeyEvent keyEvent)
    {
        stockList.clear();


        for(int i = 0; i <stockListCopy.size(); i++)
        {
            if(stockListCopy.get(i).getIngredient().getName().contains(txtSearchIngredient.getText()
                    + keyEvent.getText()))
                stockList.add(stockListCopy.get(i));
        }
    }


}
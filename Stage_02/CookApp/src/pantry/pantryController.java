package pantry;

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
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import myClasses.Ingredient;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class pantryController implements Initializable
{
    public ListView listPantry;
    public Label lbKcal;
    public Label lbSugars;
    public Label lbFat;
    public Label lbFibre;
    public Label lbSaturates;
    public Label lbProtein;
    public Label lbCarbs;
    public Label lbSalt;
    ObservableList<Ingredient> products;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        products = FXCollections.observableArrayList(readIngredients());
        listPantry.setItems(products.sorted());

        listPantry.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Ingredient>()
                {
                    @Override
                    public void changed(ObservableValue<? extends Ingredient> observableValue,
                                        Ingredient ing, Ingredient ing1) {
                        {
                            lbKcal.setText(String.valueOf(ing1.getKcal()));
                            lbSugars.setText(String.valueOf(ing1.getSugars()));
                            lbFat.setText(String.valueOf(ing1.getLipidFats()));
                            lbFibre.setText(String.valueOf(ing1.getFibre()));
                            lbSaturates.setText(String.valueOf(ing1.getSaturatedFats()));
                            lbProtein.setText(String.valueOf(ing1.getProtein()));
                            lbCarbs.setText(String.valueOf(ing1.getCarbohydrates()));
                            lbSalt.setText(String.valueOf(ing1.getSalt()));
                        }
                    }
                }
        );
    }

    public void backToMainWindow(ActionEvent actionEvent) throws IOException {
        Parent home_page_parent = FXMLLoader.load(getClass().getResource("../init/initial.fxml"));
        Scene home_page_scene = new Scene(home_page_parent);
        Stage app_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        app_stage.hide();
        app_stage.setScene(home_page_scene);
        app_stage.show();
    }

    private List<Ingredient> readIngredients() {

        try
        {
            List<String> lines = Files.readAllLines(Paths.get("src/files/ingredients.txt"));
            System.out.println(lines.size());
            return lines.stream()
                    .map(line -> new Ingredient(line.split(";")[0],
                            Short.parseShort(line.split(";")[1]),
                            line.split(";")[2],
                            Float.parseFloat(line.split(";")[3]),
                            Float.parseFloat(line.split(";")[4]),
                            Float.parseFloat(line.split(";")[5]),
                            Float.parseFloat(line.split(";")[6]),
                            Float.parseFloat(line.split(";")[7]),
                            Float.parseFloat(line.split(";")[8]),
                            Float.parseFloat(line.split(";")[9]),
                            Float.parseFloat(line.split(";")[10]))
                    )
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}

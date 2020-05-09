package scenes.initial;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import objects.Ingredient;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class InitialController implements Initializable {
    public static HashMap<String, Ingredient> ingredients;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        readIngredients();
    }

    static void readIngredients()
    {
        try
        {
            ingredients = new HashMap<>();
            List<String> lines = Files.readAllLines(Paths.get("src/files/data/ingredients.txt"));
            for (String line : lines)
            {
                ingredients.put(line.split(";")[0], //Name (Key)
                        new Ingredient(line.split(";")[0], //Name
                                line.split(";")[1], //UnitOfMeasurement
                                Float.parseFloat(line.split(";")[2]), //Kcal
                                Float.parseFloat(line.split(";")[3]), //LipidFats
                                Float.parseFloat(line.split(";")[4]), //SaturatedFats
                                Float.parseFloat(line.split(";")[5]), //Carbohydrates
                                Float.parseFloat(line.split(";")[6]), //Sugars
                                Float.parseFloat(line.split(";")[7]), //Protein
                                Float.parseFloat(line.split(";")[8]), //Fibre
                                Float.parseFloat(line.split(";")[9])) //Salt
                );
            }
        } catch (Exception e)
        {
            ingredients.clear();
        }
    }

    public void showPantryScreen(ActionEvent actionEvent) throws IOException
    {
        showNewScreen(actionEvent, "../pantry/pantry.fxml");
    }

    public void showRecipesScreen(ActionEvent actionEvent) throws IOException
    {
        showNewScreen(actionEvent, "../recipes/recipes.fxml");
    }

    public void showNewScreen(ActionEvent actionEvent, String path) throws IOException
    {
        Parent home_page_parent = FXMLLoader.load(getClass().getResource(path));
        Scene home_page_scene = new Scene(home_page_parent);
        Stage app_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        app_stage.hide();
        app_stage.setScene(home_page_scene);
        app_stage.show();
    }
}

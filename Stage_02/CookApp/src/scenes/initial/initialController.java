package scenes.initial;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class initialController implements Initializable
{
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void showPantryScreen(ActionEvent actionEvent) throws IOException
    {
        Parent home_page_parent = FXMLLoader.load(getClass().getResource("../pantry/pantry.fxml"));
        Scene home_page_scene = new Scene(home_page_parent);
        Stage app_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        app_stage.hide();
        app_stage.setScene(home_page_scene);
        app_stage.show();
    }
}

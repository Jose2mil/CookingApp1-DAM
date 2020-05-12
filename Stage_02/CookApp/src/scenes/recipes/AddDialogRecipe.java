package scenes.recipes;

import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import objects.Recipe;
import java.util.Optional;

import static objects.IOUtils.*;

public class AddDialogRecipe extends IODialogRecipes
{
    public AddDialogRecipe()
    {
        super();
        dialog.setHeaderText("New recipe");

        ButtonType buttonAdd = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonAdd);
    }

    public Recipe showAndReturnNewRecipe()
    {
        Optional<String> result = dialog.showAndWait();

        if(! result.isEmpty())
        {
            if(txtName.getText().isEmpty())
                showAlert("Error","Error adding recipe","The name can't be empty");

            else
                return collectDataAndReturnTheNewRecipe();
        }

        return null;
    }
}

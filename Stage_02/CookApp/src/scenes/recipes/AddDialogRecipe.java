package scenes.recipes;

import objects.Recipe;
import java.util.Optional;

public class AddDialogRecipe extends DialogRecipes
{
    public AddDialogRecipe()
    {
        super();
        dialog.setHeaderText("New recipe");
    }

    public Recipe showAndReturnNewRecipe()
    {
        Optional<String> result = dialog.showAndWait();

        if(! result.isEmpty())
        {
            if(tName.getText().isEmpty())
                showAlert("Error","Error adding recipe","The name can't be empty");

            else
                return collectDataAndReturnRecipe();
        }

        return null;
    }
}

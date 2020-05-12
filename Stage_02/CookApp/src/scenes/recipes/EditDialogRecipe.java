package scenes.recipes;

import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import objects.Recipe;
import objects.StockIngredient;

import java.util.HashSet;
import java.util.Optional;

public class EditDialogRecipe extends IODialogRecipes
{
    Recipe recipe;

    public EditDialogRecipe(Recipe recipe)
    {
        super();
        dialog.setHeaderText("Edit recipe");
        txtName.setEditable(false);
        this.recipe = recipe;
        fillFieldsWithRecipeData();

        ButtonType buttonAdd = new ButtonType("Edit", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonAdd);
    }

    private void fillFieldsWithRecipeData()
    {
        txtName.setText(recipe.getName());
        txtRations.setText(recipe.getAmountOfRations() + "");
        chbTypeOfFood.getSelectionModel().select(recipe.getTypeOfFood() - 1);
        txtNeededTime.setText(recipe.getTimeNeeded() + "");

        lstSelectedIngredients.getItems().setAll(recipe.getIngredients());
        for(StockIngredient stockIngredient : lstSelectedIngredients.getItems())
            lstAvailableIngredients.getItems().remove(stockIngredient.getIngredient());

        int index = 1;
        for(String instruction : recipe.getInstructions())
        {
            txaInstructions.appendText(instruction);
            if(index != recipe.getInstructions().size())
                txaInstructions.appendText("\n");

            index++;
        }

        lstGadgets.getItems().setAll(recipe.getGadgets());

        selectCheckBox(cbxVegetarian);
        selectCheckBox(cbxVegan);
        selectCheckBox(cbxNoLactose);
        selectCheckBox(cbxNoGluten);
    }

    private void selectCheckBox(CheckBox feature)
    {
        HashSet<String> features = recipe.getFeatures();
        if(features.add(feature.getText()))
            features.remove(feature.getText());
        else
            feature.setSelected(true);
    }

    public Recipe showAndReturnRecipe()
    {
        Optional<String> result = dialog.showAndWait();

        if(! result.isEmpty())
            return collectDataAndReturnTheNewRecipe();

        return  null;
    }
}

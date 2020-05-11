package scenes.recipes;

import javafx.scene.control.CheckBox;
import objects.Recipe;
import objects.StockIngredient;

import java.util.HashSet;
import java.util.Optional;

public class EditDialogRecipe extends DialogRecipes
{
    Recipe recipe;
    public EditDialogRecipe(Recipe recipe)
    {
        super();
        dialog.setHeaderText("Edit recipe");
        tName.setEditable(false);
        this.recipe = recipe;
        fillFields();
    }

    private void fillFields()
    {
        tName.setText(recipe.getName());
        tRations.setText(recipe.getAmountOfRations() + "");
        typeChoices.getSelectionModel().select(recipe.getTypeOfFood() - 1);
        tTime.setText(recipe.getTimeNeeded() + "");

        ingredientsSelected.getItems().setAll(recipe.getIngredients());
        for(StockIngredient stockIngredient : ingredientsSelected.getItems())
            ingredientsList.getItems().remove(stockIngredient.getIngredient());

        int index = 1;
        for(String instruction : recipe.getInstructions())
        {
            txtAreaInstructions.appendText(instruction);
            if(index != recipe.getInstructions().size())
                txtAreaInstructions.appendText("\n");

            index++;
        }

        gadgetsList.getItems().setAll(recipe.getGadgets());

        selectCheckBox(cbVegetarian);
        selectCheckBox(cbVegan);
        selectCheckBox(cbLactose);
        selectCheckBox(cbGluten);
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
            return collectDataAndReturnRecipe();

        return  null;
    }
}

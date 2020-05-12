package scenes.pantry;

import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import objects.StockIngredient;

import java.util.Optional;

import static objects.IOUtils.*;

public class EditDialogPantry
{
    StockIngredient stockIngredient;
    TextInputDialog dialog;

    public EditDialogPantry(StockIngredient stockIngredient)
    {
        this.stockIngredient = stockIngredient;

        dialog = new TextInputDialog(stockIngredient.getAmount() + "");
        dialog.setTitle("Edit amount");
        dialog.setHeaderText(stockIngredient.getIngredient().getName());
        dialog.setContentText("Enter the new amount:");
    }

    public void showAndEditStockIngredient(StockIngredient stockIngredient)
    {
        Optional<String> result = dialog.showAndWait();

        if(! result.isEmpty() && isIntegerGreaterThanZero(result.get()))
        {
            stockIngredient
                    .setAmount(Short.parseShort(result.get()));
        }
        else if(! result.isEmpty())
            showAlert("Error",
                    "Error editing ingredient",
                    "Amount must be an integer greater than 0");
    }

    public static boolean isIntegerGreaterThanZero(String strNum)
    {
        int num;
        if (strNum == null)
            return false;

        try
        {
            num = Integer.parseInt(strNum);
        }
        catch (NumberFormatException nfe)
        {
            return false;
        }

        if(num <= 0 )
            return false;

        return true;
    }
}

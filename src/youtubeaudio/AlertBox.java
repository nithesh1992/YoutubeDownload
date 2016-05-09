package youtubeaudio;

/**
 * Created by Nithesh on 2/28/2016.
 */

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class AlertBox {

    public static void display(String title, String message , String type) {
        String info = "INFORMATION";

        Alert alert = null;
        if (type.contains("Warning"))
        {
             alert = new Alert(AlertType.WARNING);
        }
        else if (type.contains("Error"))
        {
             alert = new Alert(AlertType.ERROR);
        }
        else if (type.contains("Confirm"))
        {
            alert = new Alert(AlertType.CONFIRMATION);
        }
        else
        {
            alert = new Alert(AlertType.INFORMATION);
        }


        alert.setTitle(title);
        alert.setContentText(message);

        alert.showAndWait();
    }

}

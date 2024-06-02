package org.example.graphic.scene;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;

public class Popup {

    public static void showError(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.initModality(Modality.APPLICATION_MODAL);
//            alert.initOwner(Application.getPrimaryStage());
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.show();
        });
    }
    public static void showDialog(String message) {
        Platform.runLater(() -> {

            Alert alert = new Alert(AlertType.INFORMATION);
        alert.initModality(Modality.APPLICATION_MODAL);
//        alert.initOwner(Application.getPrimaryStage());

        alert.setTitle("INFO");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
        });

    }

}

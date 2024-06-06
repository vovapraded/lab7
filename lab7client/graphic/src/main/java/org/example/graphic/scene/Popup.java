package org.example.graphic.scene;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import org.common.dto.TicketType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

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

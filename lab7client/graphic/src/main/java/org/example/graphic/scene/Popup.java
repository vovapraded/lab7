package org.example.graphic.scene;

import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import org.controller.MyController;
import org.example.exception.ReceivingException;
import org.example.graphic.localizator.Localizator;
import org.example.exception.NoResponseException;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

public class Popup {

    private static final Localizator localizator=Localizator.getInstance();
    private static final MyController controller = MyController.getInstance();
    public static void showError(Exception e) {
        if (e instanceof ReceivingException){
            showReceivingError((ReceivingException) e);
        }else {
            showCommonError(e);
        }
    }
    private static void showCommonError(Exception e) {

        Platform.runLater(() -> {
            var message = localizator.getKeyString(e.getMessage());
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.initModality(Modality.APPLICATION_MODAL);
//            alert.initOwner(Application.getPrimaryStage());
            alert.setHeaderText(null);
            alert.setContentText(message);
            System.out.println(message);
            alert.show();
        });
    }
    private static Alert receivingErrorAlert ;

    private static void showReceivingError(ReceivingException e) {
        Platform.runLater(() -> {

            var message = localizator.getKeyString(e.getMessage());
            if (receivingErrorAlert== null || !receivingErrorAlert.isShowing()) {
                receivingErrorAlert = new Alert(AlertType.ERROR);
                receivingErrorAlert.setTitle("Error");
                receivingErrorAlert.initModality(Modality.APPLICATION_MODAL);
//            receivingErrorAlert.initOwner(Application.getPrimaryStage());
                receivingErrorAlert.setHeaderText(null);
                receivingErrorAlert.setContentText(message);
                receivingErrorAlert.getButtonTypes().clear();
                ButtonType buttonType = new ButtonType(localizator.getKeyString("RetryLabel"), ButtonBar.ButtonData.OK_DONE);
                receivingErrorAlert.getButtonTypes().add(buttonType);
                receivingErrorAlert.resultProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue == buttonType) {
                        try {
                            MyController.getClickQueue().put(new Object());
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                });
                System.out.println(message);
                receivingErrorAlert.show();

            }
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

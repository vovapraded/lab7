package org.example.graphic.scene.main.command.filter;

import com.dlsc.formsfx.model.structure.DateField;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import org.common.dto.TicketType;
import org.example.graphic.scene.Application;
import org.example.graphic.scene.Popup;

import java.util.Arrays;
import java.util.HashMap;

public class FilterPanel {
    public Button createButton(HashMap<Node,String> nodeAndKeys){
        Button button = new Button();
        nodeAndKeys.put(button,"FilterLabel");
        button.setOnAction(e -> {
            showFilterForm(nodeAndKeys);
        });
        return button;
    }
    public static void showFilterForm(HashMap<Node,String> nodeAndKeys) {
        Platform.runLater(() -> {

            // Создаем диалог
            Dialog<Void> dialog = new Dialog<>();
            dialog.initModality(Modality.NONE);
            dialog.setTitle("Filter");

            // Устанавливаем кнопки диалога
            ButtonType applyButtonType = new ButtonType("Apply", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(applyButtonType, ButtonType.CANCEL);
            Text ticketDetailsLabel = new Text();
            // Создаем элементы управления формы
            TextField idField = new TextField();
            Label idLabel = new Label();
            TextField nameField = new TextField();
            Label nameLabel = new Label();




            ComboBox<String> genderComboBox = new ComboBox<>();
            genderComboBox.getItems().addAll(Arrays.stream(TicketType.values()).map(TicketType::toString).toList());
            var ticketTypeLabel = new Label();


            // Размещаем элементы управления на сетке
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));
            grid.add(ticketDetailsLabel, 0, 0);
            grid.add(idLabel, 0, 1);
            grid.add(idField, 1, 1);
            grid.add(nameLabel, 0, 2);
            grid.add(nameField, 1, 2);
            grid.add(ticketTypeLabel, 0, 3);
            grid.add(genderComboBox, 1, 3);

            nodeAndKeys.put(ticketDetailsLabel, "TicketDetailsLabel");
            nodeAndKeys.put(idLabel, "IdLabel");
            nodeAndKeys.put(nameLabel, "NameLabel");
            nodeAndKeys.put(ticketTypeLabel, "TypeLabel");
            nodeAndKeys.put(genderComboBox, "TypeLabel");


            Application.getMainSceneObj().updateTexts();
            dialog.getDialogPane().setContent(grid);

            // Обработка нажатия кнопок
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == applyButtonType) {
                    System.out.println("Name: " + nameField.getText());
                    System.out.println("Age: " + idField.getText());
                    System.out.println("Gender: " + genderComboBox.getValue());
                }
                return null;
            });

            dialog.showAndWait();
        });
    }
//    private void showFilterForm(){
//        Popup.showFilterForm(n);
//
//    }
}

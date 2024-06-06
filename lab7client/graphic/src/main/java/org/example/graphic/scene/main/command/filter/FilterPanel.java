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
import org.example.graphic.scene.main.command.Panel;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashMap;

public class FilterPanel extends Panel {
    public FilterPanel() {
        super("Filter");
    }


    @Override
    public  void showForm(HashMap<Node,String> nodeAndKeys) {
        Platform.runLater(() -> {
            initDialog();

            Text ticketDetailsLabel = new Text();
            // Создаем элементы управления формы
            TextField idField = new TextField();
            Label idLabel = new Label();
            TextField partOfNameField = new TextField();
            Label partOfNameLabel = new Label();


            DatePicker datePicker = new DatePicker();
            datePicker.setValue(LocalDate.now());

            Spinner<Integer> hourSpinner = new Spinner<>(0, 23, LocalTime.now().getHour());
            Spinner<Integer> minuteSpinner = new Spinner<>(0, 59, LocalTime.now().getMinute());


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
            grid.add(partOfNameLabel, 0, 2);
            grid.add(partOfNameField, 1, 2);
            grid.add(ticketTypeLabel, 0, 3);
            grid.add(genderComboBox, 1, 3);
            grid.add(new Label("Date:"), 0, 4);
            grid.add(datePicker, 1, 4);

            grid.add(new Label("Hour:"), 0, 5);
            grid.add(hourSpinner, 1, 5);

            grid.add(new Label("Minute:"), 0, 6);
            grid.add(minuteSpinner, 1, 6);

            nodeAndKeys.put(ticketDetailsLabel, "TicketDetailsLabel");
            nodeAndKeys.put(idLabel, "IdLabel");
            nodeAndKeys.put(partOfNameLabel, "PartOfNameLabel");
            nodeAndKeys.put(ticketTypeLabel, "TypeLabel");
            nodeAndKeys.put(genderComboBox, "TypeLabel");


            Application.getMainSceneObj().updateTextUI();
            dialog.getDialogPane().setContent(grid);

            // Обработка нажатия кнопок
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == applyButtonType) {
                    System.out.println("Name: " + partOfNameField.getText());
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

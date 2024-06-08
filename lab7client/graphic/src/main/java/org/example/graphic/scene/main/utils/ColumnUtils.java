package org.example.graphic.scene.main.utils;

import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import org.common.dto.Ticket;
import org.example.graphic.scene.main.utils.factory.CustomCellFactory;
import org.example.graphic.scene.main.utils.factory.NonNullEditingCellFactory;

import java.util.function.Predicate;

public class ColumnUtils {

    public static <T> TableColumn<Ticket, T> createEditableColumn(String title, String property, StringConverter<T> converter,boolean canBeNull) {
        TableColumn<Ticket, T> column = new TableColumn<>(title);
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        column.setCellFactory(new CustomCellFactory<>(converter,canBeNull));
        column.setEditable(true);

        column.setOnEditCommit(event -> {
            T newValue = event.getNewValue();
            Ticket ticket = event.getRowValue();
            try {
                var field = Ticket.class.getDeclaredField(property);
                field.setAccessible(true);
                field.set(ticket, newValue);
                System.out.println("New value committed for " + property + ": " + newValue);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }

        });
        return column;
    }  public static <T> TableColumn<Ticket, T> createEditableColumn(String title, String property, StringConverter<T> converter, Predicate<T> commitCondition) {
        TableColumn<Ticket, T> column = new TableColumn<>(title);
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        column.setCellFactory(new NonNullEditingCellFactory<>(converter));
        column.setEditable(true);
        column.setOnEditCommit(event -> {
            T newValue = event.getNewValue();
            Ticket ticket = event.getRowValue();
            try {
                var field = Ticket.class.getDeclaredField(property);
                field.setAccessible(true);
                field.set(ticket, newValue);
                System.out.println("New value committed for " + property + ": " + newValue);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });

        return column;
    }
}

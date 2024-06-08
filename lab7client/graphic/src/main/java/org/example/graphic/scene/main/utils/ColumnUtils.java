package org.example.graphic.scene.main.utils;

import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import org.common.dto.Ticket;
import org.example.graphic.scene.main.utils.factory.CustomCellFactory;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Predicate;
import java.beans.PropertyDescriptor;

public class ColumnUtils {

    public static <T> TableColumn<Ticket, T> createEditableColumn(String title, String property, StringConverter<T> converter,Class<T> tClass,Predicate<T> predicate) {
        return gnerateTicketTableColumn(title, property, converter, predicate);
    }
    public static <T> TableColumn<Ticket, T> createEditableColumn(String title, String property, StringConverter<T> converter,Predicate<T> predicate) {
        return gnerateTicketTableColumn(title, property, converter, predicate);
    }

    private static <T> TableColumn<Ticket, T> gnerateTicketTableColumn(String title, String property, StringConverter<T> converter, Predicate<T> predicate) {
        TableColumn<Ticket, T> column = new TableColumn<>(title);
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        column.setCellFactory(new CustomCellFactory<>(converter, predicate));
        column.setEditable(true);

        column.setOnEditCommit(event -> {
            T newValue = event.getNewValue();
            Ticket ticket = event.getRowValue();
            PropertyDescriptor pd = null;
            try {

                pd = new PropertyDescriptor(property, ticket.getClass());
                Method setter = pd.getWriteMethod();
                if (setter != null) {
                    setter.invoke(ticket, newValue);
                }
                System.out.println(ticket);
            }
            catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }

        });
        return column;
    }
}

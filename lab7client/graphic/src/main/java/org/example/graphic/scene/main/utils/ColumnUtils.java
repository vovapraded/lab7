package org.example.graphic.scene.main.utils;

import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import org.common.dto.Ticket;
import org.controller.MyController;
import org.example.graphic.localizator.Localizator;
import org.example.graphic.scene.Application;
import org.example.graphic.scene.Popup;
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
        MyController controller = MyController.getInstance();
        column.setOnEditCommit(event -> {
            T newValue = event.getNewValue();
            Ticket ticket = event.getRowValue();
            if (!Application.getLogin().equals(ticket.getCreatedBy())){
                Popup.showError(Localizator.getInstance().getKeyString("NoAccess"));
                event.consume(); // Отменяем событие
                event.getTableView().refresh(); // Обновляем таблицу для возврата к старому значению
                return;
            }
            PropertyDescriptor pd = null;
            try {
                pd = new PropertyDescriptor(property, ticket.getClass());
                Method setter = pd.getWriteMethod();

                if (setter != null) {
                    setter.invoke(ticket, newValue);
                    try {
                        Popup.showDialog(controller.update(event.getRowValue()));
                        Application.getMainSceneObj().getZoomableCartesianPlot().updateMap();
                    } catch (Exception e) {
                        Popup.showError(e.getMessage());
                    }

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

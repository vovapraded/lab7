package org.example.graphic.scene.main.utils.factory;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.common.dto.Ticket;
import org.common.utility.TypesOfArgs;
import org.common.utility.Validator;

import java.util.function.Predicate;

public class CustomCellFactory<T> implements Callback<TableColumn<Ticket, T>, TableCell<Ticket, T>> {

    private final StringConverter<T> converter;
    private final Predicate<T> predicate;


    public CustomCellFactory(StringConverter<T> converter, Predicate<T> predicate) {
        this.converter = converter;

        this.predicate = predicate;
    }

    @Override
    public TableCell<Ticket, T> call(TableColumn<Ticket, T> param) {
        return new CustomEditingCell<>(converter,predicate);
    }
}

package org.example.graphic.scene.main.utils.factory;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.common.dto.Ticket;
import org.common.utility.TypesOfArgs;
import org.common.utility.Validator;

public class CustomCellFactory<T> implements Callback<TableColumn<Ticket, T>, TableCell<Ticket, T>> {

    private final StringConverter<T> converter;
    private final boolean canBeNull;


    public CustomCellFactory(StringConverter<T> converter, boolean canBeNull) {
        this.converter = converter;

        this.canBeNull = canBeNull;
    }

    @Override
    public TableCell<Ticket, T> call(TableColumn<Ticket, T> param) {
        return new CustomEditingCell<>(converter,canBeNull);
    }
}

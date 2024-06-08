package org.example.graphic.scene.main.utils.factory;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.StringConverter;

public class NonNullEditingCellFactory<S, T> implements Callback<TableColumn<S, T>, TableCell<S, T>> {

    private final StringConverter<T> converter;

    public NonNullEditingCellFactory(StringConverter<T> converter) {
        this.converter = converter;
    }

    @Override
    public TableCell<S, T> call(TableColumn<S, T> param) {
        return new TextFieldTableCell<>(converter) {
            @Override
            public void commitEdit(T newValue) {
                if (newValue == null) {
                    cancelEdit();
                } else {
                    super.commitEdit(newValue);
                }
            }

            @Override
            public void startEdit() {
                super.startEdit();
                if (!isEmpty()) {
                    TextField textField = (TextField) getGraphic();
                    if (textField != null) {
                        textField.selectAll();
                    }
                }
            }

            @Override
            public void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(converter.toString(item));
                }
            }
        };
    }
}

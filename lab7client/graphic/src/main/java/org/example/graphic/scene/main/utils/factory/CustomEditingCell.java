package org.example.graphic.scene.main.utils.factory;

import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import org.common.dto.Ticket;

import java.util.function.Predicate;

public class CustomEditingCell<T> extends TableCell<Ticket, T> {
    private TextField textField;
    private final StringConverter<T> converter;
    private final boolean canBeNull;


    public CustomEditingCell(StringConverter<T> converter, boolean canBeNull) {
        this.converter = converter;
        this.canBeNull = canBeNull;
    }

    @Override
    public void startEdit() {
        if (!isEmpty()) {
            super.startEdit();
            createTextField();
            setText(null);
            setGraphic(textField);
            textField.selectAll();
        }
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText(converter.toString(getItem()));
        setGraphic(null);
    }

    @Override
    public void commitEdit(T newValue) {

            super.commitEdit(newValue);
            setText(converter.toString(newValue));
            setGraphic(null);

    }

    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                if (textField != null) {
                    textField.setText(converter.toString(item));
                }
                setText(null);
                setGraphic(textField);


            } else {
                setText(converter.toString(item));
                setGraphic(null);
            }
        }
    }

    private void createTextField() {

        textField = new TextField(converter.toString(getItem()));
        textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
        textField.setOnAction(evt -> {try {
                    if (converter.fromString(textField.getText()) == null && !canBeNull){
                        cancelEdit();
                    }else {
                        commitEdit(converter.fromString(textField.getText()));
                    }
                }catch (Exception e){
                    cancelEdit();
                }
        }
        );
        textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) {
                commitEdit(converter.fromString(textField.getText()));
            }
        });
    }
}

package org.example.graphic.scene.main.utils.factory;

import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import org.common.dto.Ticket;
import org.controller.MyController;

import java.util.function.Predicate;

public class CustomEditingCell<T> extends TableCell<Ticket, T> {
    private TextField textField;
    private final StringConverter<T> converter;
    private final Predicate<T> predicate;


    public CustomEditingCell(StringConverter<T> converter, Predicate<T> predicate) {
        this.converter = converter;
        this.predicate = predicate;
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
    private final MyController controller = MyController.getInstance();

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
                    if (!predicate.test(converter.fromString(textField.getText())) ){
                        cancelEdit();
                    }else {
                        commitEdit(converter.fromString(textField.getText()));
                    }
                }catch (Exception e){
                    e.printStackTrace();
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

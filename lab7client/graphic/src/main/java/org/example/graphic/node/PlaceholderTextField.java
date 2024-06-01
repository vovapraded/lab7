package org.example.graphic.node;

import javafx.scene.control.TextField;
import lombok.Data;
import lombok.NoArgsConstructor;


public class PlaceholderTextField extends TextField {
    private String placeholderText;

    public PlaceholderTextField(String placeholderText) {
        this.placeholderText = placeholderText;
        setPromptText(placeholderText);

        // Слушатель на изменение фокуса
        focusedProperty().addListener((observable, oldValue, newValue) -> handleFocusChange(newValue));

        // Слушатель на изменение текста
        textProperty().addListener((observable, oldValue, newValue) -> handleTextChange(newValue));
    }

    private void handleFocusChange(boolean hasFocus) {
        if (!hasFocus && getText().isEmpty()) {
            setPromptText(placeholderText);
        } else {
            setPromptText("");
        }
    }

    private void handleTextChange(String newText) {
        if (newText.isEmpty() && !isFocused()) {
            setPromptText(placeholderText);
        } else {
            setPromptText("");
        }
    }

    public String getPlaceholderText() {
        return placeholderText;
    }

    public void setPlaceholderText(String placeholderText) {
        this.placeholderText = placeholderText;
        setPromptText(placeholderText);
    }

}

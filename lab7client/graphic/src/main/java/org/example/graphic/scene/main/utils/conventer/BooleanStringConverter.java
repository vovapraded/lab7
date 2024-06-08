package org.example.graphic.scene.main.utils.conventer;

import javafx.util.StringConverter;

public class BooleanStringConverter extends StringConverter<Boolean> {
    @Override
    public String toString(Boolean object) {
        return object == null ? "" : object.toString();
    }

    @Override
    public Boolean fromString(String string) {
        return string.isEmpty() ? null : Boolean.valueOf(string);
    }
}

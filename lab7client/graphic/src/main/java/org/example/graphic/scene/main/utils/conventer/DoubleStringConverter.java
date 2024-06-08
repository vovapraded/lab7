package org.example.graphic.scene.main.utils.conventer;

import javafx.util.StringConverter;

public class DoubleStringConverter extends StringConverter<Double> {
    @Override
    public String toString(Double object) {
        return object == null ? "" : object.toString();
    }

    @Override
    public Double fromString(String string) {
        return string.isEmpty() ? null : Double.valueOf(string);
    }
}

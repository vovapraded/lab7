package org.example.graphic.scene.main.utils.conventer;

import javafx.util.StringConverter;

public class LongStringConverter extends StringConverter<Long> {
    @Override
    public String toString(Long object) {
        return object == null ? "" : object.toString();
    }

    @Override
    public Long fromString(String string) {
        return string.isEmpty() ? null : Long.valueOf(string);
    }
}

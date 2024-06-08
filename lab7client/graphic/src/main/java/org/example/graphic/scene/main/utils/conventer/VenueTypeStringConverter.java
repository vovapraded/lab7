package org.example.graphic.scene.main.utils.conventer;

import javafx.util.StringConverter;
import org.common.dto.VenueType;

public class VenueTypeStringConverter extends StringConverter<VenueType> {
    @Override
    public String toString(VenueType object) {
        return object == null ? "" : object.name();
    }

    @Override
    public VenueType fromString(String string) {
        return string.isEmpty() ? null : VenueType.valueOf(string);
    }
}

package org.example.graphic.scene.main.utils.conventer;

import javafx.util.StringConverter;
import org.common.dto.TicketType;

public class TicketTypeStringConverter extends StringConverter<TicketType> {
    @Override
    public String toString(TicketType object) {
        return object == null ? "" : object.name();
    }

    @Override
    public TicketType fromString(String string) {
            return  string.isEmpty() ? null : TicketType.valueOf(string);

    }
}

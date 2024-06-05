package org.example.graphic.scene.main;

import javafx.scene.paint.Color;
import lombok.Getter;
import org.common.dto.Ticket;
@Getter
public class WrappedTicket {
    private final Ticket ticket;
    private final Color color;

    public WrappedTicket(Ticket ticket, Color color) {
        this.ticket = ticket;
        this.color = color;
    }
}

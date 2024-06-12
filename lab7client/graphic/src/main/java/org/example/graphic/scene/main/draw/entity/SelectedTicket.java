package org.example.graphic.scene.main.draw.entity;

import javafx.scene.paint.Color;
import lombok.Getter;
import org.common.dto.Ticket;
@Getter
public class SelectedTicket extends CommonTicket{
    private Color oldColor;

    public SelectedTicket(Ticket ticket,Color oldColor) {
        super(ticket, Color.WHITE);
        this.oldColor = oldColor;
    }
}

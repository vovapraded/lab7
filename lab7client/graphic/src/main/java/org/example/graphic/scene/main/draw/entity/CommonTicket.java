package org.example.graphic.scene.main.draw.entity;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import lombok.Getter;
import org.common.dto.Ticket;

@Getter
public class CommonTicket extends DrawingTicket  {
    public CommonTicket(Ticket ticket, Color color) {
        super(color,ticket);
    }





}

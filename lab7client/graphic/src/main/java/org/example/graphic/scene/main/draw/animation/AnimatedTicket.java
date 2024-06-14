package org.example.graphic.scene.main.draw.animation;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.common.dto.Ticket;
import org.example.graphic.scene.main.draw.entity.DrawingTicket;

public class AnimatedTicket extends DrawingTicket {


//    private Color color;




    private final double DURATION = 10; // продолжительность анимации в секундах
    private double elapsedTime = 0; // прошедшее время

    public AnimatedTicket( Ticket ticket,Color color) {
        super(color, ticket);
    }

    public void update(double elapsedSeconds) {
        if (elapsedTime < DURATION) {
            elapsedTime += elapsedSeconds;
            // Update the angle
            angle += elapsedSeconds * 90; // Rotate 90 degrees per second
        }
    }


    public boolean isAnimationFinished() {
        return elapsedTime >= DURATION;
    }
}

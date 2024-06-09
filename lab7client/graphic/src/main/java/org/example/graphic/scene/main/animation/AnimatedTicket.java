package org.example.graphic.scene.main.animation;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.common.dto.Ticket;
import org.example.graphic.scene.main.WrappedTicket;

public class AnimatedTicket {
    private WrappedTicket ticket;
    private double x, y, width, height;
    private Color color;
    private double angle = 0;
    private double duration; // продолжительность анимации в секундах
    private double elapsedTime = 0; // прошедшее время

    public AnimatedTicket(WrappedTicket ticket, double x, double y, double width, double height, Color color, double duration) {
        this.ticket = ticket;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
        this.duration = duration;
    }

    public void update(double elapsedSeconds) {
        if (elapsedTime < duration) {
            elapsedTime += elapsedSeconds;
            // Update the angle
            angle += elapsedSeconds * 90; // Rotate 90 degrees per second
        }
    }

    public void draw(GraphicsContext gc) {
        gc.save();

        // Move to the center of the ticket
        gc.translate(x + width / 2, y + height / 2);

        // Rotate around the center
        gc.rotate(angle);

        // Draw the ticket
        gc.setFill(color);
        gc.fillRect(-width / 2, -height / 2, width, height);

        gc.restore();
    }

    public boolean isAnimationFinished() {
        return elapsedTime >= duration;
    }
}

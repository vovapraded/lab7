package org.example.graphic.scene.main.animation;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import lombok.Getter;
import org.common.dto.Ticket;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;

public class AnimationManager {
    @Getter
    private final ArrayList<AnimatedTicket> tickets = new ArrayList<>();

    public void start(Canvas canvas){
        new AnimationTimer() {
            private long lastUpdate = 0;
            private GraphicsContext gc = canvas.getGraphicsContext2D();
            @Override
            public void handle(long now) {
                if (lastUpdate == 0) {
                    lastUpdate = now;
                }

                // Calculate time elapsed
                long elapsedNanos = now - lastUpdate;
                double elapsedSeconds = elapsedNanos / 1_000_000_000.0;
                lastUpdate = now;

                // Update and draw all tickets
                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                Iterator<AnimatedTicket> iterator = tickets.iterator();
                while (iterator.hasNext()) {
                    AnimatedTicket ticket = iterator.next();
                    ticket.update(elapsedSeconds);
                    if (ticket.isAnimationFinished()) {
                        iterator.remove();
                    } else {
                        ticket.draw(gc);
                    }
                }
            }
        }.start();

    }

}

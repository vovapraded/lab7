package org.example.graphic.scene.main.draw.animation;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import org.example.graphic.scene.main.ZoomableCartesianPlot;
import org.example.graphic.scene.main.draw.DrawingManager;
import org.example.graphic.scene.main.draw.entity.DrawingTicket;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AnimationManager {
    private final DrawingManager drawingManager;
    private double zoomFactor;
    private double rectWidth;
    private double rectHeight;
    protected static final Integer RECT_WIDTH_IN_LOCAL = ZoomableCartesianPlot.getRECT_WIDTH_IN_LOCAL();
    protected static final Integer RECT_HEIGHT_IN_LOCAL = ZoomableCartesianPlot.getRECT_HEIGHT_IN_LOCAL();
    protected static final Integer WIDTH = ZoomableCartesianPlot.getWIDTH();
    protected static final Integer HEIGHT = ZoomableCartesianPlot.getHEIGHT();
    protected static final Integer ZERO_Y = ZoomableCartesianPlot.getZERO_Y();
    protected static final Integer ZERO_X = ZoomableCartesianPlot.getZERO_X();
    protected static final Double INITIAL_MAX_X = ZoomableCartesianPlot.getINITIAL_MAX_X();
    protected static final Double INITIAL_MAX_Y = ZoomableCartesianPlot.getINITIAL_MAX_Y();
    List<AnimatedTicket> animatedTickets = new ArrayList<>();

    public AnimationManager(DrawingManager drawingManager) {
        this.drawingManager = drawingManager;
    }

    public void start(Canvas canvas){
        new AnimationTimer() {
            private long lastUpdate = 0;
            private GraphicsContext gc = canvas.getGraphicsContext2D();
            @Override
            public void handle(long now) {
                if (lastUpdate == 0) {
                    lastUpdate = now;
                }

                drawCommonTickets(canvas);
                // Calculate time elapsed
                long elapsedNanos = now - lastUpdate;
                double elapsedSeconds = elapsedNanos / 1_000_000_000.0;
                lastUpdate = now;
                // Update and draw all tickets
                Iterator<AnimatedTicket> iterator = animatedTickets.iterator();
                while (iterator.hasNext()) {
                    AnimatedTicket ticket =  iterator.next();
                    ticket.update(elapsedSeconds);
                    if (ticket.isAnimationFinished()) {
                        iterator.remove();
                    } else {
                        ticket.draw(gc,zoomFactor,rectWidth,rectHeight);
                    }
                }
            }
        }.start();

    }

    private void drawCommonTickets(Canvas canvas) {
        var gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        this.animatedTickets = drawingManager.drawCommonTickets(gc,zoomFactor, rectWidth, rectHeight,  animatedTickets.stream().map(ticket -> (DrawingTicket)ticket).toList());
        if (!animatedTickets.isEmpty()){
            start(canvas);
        }
    }
    public void drawCommonTickets(Canvas canvas,double zoomFactor, List<DrawingTicket> tickets) {
        this.zoomFactor = zoomFactor;
        rectWidth = RECT_WIDTH_IN_LOCAL * WIDTH / INITIAL_MAX_X / 2 / zoomFactor;
        rectHeight = RECT_HEIGHT_IN_LOCAL * HEIGHT / INITIAL_MAX_Y / 2 / zoomFactor;

        var gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        animatedTickets = drawingManager.drawCommonTickets(gc,zoomFactor, rectWidth, rectHeight,tickets);
        if (!animatedTickets.isEmpty()){
            start(canvas);
        }
    }

}

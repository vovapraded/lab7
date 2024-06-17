package org.example.graphic.scene.main.draw.animation;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import org.example.graphic.scene.main.ZoomableCartesianPlot;
import org.example.graphic.scene.main.draw.DrawingManager;
import org.example.graphic.scene.main.draw.entity.CommonTicket;
import org.example.graphic.scene.main.draw.entity.DrawingTicket;

import java.util.ArrayList;
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
    List<DrawingTicket> drawingTickets = new ArrayList<>();

    public AnimationManager(DrawingManager drawingManager) {
        this.drawingManager = drawingManager;
    }
    private volatile boolean isAnimating = false;
    public  void start(Canvas canvas){
        isAnimating=true;
        new AnimationTimer() {
            private long lastUpdate = 0;
            private GraphicsContext gc = canvas.getGraphicsContext2D();
            @Override
            public void handle(long now) {
                if (lastUpdate == 0) {
                    lastUpdate = now;
                }

                var animatedTickets = drawCommonTickets(canvas);
                // Calculate time elapsed
                long elapsedNanos = now - lastUpdate;
                double elapsedSeconds = elapsedNanos / 1_000_000_000.0;
                lastUpdate = now;
                // Update and draw all tickets
                boolean allAnimationsFinished = true;
                for (AnimatedTicket ticket : animatedTickets) {
                    ticket.update(elapsedSeconds);
                    if (!ticket.isAnimationFinished()) {
                        ticket.draw(gc, zoomFactor, rectWidth, rectHeight);
                        allAnimationsFinished = false;
                    }
                    else {
                        ticket.setAngle(0);
                        ticket.draw(gc, zoomFactor, rectWidth, rectHeight);
                        drawingTickets.add(new CommonTicket(ticket.getTicket(),ticket.getColor()));
                    }
                }
                if (allAnimationsFinished) {
                    System.out.println("All animations finished.");
                    isAnimating=false;
                    this.stop();
                }
            }
        }.start();

    }

    private List<AnimatedTicket> drawCommonTickets(Canvas canvas) {
        var gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
       return drawingManager.drawCommonTickets(gc,zoomFactor, rectWidth, rectHeight);
//        if (!animatedTickets.isEmpty()){
//            start(canvas);
//        }
    }
    public void drawCommonTickets(Canvas canvas,double zoomFactor, List<DrawingTicket> tickets) {
        this.zoomFactor = zoomFactor;
        this.drawingTickets = tickets;
        rectWidth = RECT_WIDTH_IN_LOCAL * WIDTH / INITIAL_MAX_X / 2 / zoomFactor;
        rectHeight = RECT_HEIGHT_IN_LOCAL * HEIGHT / INITIAL_MAX_Y / 2 / zoomFactor;

        var gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        var animatedTickets = drawingManager.drawCommonTickets(gc,zoomFactor, rectWidth, rectHeight);
        if (!animatedTickets.isEmpty() && !isAnimating){
            start(canvas);
        }
    }

}

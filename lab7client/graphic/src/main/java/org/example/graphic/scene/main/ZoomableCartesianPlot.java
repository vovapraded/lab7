package org.example.graphic.scene.main;

import javafx.beans.binding.Bindings;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.chart.NumberAxis;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;
import org.example.graphic.scene.Application;
import org.example.graphic.scene.main.draw.animation.AnimationManager;
import org.example.graphic.scene.main.draw.entity.DrawingTicket;
import org.example.graphic.scene.main.storage.TicketStorage;
import org.example.graphic.scene.main.utils.CoordinateConverter;

import java.util.List;

public class ZoomableCartesianPlot {
    private final AnimationManager animationManager;
    @Getter
    private Canvas canvas ;
    @Getter

    private static final Integer RECT_WIDTH_IN_LOCAL = 100;
    @Getter
    private static final Integer RECT_HEIGHT_IN_LOCAL = 100;
    @Getter
    private static final Integer WIDTH = 400;
    @Getter
    private static final Integer HEIGHT = 400;
    @Getter
    private static final Integer ZERO_Y = WIDTH/2;
    @Getter
    private static final Integer ZERO_X = HEIGHT/2;
    @Getter
    private static final Double INITIAL_MAX_X = 1000.0;
    @Getter
    private static final Double INITIAL_MAX_Y = 1000.0;
    private CoordinateConverter converter = CoordinateConverter.getInstance();

    @Setter
    private List<DrawingTicket> tickets;
    private  StackPane layout;
    private Long idOfSelected;
    private Long idOfAnimated;
    private final TicketStorage ticketStorage =         Application.getMainSceneObj().getTicketStorage();

    public ZoomableCartesianPlot(AnimationManager animationManager) {
        this.animationManager = animationManager;
        canvas = new Canvas(WIDTH,HEIGHT);

    }


    public StackPane createMap() {
        var axes = createAxes(1);


        layout = new StackPane(axes,canvas);

        drawTickets(canvas,1);

        layout.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        layout.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        layout.setPadding(new Insets(12));
        layout.setOnScroll(new ZoomHandler());
        layout.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        return layout;
    }



    private Axes createAxes(double zoomFactor) {
        Axes axes = new Axes(
                WIDTH, HEIGHT,
                -INITIAL_MAX_X * zoomFactor, INITIAL_MAX_X * zoomFactor, Math.ceil(2* INITIAL_MAX_X * zoomFactor / 15 ),
                -INITIAL_MAX_Y * zoomFactor, INITIAL_MAX_Y * zoomFactor, Math.ceil(2* INITIAL_MAX_Y * zoomFactor / 15)
        );


        return axes;
    }

    class Axes extends Pane {
        private NumberAxis xAxis;
        private NumberAxis yAxis;

        public Axes(
                int width, int height,
                double xLow, double xHi, double xTickUnit,
                double yLow, double yHi, double yTickUnit
        ) {
            setMinSize(Pane.USE_PREF_SIZE, Pane.USE_PREF_SIZE);
            setPrefSize(width, height);
            setMaxSize(Pane.USE_PREF_SIZE, Pane.USE_PREF_SIZE);

            xAxis = new NumberAxis(xLow, xHi, xTickUnit);
            xAxis.setSide(Side.BOTTOM);
            xAxis.setMinorTickVisible(false);
            xAxis.setPrefWidth(width);
            xAxis.setLayoutY(height / 2);


            yAxis = new NumberAxis(yLow, yHi, yTickUnit);
            yAxis.setSide(Side.LEFT);
            yAxis.setMinorTickVisible(false);
            yAxis.setPrefHeight(height);
            yAxis.layoutXProperty().bind(
                    Bindings.subtract(
                            (width / 2) + 1,
                            yAxis.widthProperty()
                    )
            );





            getChildren().setAll(xAxis, yAxis);
            xAxis.setStyle("-fx-tick-label-fill: rgb(0, 0, 0); axis_color:    black;");
            yAxis.setStyle("-fx-tick-label-fill: rgb(0, 0, 0); axis_color:    black;");


        }

        public NumberAxis getXAxis() {
            return xAxis;
        }

        public NumberAxis getYAxis() {
            return yAxis;
        }
    }



    private class ZoomHandler implements EventHandler<ScrollEvent> {
        private static final double MAX_ZOOM = Double.MAX_VALUE;
        private static final double MIN_ZOOM = 0.01;

        private double zoomFactor = 1;

        @Override
        public void handle(ScrollEvent event) {
            if (event.getDeltaY() == 0) {
                return;
            } else if (event.getDeltaY() < 0) {
                zoomFactor = Math.max(MIN_ZOOM, zoomFactor * 0.9);
            } else if (event.getDeltaY() > 0) {
                zoomFactor = Math.min(MAX_ZOOM, zoomFactor * 1.1);
            }
            drawTickets(canvas,zoomFactor);

            Axes axes = createAxes(zoomFactor);

            Pane parent = (Pane) event.getSource();
            parent.getChildren().setAll(axes,canvas);

        }
    }

    private void drawTickets(Canvas canvas, double zoomFactor) {

        animationManager.drawCommonTickets(canvas,zoomFactor);
    }
//TODO

    public void updateMap() {
        // Очистить графический контекст канваса
        canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
//        tickets.setAll(ticketStorage.getWrappedData());
        // Повторно нарисовать билеты на канвасе с учетом текущего масштаба
        drawTickets(canvas, ((ZoomHandler) layout.getOnScroll()).zoomFactor);
    }
}
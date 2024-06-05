package org.example.graphic.scene.main;

import javafx.beans.binding.Bindings;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.NumberAxis;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class ZoomableCartesianPlot {


    private Canvas canvas ;
    private final Integer RECT_WIDTH_IN_LOCAL = 100;
    private final Integer RECT_HEIGHT_IN_LOCAL = 100;
    private final Integer WIDTH = 400;
    private final Integer HEIGHT = 400;
    private final Integer ZERO_Y = WIDTH/2;
    private final Integer ZERO_X = HEIGHT/2;
    private final Double INITIAL_MAX_X = 1000.0;
    private final Double INITIAL_MAX_Y = 1000.0;



    public StackPane createMap() {
        var axes = createAxes(1);
         canvas = new Canvas(WIDTH,HEIGHT);
        drawTicket(canvas.getGraphicsContext2D(),-4,4,1);

        StackPane layout = new StackPane(canvas,axes);
        layout.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        layout.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        layout.setPadding(new Insets(0));
        layout.setOnScroll(new ZoomHandler());
        layout.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        return layout;
    }
    private void drawTicket(GraphicsContext gc, double coordX, double coordY, double zoomFactor) {
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

        gc.setStroke(Color.BLUE);
        gc.setLineWidth(2);
//        var = centerToUpperLeft(100,100,RECT_WIDTH,RECT_HEIGHT);
        System.out.println(zoomFactor);
//        var pointL=globalToLocal(200,200,zoomFactor);
        var pointG = localToGlobal(coordX, coordY,zoomFactor);
//        System.out.println(pointL.getX()/zoomFactor);
//        System.out.println(pointL.getY()/zoomFactor);
        System.out.println(pointG);
        gc.strokeRect(pointG.getX(), pointG.getY(), RECT_WIDTH_IN_LOCAL*WIDTH/INITIAL_MAX_X/2 /zoomFactor,RECT_HEIGHT_IN_LOCAL*HEIGHT/INITIAL_MAX_Y/2 /zoomFactor);
    //        gc.strokeRect();
    }
    private Point2D globalToLocal(double coordX, double coordY,double zoomFactor) {
        double x = coordX-ZERO_X;
        double y = ZERO_Y - coordY;
        x/=(WIDTH/(INITIAL_MAX_X*zoomFactor)/2);
        y/=(HEIGHT/(INITIAL_MAX_Y*zoomFactor)/2);
        return new Point2D(x, y);
    }
    private Point2D localToGlobal(double coordX, double coordY,double zoomFactor) {
        coordX*=(WIDTH/(INITIAL_MAX_X*zoomFactor)/2);
        coordY*=(HEIGHT/(INITIAL_MAX_Y*zoomFactor)/2);
        double x = coordX+ZERO_X;
        double y = ZERO_Y - coordY;

        return new Point2D(x, y);
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
            drawTicket(canvas.getGraphicsContext2D(),-4,4,zoomFactor);

            Axes axes = createAxes(zoomFactor);

            Pane parent = (Pane) event.getSource();
            parent.getChildren().setAll(canvas,axes);

        }
    }
}
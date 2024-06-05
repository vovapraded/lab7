package org.example.graphic.scene.main;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.NumberAxis;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ZoomableCartesianPlot extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    private Canvas canvas ;
    private final Integer RECT_WIDTH = 100;
    private final Integer RECT_HEIGHT = 100;
    private final Integer WIDTH = 400;
    private final Integer HEIGHT = 300;
    private final Integer ZERO_Y = 400;
    private final Integer ZERO_X = 400;



    @Override
    public void start(final Stage stage) {
        var axes = createAxes(1);
         canvas = new Canvas(800, 800);
        drawShapes(canvas.getGraphicsContext2D(),1);

        StackPane layout = new StackPane(canvas, axes);
        layout.setPadding(new Insets(20));
        layout.setOnScroll(new ZoomHandler());

        stage.setScene(new Scene(layout, Color.rgb(35, 39, 50)));
        stage.show();
    }
    private void drawShapes(GraphicsContext gc,double zoomFactor) {
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

        gc.setStroke(Color.BLUE);
        gc.setLineWidth(2);
//        var = centerToUpperLeft(100,100,RECT_WIDTH,RECT_HEIGHT);
        System.out.println(zoomFactor);
        var pointL=globalToLocal(200,200,zoomFactor);
        var pointG = localToGlobal(pointL.getX()/zoomFactor, pointL.getY()/zoomFactor,zoomFactor);
        System.out.println(pointL.getX()/zoomFactor);
        System.out.println(pointL.getY()/zoomFactor);
        System.out.println(pointG);

        gc.strokeRect(pointG.getX(), pointG.getY(), RECT_WIDTH/zoomFactor,RECT_HEIGHT/zoomFactor);
    //        gc.strokeRect();
    }
    private Point2D globalToLocal(double coordX, double coordY,double zoomFactor) {
        double x = coordX-ZERO_X;
        double y = ZERO_Y - coordY;
        x/=(WIDTH/(8.0*zoomFactor)/2);
        y/=(HEIGHT/(6.0*zoomFactor)/2);
        return new Point2D(x, y);
    }
    private Point2D localToGlobal(double coordX, double coordY,double zoomFactor) {
        coordX*=(WIDTH/(8.0*zoomFactor)/2);
        coordY*=(HEIGHT/(6.0*zoomFactor)/2);
        double x = coordX+ZERO_X;
        double y = ZERO_Y - coordY;

        return new Point2D(x, y);
    }


    private Axes createAxes(double zoomFactor) {
        Axes axes = new Axes(
                WIDTH, HEIGHT,
                -8 * zoomFactor, 8 * zoomFactor, 1,
                -6 * zoomFactor, 6 * zoomFactor, 1
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
        }

        public NumberAxis getXAxis() {
            return xAxis;
        }

        public NumberAxis getYAxis() {
            return yAxis;
        }
    }

//    class Plot extends Pane {
//        public Plot(
//                Function<Double, Double> f,
//                double xMin, double xMax, double xInc,
//                Axes axes
//        ) {
//            Path path = new Path();
//            path.setStroke(Color.ORANGE.deriveColor(0, 1, 1, 0.6));
//            path.setStrokeWidth(2);
//
//            path.setClip(
//                    new Rectangle(
//                            0, 0,
//                            axes.getPrefWidth(),
//                            axes.getPrefHeight()
//                    )
//            );
//
//            double x = xMin;
//            double y = f.apply(x);
//
//            path.getElements().add(
//                    new MoveTo(
//                            mapX(x, axes), mapY(y, axes)
//                    )
//            );
//
//            x += xInc;
//            while (x < xMax) {
//                y = f.apply(x);
//
//                path.getElements().add(
//                        new LineTo(
//                                mapX(x, axes), mapY(y, axes)
//                        )
//                );
//
//                x += xInc;
//            }
//
//            setMinSize(Pane.USE_PREF_SIZE, Pane.USE_PREF_SIZE);
//            setPrefSize(axes.getPrefWidth(), axes.getPrefHeight());
//            setMaxSize(Pane.USE_PREF_SIZE, Pane.USE_PREF_SIZE);
//
//            getChildren().setAll(axes, path);
//        }
//
//        private double mapX(double x, Axes axes) {
//            double tx = axes.getPrefWidth() / 2;
//            double sx = axes.getPrefWidth() /
//                    (axes.getXAxis().getUpperBound() -
//                            axes.getXAxis().getLowerBound());
//
//            return x * sx + tx;
//        }
//
//        private double mapY(double y, Axes axes) {
//            double ty = axes.getPrefHeight() / 2;
//            double sy = axes.getPrefHeight() /
//                    (axes.getYAxis().getUpperBound() -
//                            axes.getYAxis().getLowerBound());
//
//            return -y * sy + ty;
//        }
//    }

    private class ZoomHandler implements EventHandler<ScrollEvent> {
        private static final double MAX_ZOOM = Double.MAX_VALUE;
        private static final double MIN_ZOOM = 0;

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

            Axes axes = createAxes(zoomFactor);
            drawShapes(canvas.getGraphicsContext2D(),zoomFactor);

            Pane parent = (Pane) event.getSource();
            parent.getChildren().setAll(axes,canvas);

        }
    }
}
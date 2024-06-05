package org.example.graphic.scene.main;

import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.NumberAxis;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.common.dto.Ticket;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

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
    private final ObservableList<WrappedTicket> tickets;


    public ZoomableCartesianPlot(ObservableList<WrappedTicket> tickets) {
        this.tickets = tickets;
        canvas = new Canvas(WIDTH,HEIGHT);

    }
    private  Color getRandomColor(){
        Random random = new Random();
        double red = random.nextDouble();
        double green = random.nextDouble();
        double blue = random.nextDouble();
        Color randomColor = new Color(red, green, blue, 0.2);
        return randomColor;
    }

    public StackPane createMap() {
        var axes = createAxes(1);

        drawTickets(canvas.getGraphicsContext2D(),1);

        StackPane layout = new StackPane(canvas,axes);
        layout.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        layout.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        layout.setPadding(new Insets(0));
        layout.setOnScroll(new ZoomHandler());
        layout.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        return layout;
    }
    private void drawTickets(GraphicsContext gc,double zoomFactor){
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        tickets.forEach( ticket -> {
            drawTicket(gc,ticket.getTicket().getCoordinatesX(),ticket.getTicket().getCoordinatesY(),
                    zoomFactor,ticket.getColor(),ticket.getTicket().getName());
        });

    }
    private void drawTicket(GraphicsContext gc, double coordX, double coordY, double zoomFactor,Color color,String text) {
        double rectWidth = RECT_WIDTH_IN_LOCAL * WIDTH / INITIAL_MAX_X / 2 / zoomFactor;
        double rectHeight = RECT_HEIGHT_IN_LOCAL * HEIGHT / INITIAL_MAX_Y / 2 / zoomFactor;
        // Текст
        Font font = new Font("Arial", 6/zoomFactor); // Установите желаемый шрифт и размер
        gc.setFont(font);

        // Получаем границы текста
        Text textNode = new Text(text);
        textNode.setFont(font);
        Bounds textBounds = textNode.getBoundsInLocal();
        var leftUpPoint = localToGlobal(coordX,coordY,zoomFactor);

        // Рассчитываем координаты для центрирования текста внутри прямоугольника
        double textX = leftUpPoint.getX() - (textBounds.getWidth() / 2); // Координата x текста
        double textY = leftUpPoint.getY() + (textBounds.getHeight() / 2); // Координата y текста

        // Отображаем текст
        gc.setFill(Color.BLACK);
        gc.fillText(text, textX, textY);



        coordX-=RECT_WIDTH_IN_LOCAL/2;
        coordY+=RECT_HEIGHT_IN_LOCAL/2;

        gc.setStroke(Color.BLUE);
        gc.setLineWidth(2);
//        var = centerToUpperLeft(100,100,RECT_WIDTH,RECT_HEIGHT);
        System.out.println(zoomFactor);
//        var pointL=globalToLocal(200,200,zoomFactor);
        var pointG = localToGlobal(coordX, coordY,zoomFactor);
//        System.out.println(pointL.getX()/zoomFactor);
//        System.out.println(pointL.getY()/zoomFactor);
        System.out.println(pointG);
        gc.setFill(color);
        gc.fillRect(pointG.getX(), pointG.getY(), rectWidth,rectHeight);
        gc.strokeRect(pointG.getX(), pointG.getY(), rectWidth,rectHeight);




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
            drawTickets(canvas.getGraphicsContext2D(),zoomFactor);

            Axes axes = createAxes(zoomFactor);

            Pane parent = (Pane) event.getSource();
            parent.getChildren().setAll(canvas,axes);

        }
    }
}
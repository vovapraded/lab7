package org.example.graphic.scene.main;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Pagination;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import lombok.Getter;
import lombok.Setter;
import org.common.dto.Ticket;
import org.example.graphic.scene.Application;
import org.example.graphic.scene.Popup;
import org.example.graphic.scene.main.animation.AnimatedTicket;
import org.example.graphic.scene.main.animation.AnimationManager;
import org.example.graphic.scene.main.utils.CoordinateConverter;

import java.util.Objects;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;
public class ZoomableCartesianPlot {
@Setter
    private CreatorTable creatorTable;
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
    private  ObservableList<WrappedTicket> tickets;
    private  StackPane layout;
    private Long idOfSelected;
    private Long idOfAnimated;
    private final TicketStorage ticketStorage =         Application.getMainSceneObj().getTicketStorage();


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


        layout = new StackPane(axes,canvas);
        drawTickets(canvas.getGraphicsContext2D(),1);

        layout.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        layout.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        layout.setPadding(new Insets(12));
        layout.setOnScroll(new ZoomHandler());
        layout.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        return layout;
    }
    private void drawTickets(GraphicsContext gc,double zoomFactor){
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());


//        var animatedTickets = tickets.stream().map(ticket -> new AnimatedTicket(ticket.getTicket(),ticket.getTicket().getCoordinatesX(),ticket.getTicket().getCoordinatesY(),rectWidth,rectHeight,getRandomColor(),1000)).toList();
//        AnimationManager animationManager = new AnimationManager();
//        animationManager.getTickets().addAll(animatedTickets);
//        animationManager.start(canvas);
        double rectWidth = RECT_WIDTH_IN_LOCAL * WIDTH / INITIAL_MAX_X / 2 / zoomFactor;
        double rectHeight = RECT_HEIGHT_IN_LOCAL * HEIGHT / INITIAL_MAX_Y / 2 / zoomFactor;
        AtomicReference<WrappedTicket> selectedTicket = new AtomicReference<>();
        tickets.forEach( ticket -> {
            if (!Objects.equals(idOfSelected, ticket.getTicket().getId())){
                ticket.draw(gc,zoomFactor,ticket,0,null,rectWidth,rectHeight);
            } else {
                selectedTicket.set(ticket);
            }
            });
        if (selectedTicket.get()!=null ){
            var ticket =selectedTicket.get();
          ticket.draw(gc,zoomFactor,ticket,0,Color.WHITE,rectWidth,rectHeight);
        }
        setCanvasOnClick(zoomFactor,rectWidth,rectHeight);

    }
    private void setCanvasOnClick(double zoomFactor,double rectWidth,double rectHeight) {
        var pagination = creatorTable.getPagination();
        var ticketTable = creatorTable.getTable();
        canvas.setOnMouseClicked(event -> {
            System.out.println("A");
            double mouseX = event.getX();
            double mouseY = event.getY();
            var flag = false;
            // Проверяем, попадает ли точка клика в какой-либо прямоугольник
            for (WrappedTicket wrappedTicket : tickets) {
                var ticket = wrappedTicket.getTicket();
                var point = converter.localToGlobal(ticket.getCoordinatesX(),ticket.getCoordinatesY(),zoomFactor);
                double rectX = point.getX();
                double rectY = point.getY();
                if (mouseX >= rectX-rectWidth/2 && mouseX <= rectX + rectWidth/2 &&
                        mouseY >= rectY - rectHeight/2 && mouseY <= rectY + rectHeight/2) {
                    // Найден прямоугольник, на который было нажатие
                    // Вы можете выполнить необходимые действия здесь
                    int index = creatorTable.getSortedData().indexOf(wrappedTicket.getTicket());
                    System.out.println(index);
                    if (index >= 0) {
                        handleRectangleClick(wrappedTicket.getTicket(),pagination,index);
                    }
                    break; // Мы нашли прямоугольник, больше проверять не нужно
                }

            }
            if (!flag){
                ticketTable.getSelectionModel().clearSelection();
            }
        });
    }
    private void drawTicket(GraphicsContext gc, double zoomFactor, WrappedTicket wrappedTicket,double rectWidth,double rectHeight) {

        var ticket = wrappedTicket.getTicket();
        // Текст
        Font font = new Font("Arial", 6/zoomFactor); // Установите желаемый шрифт и размер
        gc.setFont(font);

        // Получаем границы текста
        Text textNode = new Text(ticket.getName());
        textNode.setFont(font);
        Bounds textBounds = textNode.getBoundsInLocal();

        var coordX = ticket.getCoordinatesX();
        var coordY = ticket.getCoordinatesY();

        var leftUpPoint = converter.localToGlobal(coordX,coordY,zoomFactor);

        // Рассчитываем координаты для центрирования текста внутри прямоугольника
        double textX = leftUpPoint.getX() - (textBounds.getWidth() / 2); // Координата x текста
        double textY = leftUpPoint.getY() + (textBounds.getHeight() / 2); // Координата y текста

        // Отображаем текст





        coordX-= (double) RECT_WIDTH_IN_LOCAL /2;
        coordY+=RECT_HEIGHT_IN_LOCAL/2;

        gc.setStroke(Color.BLUE);
        gc.setLineWidth(2);
//        var = centerToUpperLeft(100,100,RECT_WIDTH,RECT_HEIGHT);
        System.out.println(zoomFactor);
//        var pointL=globalToLocal(200,200,zoomFactor);
        var pointG = converter.localToGlobal(coordX, coordY,zoomFactor);
//        System.out.println(pointL.getX()/zoomFactor);
//        System.out.println(pointL.getY()/zoomFactor);
        System.out.println(pointG);
        gc.setFill(wrappedTicket.getColor());
        var x = pointG.getX();
        var y = pointG.getY();
        gc.strokeRect(x,y,rectWidth,rectHeight);
        gc.fillRect(x,y,rectWidth,rectHeight);

        gc.setFill(Color.BLACK);
        gc.fillText(ticket.getName(), textX, textY);





    }
    private void handleRectangleClick(Ticket ticket, Pagination pagination, int index) {
        index += 1;
        System.out.println("ABOBA");
        var itemsPerPage = CreatorTable.getROWS_PER_PAGE();
        var indexOfPage = (int) Math.ceil((double) index / itemsPerPage )-1;

        pagination.setCurrentPageIndex(indexOfPage);

        // Найдите индекс билета в списке таблиц
        System.out.println(index);
        System.out.println(indexOfPage);

        int finalIndex = (index-1 )% itemsPerPage;
        Platform.runLater(() -> {
            creatorTable.getTable().getSelectionModel().clearAndSelect((finalIndex));
        });

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
            parent.getChildren().setAll(axes,canvas);

        }
    }
    public void setIdOfSelected(Long id){
        this.idOfSelected = id;
        var zoomHandler = (ZoomHandler)layout.getOnScroll();
        drawTickets(canvas.getGraphicsContext2D(),zoomHandler.zoomFactor);
    }
    public void updateMap() {
        // Очистить графический контекст канваса
        canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        tickets.setAll(ticketStorage.getWrappedData());
        // Повторно нарисовать билеты на канвасе с учетом текущего масштаба
        drawTickets(canvas.getGraphicsContext2D(), ((ZoomHandler) layout.getOnScroll()).zoomFactor);
    }
}
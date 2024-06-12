package org.example.graphic.scene.main.draw;

import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Pagination;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;
import org.common.dto.Ticket;
import org.example.graphic.scene.main.CreatorTable;
import org.example.graphic.scene.main.ZoomableCartesianPlot;
import org.example.graphic.scene.main.draw.animation.AnimatedTicket;
import org.example.graphic.scene.main.draw.entity.CommonTicket;
import org.example.graphic.scene.main.draw.entity.DrawingTicket;
import org.example.graphic.scene.main.draw.entity.SelectedTicket;
import org.example.graphic.scene.main.utils.CoordinateConverter;

import java.util.*;

public class DrawingManager {
    @Setter
    private CreatorTable creatorTable;
    private final CoordinateConverter converter = CoordinateConverter.getInstance();
    @Getter
    private  List<DrawingTicket> sortedTickets;
    protected static final Integer RECT_WIDTH_IN_LOCAL = ZoomableCartesianPlot.getRECT_WIDTH_IN_LOCAL();
    protected static final Integer RECT_HEIGHT_IN_LOCAL = ZoomableCartesianPlot.getRECT_HEIGHT_IN_LOCAL();
    protected static final Integer WIDTH = ZoomableCartesianPlot.getWIDTH();
    protected static final Integer HEIGHT = ZoomableCartesianPlot.getHEIGHT();
    protected static final Integer ZERO_Y = ZoomableCartesianPlot.getZERO_Y();
    protected static final Integer ZERO_X = ZoomableCartesianPlot.getZERO_X();
    protected static final Double INITIAL_MAX_X = ZoomableCartesianPlot.getINITIAL_MAX_X();
    protected static final Double INITIAL_MAX_Y = ZoomableCartesianPlot.getINITIAL_MAX_Y();




    public List<AnimatedTicket> drawCommonTickets(GraphicsContext gc, double zoomFactor,double rectWidth,double rectHeight,List<DrawingTicket> tickets){
         sortedTickets = tickets.stream().sorted().toList();
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
//        var animatedTickets = tickets.stream().map(ticket -> new AnimatedTicket(ticket.getTicket(),ticket.getTicket().getCoordinatesX(),ticket.getTicket().getCoordinatesY(),rectWidth,rectHeight,getRandomColor(),1000)).toList();
//        AnimationManager animationManager = new AnimationManager();
//        animationManager.getTickets().addAll(animatedTickets);
//        animationManager.start(canvas);

        final double zoomFactorFinal =zoomFactor;
        List<AnimatedTicket> animatedTickets = sortedTickets.stream().filter( ticket -> {
            if (ticket instanceof CommonTicket && !(ticket instanceof SelectedTicket)){
                ((CommonTicket)ticket).draw(gc,zoomFactorFinal,rectWidth,rectHeight);
            }else if (ticket instanceof SelectedTicket){
                ((SelectedTicket)ticket).draw(gc,zoomFactorFinal,rectWidth,rectHeight);
            }
            setCanvasOnClick(zoomFactor,rectWidth,rectHeight,gc);
            return ticket instanceof AnimatedTicket;
        }).map(ticket -> (AnimatedTicket)ticket).toList();
        return animatedTickets;
    }
    private void setCanvasOnClick(double zoomFactor,double rectWidth,double rectHeight,GraphicsContext gc) {
        var canvas = gc.getCanvas();
        var pagination = creatorTable.getPagination();
        var ticketTable = creatorTable.getTable();
        canvas.setOnMouseClicked(event -> {
            System.out.println("A");
            double mouseX = event.getX();
            double mouseY = event.getY();
            var flag = false;
            // Проверяем, попадает ли точка клика в какой-либо прямоугольник
            for (DrawingTicket wrappedTicket : sortedTickets) {
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
    private  Color getRandomColor(){
        Random random = new Random();
        double red = random.nextDouble();
        double green = random.nextDouble();
        double blue = random.nextDouble();
        Color randomColor = new Color(red, green, blue, 0.2);
        return randomColor;
    }

}

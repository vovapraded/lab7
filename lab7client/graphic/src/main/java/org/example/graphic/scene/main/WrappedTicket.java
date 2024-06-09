package org.example.graphic.scene.main;

import javafx.geometry.Bounds;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import lombok.Getter;
import lombok.Setter;
import org.common.dto.Ticket;
import org.example.graphic.scene.Application;
import org.example.graphic.scene.main.utils.CoordinateConverter;

@Getter
public class WrappedTicket {
    private final Ticket ticket;
    @Setter
    private  Color color;

    private static final Integer RECT_WIDTH_IN_LOCAL = ZoomableCartesianPlot.getRECT_WIDTH_IN_LOCAL();
    private static final Integer RECT_HEIGHT_IN_LOCAL = ZoomableCartesianPlot.getRECT_HEIGHT_IN_LOCAL();
    private static final Integer WIDTH = ZoomableCartesianPlot.getWIDTH();
    private static final Integer HEIGHT = ZoomableCartesianPlot.getHEIGHT();
    private static final Integer ZERO_Y = ZoomableCartesianPlot.getZERO_Y();
    private static final Integer ZERO_X = ZoomableCartesianPlot.getZERO_X();
    private static final Double INITIAL_MAX_X = ZoomableCartesianPlot.getINITIAL_MAX_X();
    private static final Double INITIAL_MAX_Y = ZoomableCartesianPlot.getINITIAL_MAX_Y();
    private CoordinateConverter converter = CoordinateConverter.getInstance();


    public WrappedTicket(Ticket ticket, Color color) {
        this.ticket = ticket;
        this.color = color;
    }
    public void draw(GraphicsContext gc, double zoomFactor, WrappedTicket wrappedTicket,double angle,Color color,double rectWidth,double rectHeight) {

        gc.save();

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
        if (color!=null){
            gc.setFill(color);
        }else {
            gc.setFill(this.color);
        }
        var x = pointG.getX();
        var y = pointG.getY();
        gc.translate(x + rectWidth / 2, y + rectHeight / 2);
        gc.rotate(angle);
        gc.translate(-(x + rectWidth / 2), -(y + rectHeight / 2));
        gc.strokeRect(x,y,rectWidth,rectHeight);
        gc.fillRect(x,y,rectWidth,rectHeight);
        gc.setFill(Color.BLACK);
        gc.fillText(ticket.getName(), textX, textY);
        gc.restore();
    }
}

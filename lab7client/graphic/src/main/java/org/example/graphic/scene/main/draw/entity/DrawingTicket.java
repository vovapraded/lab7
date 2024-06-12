package org.example.graphic.scene.main.draw.entity;

import javafx.geometry.Bounds;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import lombok.Getter;
import lombok.Setter;
import org.common.dto.Ticket;
import org.example.graphic.scene.main.ZoomableCartesianPlot;
import org.example.graphic.scene.main.utils.CoordinateConverter;
@Getter
public abstract class DrawingTicket implements Comparable<DrawingTicket>{
    @Setter
    protected  Color color;
    protected Ticket ticket;
    protected double angle;

    public DrawingTicket(Color color, Ticket ticket) {
        this.color = color;
        this.ticket = ticket;
    }

    protected double x, y, width, height;
    protected static final Integer RECT_WIDTH_IN_LOCAL = ZoomableCartesianPlot.getRECT_WIDTH_IN_LOCAL();
    protected static final Integer RECT_HEIGHT_IN_LOCAL = ZoomableCartesianPlot.getRECT_HEIGHT_IN_LOCAL();
    protected static final Integer WIDTH = ZoomableCartesianPlot.getWIDTH();
    protected static final Integer HEIGHT = ZoomableCartesianPlot.getHEIGHT();
    protected static final Integer ZERO_Y = ZoomableCartesianPlot.getZERO_Y();
    protected static final Integer ZERO_X = ZoomableCartesianPlot.getZERO_X();
    protected static final Double INITIAL_MAX_X = ZoomableCartesianPlot.getINITIAL_MAX_X();
    protected static final Double INITIAL_MAX_Y = ZoomableCartesianPlot.getINITIAL_MAX_Y();
    protected CoordinateConverter converter = CoordinateConverter.getInstance();

    public void draw(GraphicsContext gc, double zoomFactor, double rectWidth, double rectHeight) {

        gc.save();
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
//        if (color!=null){
            gc.setFill(color);
//        }else {
//            gc.setFill(this.color);
//        }
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

    @Override
    public int compareTo(DrawingTicket o) {

        var thisCount = 0;
        var otherCount = 0;
        if (this instanceof CommonTicket && !(this instanceof SelectedTicket)){
            thisCount = 3;
        } else if (this instanceof SelectedTicket) {
            thisCount = 2;
        }else {
            thisCount = 1;
        }
        if (o instanceof CommonTicket && !(o instanceof SelectedTicket)){
            otherCount = 3;
        } else if (o instanceof SelectedTicket) {
            otherCount = 2;
        }else {
            otherCount = 1;
        }
        var res = otherCount - thisCount;
        if (res == 0){
            return 1;
        }
        return res;
    }
}

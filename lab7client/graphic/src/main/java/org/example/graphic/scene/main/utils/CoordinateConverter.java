package org.example.graphic.scene.main.utils;

import javafx.geometry.Point2D;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.graphic.scene.Application;
import org.example.graphic.scene.main.MainScene;
import org.example.graphic.scene.main.ZoomableCartesianPlot;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CoordinateConverter {
    @Getter
    private static final CoordinateConverter instance = new CoordinateConverter();
    private final Integer WIDTH = ZoomableCartesianPlot.getWIDTH();
    private final Integer HEIGHT = ZoomableCartesianPlot.getHEIGHT();
    private final Integer ZERO_Y = ZoomableCartesianPlot.getZERO_Y();
    private final Integer ZERO_X = ZoomableCartesianPlot.getZERO_X();
    private final Double INITIAL_MAX_X = ZoomableCartesianPlot.getINITIAL_MAX_X();
    private final Double INITIAL_MAX_Y = ZoomableCartesianPlot.getINITIAL_MAX_Y();



    public  Point2D globalToLocal(double coordX, double coordY, double zoomFactor) {
        double x = coordX-ZERO_X;
        double y = ZERO_Y - coordY;
        x/=(WIDTH/(INITIAL_MAX_X*zoomFactor)/2);
        y/=(HEIGHT/(INITIAL_MAX_Y*zoomFactor)/2);
        return new Point2D(x, y);
    }
    public   Point2D localToGlobal(double coordX, double coordY,double zoomFactor) {
        coordX*=(WIDTH/(INITIAL_MAX_X*zoomFactor)/2);
        coordY*=(HEIGHT/(INITIAL_MAX_Y*zoomFactor)/2);
        double x = coordX+ZERO_X;
        double y = ZERO_Y - coordY;

        return new Point2D(x, y);
    }

}

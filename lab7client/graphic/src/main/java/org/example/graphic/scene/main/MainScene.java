package org.example.graphic.scene.main;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.util.Callback;
import lombok.SneakyThrows;
import org.common.dto.*;
import org.example.graphic.node.TableColumnAdapter;
import org.example.graphic.scene.Application;
import org.example.graphic.scene.MyScene;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

public class MainScene extends MyScene {


    private final   TicketStorage ticketStorage;


    public MainScene(TicketStorage ticketStorage) {
        this.ticketStorage = ticketStorage;
    }


    public void createMainScene() {

        BorderPane root = new BorderPane();
        Button logOut = new Button("");
        Text loginLabel1 = new Text();
        Text loginLabel2 = new Text(": " + Application.getLogin());
        HBox username = new HBox(loginLabel1, loginLabel2);
        VBox userBox = new VBox(username, logOut);
        HBox userBoxWrapper = new HBox(userBox);
        userBoxWrapper.setAlignment(Pos.TOP_RIGHT);
        userBox.setPadding(new Insets(20));
        userBox.setSpacing(10);

        createChangeLocaleBox();
        nodeAndPropertyKeys.put(logOut, "LogOut");
        nodeAndPropertyKeys.put(loginLabel1, "LoginLabel");
        logOut.setOnAction(e -> Application.switchToLoginScene());
        root.setTop(userBoxWrapper);

        HBox changeLocaleWrapper = new HBox(changeLocaleLabel);
        changeLocaleWrapper.setPadding(new Insets(20));
        changeLocaleWrapper.setAlignment(Pos.BOTTOM_RIGHT);
        root.setBottom(changeLocaleWrapper);
        ZoomableCartesianPlot zoomableCartesianPlot = new ZoomableCartesianPlot(ticketStorage.getWrappedData());
        root.setCenter(zoomableCartesianPlot.createMap());
//        ZoomableCanvasWithAxes zoomableCanvasWithAxes = new ZoomableCanvasWithAxes();
//        root.setCenter(zoomableCanvasWithAxes.getMainPane());

//        CreatorMap creatorMap = new CreatorMap(ticketStorage);
//        var scrollPane = creatorMap.createGraphicContext();
//        root.setCenter(scrollPane);

//        CreatorTable creatorTable = new CreatorTable(ticketStorage,this);
//        var pagination = creatorTable.init();
//        root.setCenter(pagination);
//        table = createTable();

        scene = new Scene(root, 900, 800);

        

    }





}

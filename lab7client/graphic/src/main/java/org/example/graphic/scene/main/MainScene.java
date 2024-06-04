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

public class MainScene extends MyScene {
    private SimpleDoubleProperty scaleProperty = new SimpleDoubleProperty(1.0);
    private Canvas canvas;
    private static final int ROWS_PER_PAGE = 10; // Количество строк на страницу
    private static final double AXIS_WIDTH = 50;
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

        var scrollPane = createGraphicContext();
//        drawTickets(canvas.getGraphicsContext2D(), sortedData);
        CreatorTable creatorTable = new CreatorTable(ticketStorage,this);
        var pagination = creatorTable.init();
        root.setCenter(pagination);
//        table = createTable();

        scene = new Scene(root, 800, 600);

        

    }



    public ScrollPane createGraphicContext(){
        canvas = new Canvas(800, 800);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        drawAxes(gc);

//        drawShapes(gc);
        StackPane stackPane = new StackPane(canvas);
        ScrollPane scrollPane = new ScrollPane(stackPane);
        scrollPane.setPannable(true); // Enable panning
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        // Adding scale transform to the canvas
        Scale scale = new Scale(1, 1);
        canvas.getTransforms().add(scale);

        // Binding the scale property to the scale transform
        scale.xProperty().bind(scaleProperty);
        scale.yProperty().bind(scaleProperty);

        // Handling zoom events
        scrollPane.addEventFilter(ScrollEvent.SCROLL, event -> {
            if (event.isControlDown()) {
                if (event.getDeltaY() > 0) {
                    scaleProperty.set(scaleProperty.get() * 1.1);
                } else {
                    scaleProperty.set(scaleProperty.get() / 1.1);
                }
                event.consume();
            }
        });
        return scrollPane;
    }

//    private void drawShapes(GraphicsContext gc) {
//        gc.strokeRect(50, 50, 700, 700);
//        gc.strokeLine(50, 50, 750, 750);
//        gc.strokeLine(750, 50, 50, 750);
//    }
private void drawAxes(GraphicsContext gc) {
    double width = gc.getCanvas().getWidth();
    double height = gc.getCanvas().getHeight();

    gc.setStroke(Color.GRAY);
    gc.setLineWidth(1);

    // Draw X and Y axes
    gc.strokeLine(0, height / 2, width, height / 2); // X axis
    gc.strokeLine(width / 2, 0, width / 2, height); // Y axis

    // Draw ticks and labels for X axis
    for (int i = 0; i < width; i += 50) {
        gc.strokeLine(i, height / 2 - 5, i, height / 2 + 5);
        if (i != width / 2) {
            gc.setFont(new Font(10));
            gc.setFill(Color.BLACK);
            gc.fillText(String.valueOf(i - width / 2), i, height / 2 + 15);
        }
    }

    // Draw ticks and labels for Y axis
    for (int i = 0; i < height; i += 50) {
        gc.strokeLine(width / 2 - 5, i, width / 2 + 5, i);
        if (i != height / 2) {
            gc.setFont(new Font(10));
            gc.setFill(Color.BLACK);
            gc.fillText(String.valueOf(height / 2 - i), width / 2 + 10, i);
        }
    }
}

    private void drawTickets(GraphicsContext gc, SortedList<Ticket> tickets) {
        // Очищаем канвас
        gc.clearRect(0, 0, 800, 600);

        // Рисуем каждый билет
        for (Ticket ticket : tickets) {
            // Настройка цвета обводки и заливки
            gc.setFill(Color.BLUE);
            gc.setStroke(Color.RED);
            gc.setLineWidth(5);

            // Рисование прямоугольника с закругленными углами
            double x = ticket.getCoordinatesX();
            double y = ticket.getCoordinatesY();
            double width = 50;
            double height = 50;
            double arcWidth = 5;  // Радиус закругления по ширине
            double arcHeight = 5; // Радиус закругления по высоте

            // Закрашенный прямоугольник с закругленными углами
//            gc.fillRoundRect(x, y, width, height, arcWidth, arcHeight);

            // Обведенный прямоугольник с закругленными углами
            gc.strokeRoundRect(x, y, width, height, arcWidth, arcHeight);
            gc.setLineWidth(10);

            gc.setLineCap(StrokeLineCap.ROUND);
            gc.fillText(ticket.getName(), ticket.getCoordinatesX() + 5, ticket.getCoordinatesY() + 25);

        }
    }

}

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
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.common.dto.*;
import org.example.graphic.node.TableColumnAdapter;
import org.example.graphic.scene.Application;
import org.example.graphic.scene.MyScene;
import org.example.graphic.scene.main.command.filter.FilterPanel;
import org.example.graphic.scene.main.command.insert.InsertPanel;
import org.example.graphic.scene.main.command.remove.RemoveButton;
import org.example.graphic.scene.main.draw.DrawingManager;
import org.example.graphic.scene.main.draw.animation.AnimationManager;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
@Getter
public class MainScene extends MyScene {


    private final   TicketStorage ticketStorage;
    private CreatorTable creatorTable;
    private ZoomableCartesianPlot zoomableCartesianPlot;
    private HBox tableAndPlot;
    @Setter
    private Pagination pagination;
    private StackPane map;
    private BorderPane root;


    public MainScene(TicketStorage ticketStorage) {
        this.ticketStorage = ticketStorage;
        filterPanel = new FilterPanel(ticketStorage);
    }

    private FilterPanel filterPanel ;
    private InsertPanel insertPanel = new InsertPanel();



    public void createMainScene() {

         root = new BorderPane();
        Button logOut = new Button("");
        Text loginLabel1 = new Text();
        Text loginLabel2 = new Text(": " + Application.getLogin());
        HBox username = new HBox(loginLabel1, loginLabel2);
        VBox userBox = new VBox(username, logOut);
        BorderPane stripOnTop = new BorderPane();
        stripOnTop.setRight(userBox);
        HBox commandPanel = new HBox();
        commandPanel.setSpacing(10);
        stripOnTop.setCenter(commandPanel);
        var removeButtonObj = new RemoveButton();

        commandPanel.getChildren().addAll(filterPanel.createButton(nodeAndPropertyKeys),
               insertPanel.createButton(nodeAndPropertyKeys),
                removeButtonObj.createButton()

        );
        commandPanel.setAlignment(Pos.CENTER);
        userBox.setPadding(new Insets(20));
        userBox.setSpacing(10);

        createChangeLocaleBox();
        nodeAndPropertyKeys.put(logOut, "LogOut");
        nodeAndPropertyKeys.put(loginLabel1, "LoginLabel");
        logOut.setOnAction(e -> Application.switchToLoginScene());
        root.setTop(stripOnTop);

        HBox changeLocaleWrapper = new HBox(changeLocaleLabel);
        changeLocaleWrapper.setPadding(new Insets(20));
        changeLocaleWrapper.setAlignment(Pos.BOTTOM_RIGHT);
        root.setBottom(changeLocaleWrapper);

        creatorTable = new CreatorTable(ticketStorage,this);
        var drawingManager = new DrawingManager(ticketStorage,creatorTable);
        var animationManager = new AnimationManager(drawingManager);

          zoomableCartesianPlot = new ZoomableCartesianPlot(animationManager);
        creatorTable.setZoomableCartesianPlot(zoomableCartesianPlot);

         pagination = creatorTable.init();
         map = zoomableCartesianPlot.createMap();

         tableAndPlot = new HBox(pagination,map);
         tableAndPlot.setMaxWidth(Double.MAX_VALUE);
        tableAndPlot.setPrefWidth(Double.MAX_VALUE);
        tableAndPlot.setAlignment(Pos.CENTER);
        tableAndPlot.setPadding(new Insets(10,30,10,10));

        root.setCenter(tableAndPlot);





        scene = new Scene(root, 900, 800);

        

    }
    private void updateDialogButtonsText(){
        if (insertPanel.getDialog()!=null){
            insertPanel.updateButtons();
        }
        if (filterPanel.getDialog()!=null){
            filterPanel.updateButtons();
        }
    }

    @Override
    public void updateTextUI() {
        updateText();
        updateDialogButtonsText();
    }
    public void updatePagination() {
      tableAndPlot.getChildren().setAll(pagination,map);
    }
}

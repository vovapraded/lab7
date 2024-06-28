package org.example.graphic.scene.main;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import lombok.Getter;
import lombok.Setter;
import org.example.graphic.scene.Application;
import org.example.graphic.scene.MyScene;
import org.example.graphic.scene.main.command.filter.FilterPanel;
import org.example.graphic.scene.main.command.insert.InsertPanel;
import org.example.graphic.scene.main.command.remove.RemoveButton;
import org.example.graphic.scene.main.draw.DrawingManager;
import org.example.graphic.scene.main.draw.animation.AnimationManager;
import org.example.graphic.scene.main.storage.TicketStorage;

@Getter
public class MainScene extends MyScene {


    private final TicketStorage ticketStorage;
    private CreatorTable creatorTable;
    private ZoomableCartesianPlot zoomableCartesianPlot;
    private HBox tableAndPlot;
    @Setter
    private Pagination pagination;
    private StackPane map;
    private BorderPane root;
    private DrawingManager drawingManager;
    private AnimationManager animationManager;


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
        drawingManager = new DrawingManager(ticketStorage,creatorTable);
        animationManager = new AnimationManager(drawingManager);

          zoomableCartesianPlot = new ZoomableCartesianPlot(animationManager);
        creatorTable.setZoomableCartesianPlot(zoomableCartesianPlot);

         pagination = creatorTable.init();
         map = zoomableCartesianPlot.createMap();

         tableAndPlot = new HBox(pagination,map);
         tableAndPlot.setMaxWidth(Double.MAX_VALUE);
        tableAndPlot.setPrefWidth(Double.MAX_VALUE);
        tableAndPlot.setAlignment(Pos.TOP_CENTER);
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

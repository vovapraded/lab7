package org.example.graphic.scene;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainScene extends MyScene  {
    public void createMainScene() {
        GridPane root = new GridPane();
        root.setAlignment(Pos.BOTTOM_RIGHT);
        changeLocale = createChangeLocaleBox();
        Button logOut = new Button("");
        nodeAndPropertyKeys.put(logOut,"LogOut");
        logOut.setOnAction(e -> Application.switchToLoginScene());
        root.add(logOut,0,0);
        root.add(changeLocale,0,1);

        scene = new Scene(root, 800, 600);
        System.out.println(localizator);
    }


}

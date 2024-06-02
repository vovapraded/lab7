package org.example.graphic.scene;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class MainScene extends MyScene  {
    public void createMainScene() {
        BorderPane root = new BorderPane();
        Button logOut = new Button("");
        Text loginLabel1 = new Text();
        Text loginLabel2 = new Text(": "+Application.getLogin());
        HBox username = new HBox(loginLabel1,loginLabel2);
//        loginLabel1.textProperty().addListener(() ->
//                textFlow);
        VBox userBox = new VBox(username,logOut);
        HBox userBoxWrapper = new HBox(userBox); // Обертка для userBox
        userBoxWrapper.setAlignment(Pos.TOP_RIGHT);
        userBox.setPadding(new Insets(20));
        userBox.setSpacing(10);

        createChangeLocaleBox();
//        changeLocale.setPadding(new Insets(20));
        nodeAndPropertyKeys.put(logOut,"LogOut");
        nodeAndPropertyKeys.put(loginLabel1,"LoginLabel");
        logOut.setOnAction(e -> Application.switchToLoginScene());
        root.setTop(userBoxWrapper);
        HBox changeLocaleWrapper = new HBox(changeLocaleLabel); // Обертка для userBox
        changeLocaleWrapper.setPadding(new Insets(20));
        changeLocaleWrapper.setAlignment(Pos.BOTTOM_RIGHT);

        root.setBottom(changeLocaleWrapper);

        scene = new Scene(root, 800, 600);
        System.out.println(localizator);
    }
    private void createTable(){

    }


}

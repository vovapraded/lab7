package org.example.graphic.scene;

import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.example.graphic.scene.util.BackgroundClickableMaker;

public class Application extends javafx.application.Application {
    @Getter
    private static  Stage primaryStage;
    private static final BackgroundClickableMaker backgroundClickableMaker = new BackgroundClickableMaker();
    private static  LoginScene loginSceneObj;
    private static  MainScene mainSceneObj;
    @Getter @Setter
    private static String login;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        launchLoginStage();
    }
    private void launchLoginStage(){
         loginSceneObj = new LoginScene();
         loginSceneObj.createLoginScene();
        backgroundClickableMaker.make(loginSceneObj.scene);
        primaryStage.setScene(loginSceneObj.scene);
        // Устанавливаем заголовок окна
        primaryStage.setTitle("Login");
        loginSceneObj.updateTexts();
        primaryStage.show();
    }
    public static void switchToMainScene() {
        //подумать над мувом
        if (mainSceneObj == null) {
            mainSceneObj = new MainScene();
            mainSceneObj.createMainScene();
            mainSceneObj.updateValueChangeLocale();
            mainSceneObj.updateTexts();
            backgroundClickableMaker.make(mainSceneObj.scene);
        }
        primaryStage.setTitle("Main");
        mainSceneObj.updateValueChangeLocale();
        mainSceneObj.updateTexts();
        primaryStage.setScene(mainSceneObj.scene);
//        primaryStage.setFullScreen(true);
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        // Настройка размеров и позиции окна
        primaryStage.setX(screenBounds.getMinX());
        primaryStage.setY(screenBounds.getMinY());
        primaryStage.setWidth(screenBounds.getWidth());
        primaryStage.setHeight(screenBounds.getHeight());

    }

    public  static  void switchToLoginScene() {
        loginSceneObj.updateValueChangeLocale();
        loginSceneObj.updateTexts();
        primaryStage.setWidth(400);
        primaryStage.setHeight(300);
        primaryStage.setScene(loginSceneObj.scene);
        primaryStage.centerOnScreen();





    }

    public static void main(String[] args) {
        launch(Application.class);
    }
}

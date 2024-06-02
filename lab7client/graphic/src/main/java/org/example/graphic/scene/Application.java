package org.example.graphic.scene;

import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;
import org.example.graphic.scene.util.BackgroundClickableMaker;

public class Application extends javafx.application.Application {
    @Getter
    private static  Stage primaryStage;
    private static final BackgroundClickableMaker backgroundClickableMaker = new BackgroundClickableMaker();
    private static  LoginScene loginSceneObj;
    private static  MainScene mainSceneObj;

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
        primaryStage.setScene(mainSceneObj.scene);


    }

    public  static  void switchToLoginScene() {

        primaryStage.setScene(loginSceneObj.scene);
        loginSceneObj.updateValueChangeLocale();
        loginSceneObj.updateTexts();


    }

    public static void main(String[] args) {
        launch(Application.class);
    }
}

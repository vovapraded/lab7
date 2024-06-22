package org.example.graphic.scene;

import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.example.graphic.scene.main.MainScene;
import org.example.graphic.scene.main.TicketStorage;
import org.example.graphic.scene.util.BackgroundClickableMaker;

public class Application extends javafx.application.Application {
    @Getter
    private static  Stage primaryStage;
    private static final BackgroundClickableMaker backgroundClickableMaker = new BackgroundClickableMaker();
    private static  LoginScene loginSceneObj;
    @Getter
    private static MainScene mainSceneObj;
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
        loginSceneObj.updateTextUI();
        primaryStage.show();
    }
    public static void switchToMainScene() {
        primaryStage.setTitle("Main");
        primaryStage.setMaximized(false);

        //подумать над мувом
        if (mainSceneObj == null) {
            TicketStorage ticketStorage = new TicketStorage();
            mainSceneObj = new MainScene(ticketStorage);
            mainSceneObj.createMainScene();
//            backgroundClickableMaker.make(mainSceneObj.scene);
            primaryStage.setScene(mainSceneObj.scene);

        }
        mainSceneObj.updateValueChangeLocale();
        mainSceneObj.updateTextUI();
        primaryStage.setScene(mainSceneObj.scene);


//        primaryStage.show();

        primaryStage.setMaximized(true);
        //устанавливаем минимум для пагинации
        var pagination = mainSceneObj.getPagination();
        pagination.setMinHeight(pagination.getHeight());
        pagination.setMinWidth(pagination.getWidth());



}




    public  static  void switchToLoginScene() {
        loginSceneObj.updateValueChangeLocale();
        loginSceneObj.updateTextUI();
        primaryStage.setWidth(400);
        primaryStage.setHeight(300);
        primaryStage.setScene(loginSceneObj.scene);
        primaryStage.centerOnScreen();





    }

    public static void main(String[] args) {
        launch(Application.class);
    }
}

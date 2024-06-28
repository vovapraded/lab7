package org.example.graphic.scene;

import javafx.stage.Stage;
import lombok.Cleanup;
import lombok.Getter;
import lombok.Setter;
import org.example.graphic.scene.main.MainScene;
import org.example.graphic.scene.main.draw.select.SelectedManager;
import org.example.graphic.scene.main.storage.TicketStorage;
import org.example.graphic.scene.main.storage.TicketUpdater;
import org.example.graphic.scene.util.BackgroundClickableMaker;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Application extends javafx.application.Application {
    @Getter
    private static  Stage primaryStage;
    private static final BackgroundClickableMaker backgroundClickableMaker = new BackgroundClickableMaker();
    private static  LoginScene loginSceneObj;
    @Getter
    private static MainScene mainSceneObj;
    @Getter @Setter
    private static String login;
    private static TicketStorage ticketStorage = null;
    private static ScheduledExecutorService scheduler ;

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

        if (ticketStorage == null){
            ticketStorage = new TicketStorage();
        }else{
            ticketStorage.unmakeAllTicketsSelected();
            SelectedManager.setSelectedId(null);
        }

        //подумать над мувом
        mainSceneObj = new MainScene(ticketStorage);
        mainSceneObj.createMainScene();
        scheduler = Executors.newScheduledThreadPool(1);
        System.out.println("Scheduler включен");
        scheduler.scheduleAtFixedRate(new TicketUpdater(ticketStorage),  TicketUpdater.getTIMEOUT(), TicketUpdater.getTIMEOUT(), TimeUnit.MILLISECONDS);

//            backgroundClickableMaker.make(mainSceneObj.scene);
        primaryStage.setScene(mainSceneObj.scene);
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
        if (scheduler!=null){
            scheduler.shutdownNow();
            System.out.println("Scheduler выключен");
        }
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

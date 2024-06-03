package org.example.graphic.scene;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.controller.MyController;
import org.example.graphic.localizator.Localizator;
import org.example.graphic.node.PlaceholderTextField;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LoginScene extends MyScene {
//    private Stage primaryStage;
//    private final String defaultBundle;
//    private  String bundle;


    public void createLoginScene(){
         createChangeLocaleBox();
        //        Label labelForChangeLocale = new Label(,changeLocale);
//        labelForChangeLocale.setContentDisplay(ContentDisplay.BOTTOM);
        // Создаем GridPane
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10)); // отступ от границ окна
        gridPane.setHgap(10); // горизонтальные промежутки между столбцами
        gridPane.setVgap(10); // вертикальные промежутки между строками
        //создаём register1 form
        GridPane loginForm = new GridPane();
        loginForm.setHgap(5); // горизонтальные промежутки между столбцами
        loginForm.setVgap(5);
        PlaceholderTextField inputLogin = new PlaceholderTextField("");
        PlaceholderTextField inputPassword = new PlaceholderTextField("");

//        Label labelForChangeLocale = new Label("",changeLocale);
//        labelForChangeLocale.setContentDisplay(ContentDisplay.BOTTOM);



        // Создаем HBox для кнопок и устанавливаем отступы
        HBox buttonBox = new HBox(20); // отступ 5 между кнопками
        buttonBox.setPadding(new Insets(10, 10, 10, 10)); // отступ от границ HBox
        loginForm.setAlignment(Pos.CENTER); // выравнивание по правому верхнему углу

        loginForm.add(inputLogin,0,0);
        loginForm.add(inputPassword,0,1);

        loginForm.add(buttonBox,0,2);

        // Создаем кнопки
        Button register = new Button();

        register.setOnAction(e -> {
            try {
                var message=controller.register(inputLogin.getText(),inputPassword.getText());
                Popup.showDialog(localizator.getKeyString(message));
                if (message.equals("SuccessRegisterAndLoggedIn")){
                    Application.setLogin(inputLogin.getText());
                    Application.switchToMainScene();

                }
            } catch (Exception ex) {
                Popup.showError(localizator.getKeyString(ex.getMessage()));

            }
        });

        Button login = new Button();
        login.setOnAction(e -> {
            try {
                var message=controller.login(inputLogin.getText(),inputPassword.getText());
                Popup.showDialog(localizator.getKeyString(message));
                Application.setLogin(inputLogin.getText());
                Application.switchToMainScene();

            } catch (Exception ex) {
                Popup.showError(localizator.getKeyString(ex.getMessage()));

            }
        });

        // Добавляем кнопки в HBox
        buttonBox.getChildren().addAll(login, register);
        GridPane.setHalignment(loginForm, HPos.CENTER);
        GridPane.setValignment(loginForm, VPos.CENTER);
        GridPane.setHalignment(changeLocaleLabel, HPos.RIGHT);
        GridPane.setValignment(changeLocaleLabel, VPos.BOTTOM);
        // Добавляем HBox в GridPane в ячейку (0, 0)
        gridPane.add(loginForm, 0, 0);
        gridPane.add(changeLocaleLabel,0,1);


        GridPane.setMargin(loginForm, new Insets(0, 0, 0, 0)); // добавляем отступы если нужно

        // Устанавливаем ColumnConstraints для правого выравнивания
        ColumnConstraints colConstraints = new ColumnConstraints();
        colConstraints.setHgrow(Priority.ALWAYS); // чтобы колонка занимала все доступное пространство
        gridPane.getColumnConstraints().add(colConstraints);

        // Устанавливаем RowConstraints для растяжения на всю высоту
        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setVgrow(Priority.ALWAYS); // чтобы строка занимала все доступное пространство
        gridPane.getRowConstraints().add(rowConstraints);
        
        nodeAndPropertyKeys.put(login,"LoginLabel");
        nodeAndPropertyKeys.put(register,"RegisterLabel");
//        nodeAndPropertyKeys.put(labelForChangeLocale,"changeLocaleLabel");
        nodeAndPropertyKeys.put(inputLogin,"loginLabel");
        nodeAndPropertyKeys.put(inputPassword,"passwordLabel");
        scene = new Scene(gridPane, 400, 300);
        System.out.println(localizator);
//        gridPane.setAlignment(Pos.TOP_LEFT);
        // Создаем сцену


    }




//    private void switchToMainScene() {
//        Scene mainScene = createMainScene();
//        primaryStage.setScene(mainScene);
//    }
//
//    private void switchToSecondScene() {
//        Scene secondScene = createSecondScene();
//        primaryStage.setScene(secondScene);
//    }



}

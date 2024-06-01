package org.example.graphic.scene;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class MainScene extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Создаем GridPane
        GridPane gridPane = new GridPane();

        // Создаем HBox для кнопок и устанавливаем отступы
        HBox buttonBox = new HBox(5); // отступ 5 между кнопками
        buttonBox.setPadding(new Insets(10)); // отступ от границ окна
        buttonBox.setAlignment(Pos.TOP_RIGHT); // выравнивание по правому верхнему углу

        // Создаем кнопки
        Button login = new Button("Login");
        login.setOnAction(e -> primaryStage.close());

        Button register = new Button("Register");
        register.setOnAction(e -> primaryStage.setIconified(true));

        // Добавляем кнопки в HBox
        buttonBox.getChildren().addAll(register, login);


        // Создаем сцену
        Scene scene = new Scene(gridPane, 800, 600); // начальный размер сцены

        // Устанавливаем сцену на Stage
        primaryStage.setScene(scene);

        // Делаем окно максимально возможным (но не полноэкранным)
        primaryStage.setMaximized(true);

        // Устанавливаем заголовок окна
        primaryStage.setTitle("GridPane Example");

        // Показываем Stage
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

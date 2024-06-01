package org.example.graphic.scene;

import com.dlsc.formsfx.model.util.ResourceBundleService;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.example.graphic.localizator.Localizator;
import org.example.graphic.node.PlaceholderTextField;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.spi.ResourceBundleProvider;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HelloApplication extends Application {
    private Localizator localizator;
//    private final String defaultBundle;
    private  String bundle;

    @Override

    public void start(Stage primaryStage) {
        Locale.setDefault(Locale.UK);
        Localizator localizator = new Localizator();



        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll("en EN","ru RU", "en IE", "nl NL","sq AL");
        comboBox.setValue("EN");
        comboBox.setOnAction(e -> {
            System.out.println("A");
            var value = comboBox.getValue();
            localizator.setBundle(ResourceBundle.getBundle("locales.gui",
                    new Locale(value.split(" ")[0],value.split(" ")[1])));
            System.out.println(localizator.getBundle().getLocale());
            System.out.println("ABOBA");



        });//        Label label = new Label(,comboBox);
//        label.setContentDisplay(ContentDisplay.BOTTOM);
        // Создаем GridPane
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10)); // отступ от границ окна
        gridPane.setHgap(10); // горизонтальные промежутки между столбцами
        gridPane.setVgap(10); // вертикальные промежутки между строками
        //создаём register1 form
        GridPane loginForm = new GridPane();
        loginForm.setHgap(5); // горизонтальные промежутки между столбцами
        loginForm.setVgap(5);
        PlaceholderTextField inputLogin = new PlaceholderTextField(localizator.getKeyString("loginLabel"));
        PlaceholderTextField inputPassword = new PlaceholderTextField(localizator.getKeyString("passwordLabel"));

        Label label = new Label(localizator.getKeyString("changeLocaleLabel"),comboBox);
        label.setContentDisplay(ContentDisplay.BOTTOM);




        // Создаем HBox для кнопок и устанавливаем отступы
        HBox buttonBox = new HBox(20); // отступ 5 между кнопками
        buttonBox.setPadding(new Insets(10, 10, 10, 10)); // отступ от границ HBox
        loginForm.setAlignment(Pos.CENTER); // выравнивание по правому верхнему углу

        loginForm.add(inputLogin,0,0);
        loginForm.add(inputPassword,0,1);

        loginForm.add(buttonBox,0,2);

        // Создаем кнопки
        Button register = new Button(localizator.getKeyString("RegisterLabel"));
        register.setOnAction(e -> primaryStage.close());

        Button login = new Button(localizator.getKeyString("LoginLabel"));
        login.setOnAction(e -> primaryStage.setIconified(true));

        // Добавляем кнопки в HBox
        buttonBox.getChildren().addAll(login, register);
        GridPane.setHalignment(loginForm, HPos.CENTER);
        GridPane.setValignment(loginForm, VPos.CENTER);
        GridPane.setHalignment(label, HPos.RIGHT);
        GridPane.setValignment(label, VPos.BOTTOM);
        // Добавляем HBox в GridPane в ячейку (0, 0)
        gridPane.add(loginForm, 0, 0);
        gridPane.add(label,0,1);

        GridPane.setMargin(loginForm, new Insets(0, 0, 0, 0)); // добавляем отступы если нужно

        // Устанавливаем ColumnConstraints для правого выравнивания
        ColumnConstraints colConstraints = new ColumnConstraints();
        colConstraints.setHgrow(Priority.ALWAYS); // чтобы колонка занимала все доступное пространство
        gridPane.getColumnConstraints().add(colConstraints);

        // Устанавливаем RowConstraints для растяжения на всю высоту
        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setVgrow(Priority.ALWAYS); // чтобы строка занимала все доступное пространство
        gridPane.getRowConstraints().add(rowConstraints);

//        gridPane.setAlignment(Pos.TOP_LEFT);
        // Создаем сцену
        Scene scene = new Scene(gridPane, 400, 300); // размер сцены

        // Устанавливаем сцену на Stage
        primaryStage.setScene(scene);

        // Устанавливаем заголовок окна
        primaryStage.setTitle("GridPane Example");
        scene.setOnMousePressed(event -> {
            // Перебираем все узлы в корневом узле и проверяем, было ли нажатие внутри какого-либо узла
            boolean clickInsideNode = false;
            var node = gridPane;
            var childrens = handle(new ArrayList<>(List.of(node)));

                for (javafx.scene.Node children  : childrens) {
                    if (children.getBoundsInParent().contains(event.getX(), event.getY())) {
                        System.out.println(event.getX()+" "+event.getY());
                        clickInsideNode = true;
                        break;
                }
            }

            // Если ни один узел не содержит нажатие, значит, оно произошло вне любого узла интерфейса
            if (!clickInsideNode) {
                scene.getRoot().requestFocus();
                System.out.println("Clicked outside!");
            }
        });

        // Показываем Stage
        primaryStage.show();

        Platform.runLater(() -> {
            inputLogin.getParent().requestFocus();
        });

    }
    private List<Node> handle(List<Node> nodes){
        AtomicBoolean flag = new AtomicBoolean(false);
            nodes = nodes.stream().map(nd -> {
                if (nd instanceof  Pane){
                    flag.set(true);
                    return (List<Node>)((Pane) nd).getChildren();
                }else {
                    return (Node) nd;
                }
            }) .flatMap(o -> o instanceof List ? ((List<Node>) o).stream() : Stream.of((Node) o))
                    .collect(Collectors.toList());

        if (!flag.get()){
            return nodes;
        }
        return  handle(nodes);
    }
//    private void updateTexts(List<Node> nodes) {
//        nodes.stream().forEach();
//        label.u(bundle.getString("label.text"));
//        button.setText(bundle.getString("button.text"));
//    }

    public static void main(String[] args) {
        launch(args);
    }
}

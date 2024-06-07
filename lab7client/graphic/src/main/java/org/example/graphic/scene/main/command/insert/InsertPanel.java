package org.example.graphic.scene.main.command.insert;

import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.common.dto.*;
import org.controller.MyController;
import org.example.graphic.scene.Application;
import org.example.graphic.scene.Popup;
import org.example.graphic.scene.main.command.Panel;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class InsertPanel extends Panel {


    public InsertPanel() {
        super("Insert",false);
    }
    private TextField idField;
    private TextField priceField;
    private TextField discountField;
    private VBox refundable;
    private TextField nameField;

    private VBox ticketTypeContainer;

    private TextField capacityField;
    private TextField venueNameField;

    private VBox venueTypeContainer = new VBox();
    private TextField coordinatesXField;
    private   TextField coordinatesYField;


    @Override
    public  void showForm(HashMap<Node,String> nodeAndKeys) {
        Platform.runLater(() -> {
            var firstForm = createFirstForm(nodeAndKeys);
            var secondForm = createSecondForm(nodeAndKeys);
            var thirdForm = createThirdForm(nodeAndKeys);
            pages.add(firstForm);
            pages.add(secondForm);
            pages.add(thirdForm);;
            dialog.getDialogPane().setContent(firstForm);

            //            Button continueButton = (Button) dialog.getDialogPane().lookupButton(applyButtonType);
//            continueButton.setOnAction(e -> createSecondForm(nodeAndKeys));
            dialog.showAndWait();
        });
    }

    @Override
    protected void onApply() throws Exception {
        Ticket ticket = Ticket.builder()
                .id(valueOf(Long.class, idField.getText()))
                .price(valueOf(Long.class, priceField.getText()))
                .discount(valueOf(Long.class, discountField.getText()))
                .refundable(getRefundableValue())
                .ticketType(getTicketTypeValue())
                .name(nameField.getText())
                .createdBy(Application.getLogin())
                .creationDate(LocalDateTime.now())
                .venue(Venue.builder()
                        .capacity(valueOf(Long.class, capacityField.getText()))
                        .name(venueNameField.getText())
                        .venueType(getVenueTypeValue())
                        .build())
                .coordinates(Coordinates.builder()
                        .x(valueOf(Double.class, coordinatesXField.getText()))
                        .y(valueOf(Long.class, coordinatesYField.getText()))
                        .build())
                .build();
        System.out.println(ticket.getId().getClass());
        MyController controller = MyController.getInstance();

        try {
            Popup.showDialog(controller.insert(ticket));;
        }catch (Exception e){
            Popup.showError(e.getMessage());
        }

        Application.getMainSceneObj().getTicketStorage().getData().add(ticket);
        Application.getMainSceneObj().getTicketStorage().filter();
        Application.getMainSceneObj().getCreatorTable().updatePagination();
        Application.getMainSceneObj().getZoomableCartesianPlot().updateMap();



    }

    private static <T> T valueOf(Class<T> clazz, Object param) {
        try {
            // Получаем метод valueOf с одним параметром типа Object
            Method valueOfMethod = clazz.getMethod("valueOf", param.getClass());

            // Вызываем метод valueOf на классе с переданным параметром
            return clazz.cast(valueOfMethod.invoke(null, param));
        } catch (Exception e) {
            return null;
        }
    }


    private GridPane createFirstForm(HashMap<Node, String> nodeAndKeys) {
        initDialog();

        Text ticketDetailsLabel = new Text();
        // Создаем элементы управления формы
        idField = createNumericTextField();
        Label idLabel = new Label();


        nameField = new TextField();
        Label nameLabel = new Label();




        var ticketTypeLabel = new Label();
        ticketTypeContainer = new VBox(); // Создаем контейнер для чекбоксов
// Добавляем чекбокс для каждого элемента перечисления TicketType
        ToggleGroup ticketTypeGroup = new ToggleGroup(); // Создаем группу для RadioButton

        for (TicketType type : TicketType.values()) {
            RadioButton radioButton = new RadioButton(type.toString()); // Создаем RadioButton с названием типа
            radioButton.setToggleGroup(ticketTypeGroup); // Добавляем RadioButton в группу
            ticketTypeContainer.getChildren().add(radioButton); // Добавляем RadioButton в контейнер
        }


        priceField = createNumericTextField();
        Label priceLabel = new Label();

        discountField = createNumericTextField();
        Label  discountLabel = new Label();

        Label  refundableLabel = new Label();
        refundable = new VBox();
        ToggleGroup refundableGroup = new ToggleGroup(); // Создаем группу для RadioButton
        var refundableList = new ArrayList<>(Arrays.asList(Boolean.TRUE, Boolean.FALSE, null));
        for (Boolean type : refundableList) {
            String typeString = type == null ? "null" : type.toString();
            RadioButton radioButton = new RadioButton(typeString); // Создаем RadioButton с названием типа
            radioButton.setToggleGroup(refundableGroup); // Добавляем RadioButton в группу
            refundable.getChildren().add(radioButton); // Добавляем RadioButton в контейнер
        }

        // Размещаем элементы управления на сетке
        GridPane grid = new GridPane();

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setHalignment(HPos.CENTER); // Устанавливаем выравнивание для первой колонки
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHalignment(HPos.CENTER); // Устанавливаем выравнивание для первой колонки
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setHalignment(HPos.CENTER); // Устанавливаем выравнивание для первой колонки
        ColumnConstraints col4 = new ColumnConstraints();
        col4.setHalignment(HPos.CENTER); //

        grid.getColumnConstraints().addAll(col1, col2,col3,col4);
        grid.setHgap(20);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        grid.add(ticketDetailsLabel, 0, 0);
        grid.add(idLabel, 0, 1);
        grid.add(idField, 1, 1);


        grid.add(nameLabel, 0, 2);
        grid.add(nameField, 1, 2);
        grid.add(ticketTypeLabel, 0, 3);
        grid.add(ticketTypeContainer, 1, 3);



        grid.add(priceLabel,0,9);
        grid.add(priceField,1,9);


        grid.add(discountLabel,0,10);
        grid.add(discountField,1,10);


        grid.add(refundableLabel,0,11);
        grid.add(refundable,1,11);



        nodeAndKeys.put(ticketDetailsLabel, "TicketDetailsLabel");

        nodeAndKeys.put(idLabel, "IdLabel");

        nodeAndKeys.put(priceLabel, "PriceLabel");
        nodeAndKeys.put(discountLabel, "DiscountLabel");
        nodeAndKeys.put(refundableLabel, "RefundableLabel");


        nodeAndKeys.put(nameLabel, "NameLabel");
        nodeAndKeys.put(ticketTypeLabel, "TypeLabel");







        Application.getMainSceneObj().updateTextUI();
        return grid;
    }
    private Boolean getRefundableValue() {
        for (var node : refundable.getChildren()) {
            if (node instanceof RadioButton) { // Проверяем, что элемент является RadioButton
                RadioButton radioButton = (RadioButton) node;
                if (radioButton.isSelected()) {
                    String text = radioButton.getText();
                    if (text.equals("true")) {
                        return Boolean.TRUE;
                    } else if (text.equals("false")) {
                        return Boolean.FALSE;
                    } else if (text.equals("null")) {
                        return null;
                    }
                }
            }
        }
        return null; // Если ни одна радиокнопка не выбрана
    }
    private TicketType getTicketTypeValue() {
        for (var node : ticketTypeContainer.getChildren()) {
            if (node instanceof RadioButton) {
                RadioButton radioButton = (RadioButton) node;
                if (radioButton.isSelected()) {
                    return TicketType.valueOf(radioButton.getText());
                }
            }
        }
        return null; // Если ни одна радиокнопка не выбрана
    }
    private VenueType getVenueTypeValue() {
        for (var node : venueTypeContainer.getChildren()) {
            if (node instanceof RadioButton) {
                RadioButton radioButton = (RadioButton) node;
                if (radioButton.isSelected()) {
                    if (radioButton.getText().equalsIgnoreCase("null")) {
                        return null;
                    } else {
                        return VenueType.valueOf(radioButton.getText());
                    }
                }
            }
        }
        return null;
    }









    private GridPane createSecondForm(HashMap<Node, String> nodeAndKeys) {

        Text venueDetailsLabel = new Text();
        // Создаем элементы управления формы
        capacityField = createNumericTextField();
        Label capacityLabel = new Label();

        venueNameField = new TextField();
        Label NameLabel = new Label();


        var venueTypeLabel = new Label();
        ToggleGroup venueTypeGroup = new ToggleGroup(); // Создаем группу для RadioButton
        venueTypeContainer = new VBox();
        for (VenueType type : VenueType.values()) {
            RadioButton radioButton = new RadioButton(type.toString()); // Создаем RadioButton с названием типа
            radioButton.setToggleGroup(venueTypeGroup); // Добавляем RadioButton в группу
            venueTypeContainer.getChildren().add(radioButton); // Добавляем RadioButton в контейнер
        }
        RadioButton radioButton = new RadioButton("null"); // Создаем RadioButton с названием типа
        radioButton.setToggleGroup(venueTypeGroup); // Добавляем RadioButton в группу
        venueTypeContainer.getChildren().add(radioButton); // Д



        // Размещаем элементы управления на сетке
        GridPane grid = new GridPane();

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setHalignment(HPos.CENTER); // Устанавливаем выравнивание для первой колонки
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHalignment(HPos.CENTER); // Устанавливаем выравнивание для первой колонки
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setHalignment(HPos.CENTER); // Устанавливаем выравнивание для первой колонки
        ColumnConstraints col4 = new ColumnConstraints();
        col4.setHalignment(HPos.CENTER); //

        grid.getColumnConstraints().addAll(col1, col2,col3,col4);
        grid.setHgap(20);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        grid.add(venueDetailsLabel, 0, 0);
        grid.add(capacityLabel, 0, 1);
        grid.add(capacityField, 1, 1);

        grid.add(NameLabel,0,2);
        grid.add(venueNameField,1,2);

        grid.add(venueTypeLabel,0,3);
        grid.add(venueTypeContainer,1,3);


        nodeAndKeys.put(venueDetailsLabel, "VenueDetailsLabel");
        nodeAndKeys.put(capacityLabel, "CapacityLabel");
        nodeAndKeys.put(venueTypeLabel, "TypeLabel");
        nodeAndKeys.put(NameLabel, "NameLabel");


        Application.getMainSceneObj().updateTextUI();

        return grid;
    }
    private GridPane createThirdForm(HashMap<Node, String> nodeAndKeys) {

        Text coordDetailsLabel = new Text();
        // Создаем элементы управления формы
        coordinatesXField = createDoubleTextField();
        Label coordinatesXLabel = new Label();

        coordinatesYField = createNumericTextField();
        Label coordinatesYLabel = new Label();

        // Размещаем элементы управления на сетке
        GridPane grid = new GridPane();

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setHalignment(HPos.CENTER); // Устанавливаем выравнивание для первой колонки
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHalignment(HPos.CENTER); // Устанавливаем выравнивание для первой колонки
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setHalignment(HPos.CENTER); // Устанавливаем выравнивание для первой колонки
        ColumnConstraints col4 = new ColumnConstraints();
        col4.setHalignment(HPos.CENTER); //

        grid.getColumnConstraints().addAll(col1, col2,col3,col4);
        grid.setHgap(20);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        grid.add(coordDetailsLabel, 0, 0);
        grid.add(coordinatesXLabel, 0, 1);
        grid.add(coordinatesXField, 1, 1);


        grid.add(coordinatesYLabel, 0, 2);
        grid.add(coordinatesYField, 1, 2);




        nodeAndKeys.put(coordDetailsLabel, "CoordinatesDetailsLabel");
        nodeAndKeys.put(coordinatesXLabel, "CoordinatesXLabel");
        nodeAndKeys.put(coordinatesYLabel, "CoordinatesYLabel");



        Application.getMainSceneObj().updateTextUI();

        return grid;
    }

//    private void showFilterForm(){
//        Popup.showFilterForm(n);
//
//    }
}

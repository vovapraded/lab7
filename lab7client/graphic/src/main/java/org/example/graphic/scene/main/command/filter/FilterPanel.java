package org.example.graphic.scene.main.command.filter;

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
import org.common.dto.TicketType;
import org.common.dto.VenueType;
import org.example.graphic.scene.Application;
import org.example.graphic.scene.main.CreatorTable;
import org.example.graphic.scene.main.TicketStorage;
import org.example.graphic.scene.main.command.Panel;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class FilterPanel extends Panel {


    public FilterPanel(TicketStorage ticketStorage) {
        super("Filter",false);
        this.ticketStorage = ticketStorage;
    }
    private final TicketStorage ticketStorage;
    private  TicketFilter ticketFilter;

    private TextField idMinField;
    private TextField idMaxField;
    private TextField priceMinField;
    private TextField priceMaxField;
    private TextField discountMinField;
    private TextField discountMaxField;
    private TextField refundableMinField;
    private VBox refundable;
    private TextField partOfNameField;
    private DatePicker dateMinPicker;
    private DatePicker dateMaxPicker;
    private Spinner<Integer> hourMinSpinner;
    private Spinner<Integer> minuteMinSpinner;
    private Spinner<Integer> hourMaxSpinner;
    private Spinner<Integer> minuteMaxSpinner;
    private VBox ticketTypeContainer;
    private TextField createdByField;

    private TextField capacityMinField;
    private TextField capacityMaxField;
    private TextField venuePartOfNameField;
    private DatePicker venueDateMinPicker;
    private DatePicker venueDateMaxPicker;
    private Spinner<Integer> venueHourMinSpinner;
    private Spinner<Integer> venueMinuteMinSpinner;
    private Spinner<Integer> venueHourMaxSpinner;
    private Spinner<Integer> venueMinuteMaxSpinner;
    private VBox venueTypeContainer;
    private TextField coordinatesXMinField;
     private   TextField    coordinatesXMaxField;
    private   TextField    coordinatesYMinField;
    private   TextField    coordinatesYMaxField;




    @Override
    protected void onApply() {
        LocalDateTime dateTimeMin = null;
        LocalDateTime dateTimeMax = null;
        if (dateMinPicker.getValue()!=null) {
            dateTimeMin = LocalDateTime.of(dateMinPicker.getValue(), LocalTime.of(hourMinSpinner.getValue(), minuteMinSpinner.getValue()));
        }
        if (dateMaxPicker.getValue()!=null) {
            dateTimeMax = LocalDateTime.of(dateMaxPicker.getValue(), LocalTime.of(hourMaxSpinner.getValue(), minuteMaxSpinner.getValue()));
        }
        TicketFilter ticketFilter = TicketFilter.builder()
                .idMin(valueOf(Long.class, idMinField.getText()))
                .idMax(valueOf(Long.class, idMaxField.getText()))
                .priceMin(valueOf(Long.class, priceMinField.getText()))
                .priceMax(valueOf(Long.class, priceMaxField.getText()))
                .discountMin(valueOf(Long.class, discountMinField.getText()))
                .discountMax(valueOf(Long.class, discountMaxField.getText()))
                .refundable(getRefundableValues())
                .ticketTypes(getTicketTypeValues())
                .dateMin(dateTimeMin)
                .dateMax(dateTimeMax)
                .partOfName(partOfNameField.getText())
                .createdBy(createdByField.getText())
                .venueFilter(VenueFilter.builder()
                        .capacityMin(valueOf(Long.class, capacityMinField.getText()))
                        .capacityMax(valueOf(Long.class, capacityMaxField.getText()))
                        .partOfName(venuePartOfNameField.getText())
                        .venueTypes(getVenueTypeValues())
                        .build())
                .coordinatesFilter(CoordinatesFilter.builder()
                        .xMin(valueOf(Double.class, coordinatesXMinField.getText()))
                        .xMax(valueOf(Double.class, coordinatesXMaxField.getText()))
                        .yMin(valueOf(Long.class, coordinatesYMinField.getText()))
                        .yMax(valueOf(Long.class, coordinatesYMaxField.getText()))
                        .build())
                .build();
        Application.getMainSceneObj().getTicketStorage().setTicketFilter(ticketFilter);
        Application.getMainSceneObj().getCreatorTable().updatePagination();
        System.out.println(ticketFilter);
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
    private  String valueOf(Object t) {
        if (t != null){
           return String.valueOf(t);
        }else {
            return "";
        }
    }

    protected GridPane createFirstForm(HashMap<Node, String> nodeAndKeys) {
        initDialog();
        ticketFilter = ticketStorage.getTicketFilter();
        Text ticketDetailsLabel = new Text();
        // Создаем элементы управления формы
         idMinField = createNumericTextField();
         idMinField.setText(valueOf(ticketFilter.getIdMin()));
         idMaxField = createNumericTextField();
        idMaxField.setText(valueOf(ticketFilter.getIdMax()));

        Label idMinLabel = new Label();
        Label idMaxLabel = new Label();

        partOfNameField = new TextField();
        partOfNameField.setText(ticketFilter.getPartOfName());

        Label partOfNameLabel = new Label();
//        partOfNameLabel.setLabelFor(partOfNameField);

        Label date = new Label();
        Label min = new Label();
         dateMinPicker = new DatePicker();
        var dateMin = ticketFilter.getDateMin();
        int hourMin = 0;
        int minuteMin = 0;
        if (dateMin != null) {
            dateMinPicker.setValue(dateMin.toLocalDate());
            minuteMin = dateMin.getMinute();
            hourMin = dateMin.getHour();
        }
//        dateMinPicker.setValue(LocalDate.now());
        Label hour = new Label();
         hourMinSpinner = new Spinner<>(0, 23, hourMin);
        Label minute = new Label();
         minuteMinSpinner = new Spinner<>(0, 59, minuteMin);


        Label max = new Label();
        dateMaxPicker = new DatePicker();

        var dateMax = ticketFilter.getDateMax();
        int hourMax = 0;
        int minuteMax = 0;
        if (dateMax != null) {
            dateMaxPicker.setValue(dateMax.toLocalDate());
            minuteMax = dateMax.getMinute();
            hourMax = dateMax.getHour();
        }
//        dateMaxPicker.setValue(LocalDate.now());
         hourMaxSpinner = new Spinner<>(0, 23, hourMax);
         minuteMaxSpinner = new Spinner<>(0, 59, minuteMax);

        var ticketTypeLabel = new Label();
         ticketTypeContainer = new VBox(); // Создаем контейнер для чекбоксов
// Добавляем чекбокс для каждого элемента перечисления TicketType
        for (TicketType type : TicketType.values()) {
            CheckBox checkBox = new CheckBox(type.toString()); // Создаем чекбокс с названием типа
            ticketTypeContainer.getChildren().add(checkBox); // Добавляем чекбокс в контейнер
            if (ticketFilter.getTicketTypes().contains(type)){
                checkBox.fire();
            }
        }
        var createdByLabel = new Label();
        createdByField = new TextField();
        createdByField.setText(ticketFilter.getCreatedBy());

        priceMinField = createNumericTextField();
        priceMinField.setText(valueOf(ticketFilter.getPriceMin()));
        priceMaxField = createNumericTextField();
        priceMaxField.setText(valueOf(ticketFilter.getPriceMax()));

        Label priceMinLabel = new Label();
        Label priceMaxLabel = new Label();

        discountMinField = createNumericTextField();
        discountMinField.setText(valueOf(ticketFilter.getDiscountMin()));
//        dateMin.set
        discountMaxField = createNumericTextField();
        discountMaxField.setText(valueOf(ticketFilter.getDiscountMax()));
        Label  discountMinLabel = new Label();
        Label  discountMaxLabel = new Label();
        refundable = new VBox();
        var refundableList =  new ArrayList<>(Arrays.asList(Boolean.TRUE,Boolean.FALSE,null));
        for (Boolean type : refundableList) {
            var typeString = type == null ? "null" : type.toString();
            CheckBox checkBox = new CheckBox(typeString); // Создаем чекбокс с названием типа
            refundable.getChildren().add(checkBox); // Добавляем чекбокс в контейнер
            if (ticketFilter.getRefundable().contains(type)){
                checkBox.fire();
            }
        }
        Label  refundableLabel = new Label();

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
        grid.add(idMinLabel, 0, 1);
        grid.add(idMinField, 1, 1);
        grid.add(idMaxLabel,2,1);
        grid.add(idMaxField,3,1);

        grid.add(partOfNameLabel, 0, 2);
        grid.add(partOfNameField, 1, 2);
        grid.add(ticketTypeLabel, 0, 3);
        grid.add(ticketTypeContainer, 1, 3);
        grid.add(min, 1, 4);
        grid.add(max, 2, 4);

        grid.add(date, 0, 5);
        grid.add(dateMinPicker, 1, 5);
        grid.add(dateMaxPicker, 2, 5);

        grid.add(hour,0,6);
        grid.add(hourMinSpinner, 1, 6);
        grid.add(hourMaxSpinner, 2, 6);

        grid.add(minute, 0, 7);
        grid.add(minuteMinSpinner, 1, 7);
        grid.add(minuteMaxSpinner, 2, 7);

        grid.add(createdByLabel,0,8);
        grid.add(createdByField,1,8);

        grid.add(priceMinLabel,0,9);
        grid.add(priceMinField,1,9);
        grid.add(priceMaxLabel,2,9);
        grid.add(priceMaxField,3,9);

        grid.add(discountMinLabel,0,10);
        grid.add(discountMinField,1,10);
        grid.add(discountMaxLabel,2,10);
        grid.add(discountMaxField,3,10);

        grid.add(refundableLabel,0,11);
        grid.add(refundable,1,11);



        nodeAndKeys.put(ticketDetailsLabel, "TicketDetailsLabel");
        nodeAndKeys.put(idMinLabel, "IdMinLabel");
        nodeAndKeys.put(idMaxLabel, "IdMaxLabel");

        nodeAndKeys.put(priceMinLabel, "PriceMinLabel");
        nodeAndKeys.put(priceMaxLabel, "PriceMaxLabel");
        nodeAndKeys.put(discountMinLabel, "DiscountMinLabel");
        nodeAndKeys.put(discountMaxLabel, "DiscountMaxLabel");
        nodeAndKeys.put(refundableLabel, "RefundableLabel");


        nodeAndKeys.put(partOfNameLabel, "PartOfNameLabel");
        nodeAndKeys.put(ticketTypeLabel, "TypeLabel");
        nodeAndKeys.put(ticketTypeContainer, "TypeLabel");

        nodeAndKeys.put(min, "minLabel");
        nodeAndKeys.put(max, "maxLabel");
        nodeAndKeys.put(date, "DateLabel");
        nodeAndKeys.put(hour, "HourLabel");
        nodeAndKeys.put(minute,"MinuteLabel");

        nodeAndKeys.put(createdByLabel,"CreatedByLabel");



        Application.getMainSceneObj().updateTextUI();
        return grid;
    }
    private List<Boolean> getRefundableValues() {
        List<Boolean> values = new ArrayList<>();
        for (var node : refundable.getChildren()) {
            if (node instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) node;
                if (checkBox.isSelected()) {
                    if (checkBox.getText().equals("true")){
                        values.add(Boolean.TRUE);
                    }
                    else if (checkBox.getText().equals("false")){
                        values.add(Boolean.FALSE);
                    }
                    else {
                        values.add(null);
                    }
                }
            }
        }
        return values;
    }
    private List<TicketType> getTicketTypeValues() {
        List<TicketType> values = new ArrayList<>();
        for (var node : ticketTypeContainer.getChildren()) {
            if (node instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) node;
                if (checkBox.isSelected()) {
                    values.add(TicketType.valueOf(checkBox.getText()));
                }
            }
        }
        return values;
    }
    private List<VenueType> getVenueTypeValues() {
        List<VenueType> values = new ArrayList<>();
        for (var node : venueTypeContainer.getChildren()) {
            if (node instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) node;
                if (checkBox.isSelected()) {
                    values.add(VenueType.valueOf(checkBox.getText()));
                }
            }
        }
        return values;
    }
    protected GridPane createSecondForm(HashMap<Node, String> nodeAndKeys) {

        Text venueDetailsLabel = new Text();
        var venueFilter = ticketFilter.getVenueFilter();
        // Создаем элементы управления формы
         capacityMinField = createNumericTextField();
        capacityMinField.setText(valueOf(venueFilter.getCapacityMin()));
         capacityMaxField = createNumericTextField();
        capacityMaxField.setText(valueOf(venueFilter.getCapacityMax()));
        Label capacityMinLabel = new Label();
        Label capacityMaxLabel = new Label();
        capacityMaxLabel.setPrefWidth(Region.USE_COMPUTED_SIZE);

        venuePartOfNameField = new TextField();
        venuePartOfNameField.setText(venueFilter.getPartOfName());
        Label partOfNameLabel = new Label();


        var venueTypeLabel = new Label();
         venueTypeContainer = new VBox(); // Создаем контейнер для чекбоксов
// Добавляем чекбокс для каждого элемента перечисления TicketType
        for (VenueType type : VenueType.values()) {
            CheckBox checkBox = new CheckBox(type.toString()); // Создаем чекбокс с названием типа
            venueTypeContainer.getChildren().add(checkBox); // Добавляем чекбокс в контейнер
            if (venueFilter.getVenueTypes().contains(type)){
                checkBox.fire();
            }
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
        grid.add(venueDetailsLabel, 0, 0);
        grid.add(capacityMinLabel, 0, 1);
        grid.add(capacityMinField, 1, 1);
        grid.add(capacityMaxLabel,2,1);
        grid.add(capacityMaxField,3,1);

        grid.add(partOfNameLabel,0,2);
        grid.add(venuePartOfNameField,1,2);

        grid.add(venueTypeLabel,0,3);
        grid.add(venueTypeContainer,1,3);


        nodeAndKeys.put(venueDetailsLabel, "VenueDetailsLabel");
        nodeAndKeys.put(capacityMinLabel, "CapacityMinLabel");
        nodeAndKeys.put(capacityMaxLabel, "CapacityMaxLabel");
        nodeAndKeys.put(venueTypeLabel, "TypeLabel");
        nodeAndKeys.put(partOfNameLabel, "PartOfNameLabel");


        Application.getMainSceneObj().updateTextUI();

        return grid;
    }
    protected GridPane createThirdForm(HashMap<Node, String> nodeAndKeys) {

        Text coordDetailsLabel = new Text();
        var coordinatesFilter = ticketFilter.getCoordinatesFilter();
        // Создаем элементы управления формы
        coordinatesXMinField = createDoubleTextField();
        coordinatesXMinField.setText(valueOf(coordinatesFilter.getXMin()));
        coordinatesXMaxField = createDoubleTextField();
        coordinatesXMaxField.setText(valueOf(coordinatesFilter.getXMax()));

        Label coordinatesXMinLabel = new Label();
        Label coordinatesXMaxLabel = new Label();

        coordinatesYMinField = createNumericTextField();
        coordinatesYMinField.setText(valueOf(coordinatesFilter.getYMin()));
        coordinatesYMaxField = createNumericTextField();
        coordinatesYMaxField.setText(valueOf(coordinatesFilter.getYMax()));

        Label coordinatesYMinLabel = new Label();
        Label coordinatesYMaxLabel = new Label();







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
        grid.add(coordinatesXMinLabel, 0, 1);
        grid.add(coordinatesXMinField, 1, 1);
        grid.add(coordinatesXMaxLabel,2,1);
        grid.add(coordinatesXMaxField,3,1);

        grid.add(coordinatesYMinLabel, 0, 2);
        grid.add(coordinatesYMinField, 1, 2);
        grid.add(coordinatesYMaxLabel,2,2);
        grid.add(coordinatesYMaxField,3,2);



        nodeAndKeys.put(coordDetailsLabel, "CoordinatesDetailsLabel");
        nodeAndKeys.put(coordinatesXMinLabel, "CoordinatesXMinLabel");
        nodeAndKeys.put(coordinatesXMaxLabel, "CoordinatesXMaxLabel");
        nodeAndKeys.put(coordinatesYMinLabel, "CoordinatesYMinLabel");
        nodeAndKeys.put(coordinatesYMaxLabel, "CoordinatesYMaxLabel");



        Application.getMainSceneObj().updateTextUI();

        return grid;
    }

//    private void showFilterForm(){
//        Popup.showFilterForm(n);
//
//    }
}

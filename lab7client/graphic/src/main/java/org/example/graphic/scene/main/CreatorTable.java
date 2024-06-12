package org.example.graphic.scene.main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.util.converter.DefaultStringConverter;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.common.dto.Ticket;
import org.common.dto.TicketType;
import org.common.dto.VenueType;
import org.controller.MyController;
import org.example.graphic.node.TableColumnAdapter;
import org.example.graphic.scene.main.utils.*;
import org.example.graphic.scene.main.utils.conventer.*;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;

public class CreatorTable {
    @Getter
    private TableView<Ticket> table;
    @Getter
    private SortedList<Ticket> sortedData;
    private final TicketStorage ticketStorage;
    private HashMap<Node, String> nodeAndPropertyKeys = new HashMap<>();
    private final MyController controller = MyController.getInstance();
    private final MainScene mainScene;
    @Getter
    private Pagination pagination;
    @Getter
    private static final int ROWS_PER_PAGE = 10;
    @Getter
    @Setter
    private ZoomableCartesianPlot zoomableCartesianPlot;
    private HashMap<Integer,VBox> boxes = new HashMap<>();
    private VBox box;

    public CreatorTable(TicketStorage ticketStorage, MainScene mainScene) {
        this.ticketStorage = ticketStorage;
        this.nodeAndPropertyKeys = mainScene.getNodeAndPropertyKeys();
        this.mainScene = mainScene;
    }

    public Pagination init() {
        pagination = createPagination();
        createTable();

        return pagination;
    }

    @SneakyThrows
    private TableView<Ticket> createTable() {

        table = new TableView<>();
        table.setPadding(new Insets(10));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setMinSize(760,360); // Разрешаем таблице занимать всю доступную высоту
        table.setEditable(true);


        TableColumn<Ticket, Long> id = new TableColumn<>("");
        id.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Ticket, String> name = ColumnUtils.createEditableColumn("Name", "name", new DefaultStringConverter(),value -> value!=null && !value.isBlank());





        TableColumn<Ticket, LocalDateTime> creationDate = new TableColumn<>("Creation date");
        creationDate.setCellValueFactory(new PropertyValueFactory<>("creationDate"));


        TableColumn<Ticket, Long> price = ColumnUtils.createEditableColumn("Price", "price", new LongStringConverter(),value -> value>0L);

        TableColumn<Ticket, Long> discount = ColumnUtils.createEditableColumn("Discount", "discount", new LongStringConverter(), value -> value==null || value>0L);

        TableColumn<Ticket, Boolean> refundable = ColumnUtils.createEditableColumn("Refundable", "refundable", new BooleanStringConverter(),value -> true);

        TableColumn<Ticket, TicketType> ticketType = ColumnUtils.createEditableColumn("Ticket type", "ticketType", new TicketTypeStringConverter(),value -> value!=null);


        TableColumn<Ticket, String> createdBy = new TableColumn<>("Created by");
        createdBy.setCellValueFactory(new PropertyValueFactory<>("createdBy"));

        TableColumn<Ticket, Long> venueCapacity = ColumnUtils.createEditableColumn("Capacity", "venueCapacity", new LongStringConverter(),value -> value==null || value>0L);

        TableColumn<Ticket, String> venueName = ColumnUtils.createEditableColumn("Name", "venueName", new DefaultStringConverter(),value -> value!=null);

        TableColumn<Ticket, VenueType> venueType = ColumnUtils.createEditableColumn("Type", "venueType", new VenueTypeStringConverter(),value -> true);


        TableColumn<Ticket, Double> x = ColumnUtils.createEditableColumn("x", "coordinatesX", new DoubleStringConverter(),value -> value!=null);

        TableColumn<Ticket, Long> y = ColumnUtils.createEditableColumn("y", "coordinatesY", new LongStringConverter(),value -> value>-618L);

        TableColumn<Ticket, ?> ticketDetailsColumn = new TableColumn<>("Ticket Details");
        ticketDetailsColumn.getColumns().addAll(id, name, price, discount, refundable, createdBy, creationDate, ticketType);

        TableColumn<Ticket, ?> venueDetailsColumn = new TableColumn<>("Venue Details");
        venueDetailsColumn.getColumns().addAll(venueName, venueCapacity, venueType);

        TableColumn<Ticket, ?> coordinatesDetails = new TableColumn<>("Coordinates Details");
        coordinatesDetails.getColumns().addAll(x, y);

        table.getColumns().addAll(ticketDetailsColumn, venueDetailsColumn, coordinatesDetails);

        table.getSortOrder().add(price);

        table.setSortPolicy(var1 -> {
            var comparator = var1.getComparator();
            if (comparator == null) {
                price.setSortType(TableColumn.SortType.ASCENDING);
                return true;
            }
            if (comparator.equals(sortedData.getComparator())) {
                comparator = comparator.reversed();
            }
            sortedData.setComparator(comparator);
            pagination.setPageFactory(this::createPage);
            return false;
        });

//
        table.setRowFactory(tv -> {
            TableRow<Ticket> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    Ticket rowData = row.getItem();
                    int index = sortedData.indexOf(rowData);
                    System.out.println("Clicked on index: " + index);
                    table.getSelectionModel().select(index);

                    // Определяем, какой столбец был нажат, и начинаем редактирование этой ячейки
                    TablePosition<Ticket, ?> position = table.getFocusModel().getFocusedCell();
                    if (position != null) {
                        table.edit(index, position.getTableColumn());
                    }
                }
            });
            return row;
        });

        price.setOnEditCommit(event -> {
            var newValue = event.getNewValue();
            Ticket ticket = event.getRowValue();
            ticket.setPrice(newValue);
            System.out.println("New value committed: " + newValue);
        });
        this.box = new VBox(table);
//        this.box.setFillWidth(true); // Разрешаем VBox занимать всю доступную ширину

//        box.setMinSize(800,400);
//        box.setMaxSize(800,400);



        nodeAndPropertyKeys.put(new TableColumnAdapter(id), "IdLabel");
        nodeAndPropertyKeys.put(new TableColumnAdapter(name), "NameLabel");
        nodeAndPropertyKeys.put(new TableColumnAdapter(price), "PriceLabel");
        nodeAndPropertyKeys.put(new TableColumnAdapter(discount), "DiscountLabel");
        nodeAndPropertyKeys.put(new TableColumnAdapter(refundable), "RefundableLabel");
        nodeAndPropertyKeys.put(new TableColumnAdapter(createdBy), "CreatedByLabel");
        nodeAndPropertyKeys.put(new TableColumnAdapter(creationDate), "CreationDateLabel");
        nodeAndPropertyKeys.put(new TableColumnAdapter(ticketType), "TypeLabel");
        nodeAndPropertyKeys.put(new TableColumnAdapter(venueName), "NameLabel");
        nodeAndPropertyKeys.put(new TableColumnAdapter(venueType), "TypeLabel");
        nodeAndPropertyKeys.put(new TableColumnAdapter(venueCapacity), "CapacityLabel");
        nodeAndPropertyKeys.put(new TableColumnAdapter(x), "xLabel");
        nodeAndPropertyKeys.put(new TableColumnAdapter(y), "yLabel");
        nodeAndPropertyKeys.put(new TableColumnAdapter(ticketDetailsColumn), "TicketDetailsLabel");
        nodeAndPropertyKeys.put(new TableColumnAdapter(coordinatesDetails), "CoordinatesDetailsLabel");
        nodeAndPropertyKeys.put(new TableColumnAdapter(venueDetailsColumn), "VenueDetailsLabel");
        mainScene.updateTextUI();
        setOnMouseClicked();
        return table;
    }

    private void setOnMouseClicked() {
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            ticketStorage.unmakeAllTicketsSelected();
            if (newSelection != null) {
                if (!newSelection.equals(oldSelection)) {
                    ticketStorage.makeTicketSelected(newSelection.getId());
                }
            }
            System.out.println(ticketStorage);
            zoomableCartesianPlot.updateMap();
        });

    }

    @SneakyThrows
    public Pagination createPagination() {
        sortedData = new SortedList<>(ticketStorage.getFilteredData());
        int totalPageCount = (int) Math.ceil((double) sortedData.size() / ROWS_PER_PAGE);
        pagination = new Pagination(totalPageCount, 0);
        pagination.setPageFactory(this::createPage);
//        pagination.currentPageIndexProperty().addListener(new ChangeListener<Number>() {
//            @Override
//            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//                initPage(newValue.intValue());
//                System.out.println("поменялась страница");
//            }
//        });
        pagination.setMinSize(800,400);

        return pagination;
    }

    private VBox createPage(int pageIndex) {
        initPage(pageIndex);
        return box;
    }

    private void initPage(int pageIndex) {
        int fromIndex = pageIndex * ROWS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ROWS_PER_PAGE, sortedData.size());

        if (sortedData.getComparator() == null) {
            sortedData.setComparator(Comparator.naturalOrder());
        }

        // Создаем данные для текущей страницы
        ObservableList<Ticket> pageData = FXCollections.observableArrayList(sortedData.subList(fromIndex, toIndex));
        box.setPadding(new Insets(20));
        // Устанавливаем данные в таблицу
        table.setItems(pageData);
        System.out.println("table"+table.getWidth());
        System.out.println(table.getHeight());
        System.out.println("box"+box.getWidth());
        System.out.println(box.getHeight());
        box.requestFocus();
        box.getChildren().setAll(table);
//        boxes.putIfAbsent(pageIndex,new VBox());
        // Обновляем VBox


    }

    public void updatePagination() {
        updateData();
        pagination.setPageFactory(this::createPage);
    }

    public void updateData() {
        ObservableList<Ticket> updatedData = FXCollections.observableArrayList(ticketStorage.getFilteredData());
        sortedData = new SortedList<>(updatedData);
        sortedData.setComparator(Comparator.naturalOrder());

    }
}

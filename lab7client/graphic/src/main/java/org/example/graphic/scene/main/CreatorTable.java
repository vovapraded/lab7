package org.example.graphic.scene.main;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
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
import org.example.graphic.scene.main.draw.entity.DrawingTicket;
import org.example.graphic.scene.main.draw.select.SelectedManager;
import org.example.graphic.scene.main.storage.TicketStorage;
import org.example.graphic.scene.main.utils.*;
import org.example.graphic.scene.main.utils.conventer.*;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Optional;

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
    private final      SelectedManager selectedManager;
    @Getter
    private VBox box;

    public CreatorTable(TicketStorage ticketStorage, MainScene mainScene) {
        this.ticketStorage = ticketStorage;
        selectedManager = new SelectedManager(ticketStorage,this);

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
        table.setPadding(new Insets(12));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
//        table.setMinSize(760,360); // Разрешаем таблице занимать всю доступную высоту
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

        TableColumn<Ticket, String> venueName = ColumnUtils.createEditableColumn("Name", "venueName", new DefaultStringConverter(),value -> value!=null && !value.isBlank());

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
            comparator = comparator.thenComparing(Ticket::getId);
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
                }else if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1){
                    selectedManager.tryToSelectTicket(row.getItem().getId());
                    }
            });
            return row;
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
        return table;
    }



    @SneakyThrows
    public Pagination createPagination() {

        sortedData = new SortedList<Ticket>(ticketStorage.getFilteredData());
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
//        pagination.setMinSize(800,400);

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
            sortedData.setComparator(Comparator.comparing(Ticket::getPrice).thenComparing(Ticket::getId));
        }

        // Создаем данные для текущей страницы
        ObservableList<Ticket> pageData = FXCollections.observableArrayList(sortedData.subList(fromIndex, toIndex));
            sortedData.addListener((ListChangeListener.Change<? extends Ticket> change) ->{
                while (change.next()){
                    if (change.wasAdded() || change.wasRemoved()|| change.wasPermutated()){
                        if (fromIndex < sortedData.size()) {
                            pageData.setAll(sortedData.subList(fromIndex, Math.min(fromIndex + ROWS_PER_PAGE, sortedData.size())));
                        }
                    }
                }
        });
        box.setPadding(new Insets(20));
        // Устанавливаем данные в таблицу
        table.setItems(pageData);
        box.requestFocus();
        box.getChildren().setAll(table);
//        boxes.putIfAbsent(pageIndex,new VBox());
        // Обновляем VBox


    }

    public void selectTicket(Ticket ticket) {
        Platform.runLater(() -> {

        var index =     sortedData.indexOf(ticket);
        var itemsPerPage = ROWS_PER_PAGE;
        var indexOfPage = index / itemsPerPage;
        pagination.setCurrentPageIndex(indexOfPage);



        int finalIndex = (index)% itemsPerPage;

                table.getSelectionModel().clearAndSelect((finalIndex));
        });

    }
    public void updatePagination() {

        int totalPageCount = (int) Math.ceil((double) sortedData.size() / ROWS_PER_PAGE);
        pagination.setPageCount(totalPageCount);
        selectTicket(ticketStorage.findSelectedTicket());

    }




    public void selectTicket(Optional<DrawingTicket> selectedTicketOptional) {

     selectedTicketOptional.ifPresent((selectedTicket)->{
        var ticket = selectedTicket.getTicket();
        selectTicket(ticket);
    });
    }

}

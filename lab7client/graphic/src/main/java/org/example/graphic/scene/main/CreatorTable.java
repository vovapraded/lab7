package org.example.graphic.scene.main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import lombok.SneakyThrows;
import org.common.dto.Ticket;
import org.common.dto.TicketType;
import org.common.dto.VenueType;
import org.controller.MyController;
import org.example.graphic.node.TableColumnAdapter;
import org.example.graphic.scene.MyScene;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;

public class CreatorTable {
    private  TableView<Ticket> table;
    private SortedList<Ticket> sortedData; // SortedList для поддержания сортировки
    private final TicketStorage ticketStorage;
    private HashMap<Node,String > nodeAndPropertyKeys = new HashMap<Node,String >();
    private final  MyController controller = MyController.getInstance();
    private final MainScene mainScene;
    private Pagination pagination;
    private static final int ROWS_PER_PAGE = 10; // Количество строк на страницу



    public Pagination init(){
        pagination = createPagination();
        createTable();
        return pagination;
    }

    public CreatorTable(TicketStorage ticketStorage, MainScene mainScene) {
        this.ticketStorage = ticketStorage;
        this.nodeAndPropertyKeys = mainScene.getNodeAndPropertyKeys();
        this.mainScene = mainScene;
    }

    @SneakyThrows
    private TableView<Ticket> createTable() {
        table = new TableView<Ticket>();
        table.setPadding(new Insets(30));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Ticket, Long> id = new TableColumn<>("");
        id.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Ticket, String> name = new TableColumn<>("Name");
        name.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Ticket, LocalDateTime> creationDate = new TableColumn<>("Creation date");
        creationDate.setCellValueFactory(new PropertyValueFactory<>("creationDate"));

        TableColumn<Ticket, Long> price = new TableColumn<>("Price");
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        price.setSortable(true);


        price.setSortType(TableColumn.SortType.ASCENDING);
//        price.setText();


        TableColumn<Ticket, Long> discount = new TableColumn<>("Discount");
        discount.setCellValueFactory(new PropertyValueFactory<>("discount"));

        TableColumn<Ticket, Boolean> refundable = new TableColumn<>("Refundable");
        refundable.setCellValueFactory(new PropertyValueFactory<>("refundable"));

        TableColumn<Ticket, TicketType> ticketType = new TableColumn<>("Ticket type");
        ticketType.setCellValueFactory(new PropertyValueFactory<>("ticketType"));

        TableColumn<Ticket, String> createdBy = new TableColumn<>("Created by");
        createdBy.setCellValueFactory(new PropertyValueFactory<>("createdBy"));

        TableColumn<Ticket, Long> venueCapacity = new TableColumn<>("Capacity");
        venueCapacity.setCellValueFactory(new PropertyValueFactory<>("venueCapacity"));

        TableColumn<Ticket, String> venueName = new TableColumn<>("Name");
        venueName.setCellValueFactory(new PropertyValueFactory<>("venueName"));

        TableColumn<Ticket, VenueType> venueType = new TableColumn<>("Type");
        venueType.setCellValueFactory(new PropertyValueFactory<>("venueType"));

        TableColumn<Ticket, Double> x = new TableColumn<>("x");
        x.setCellValueFactory(new PropertyValueFactory<>("coordinatesX"));

        TableColumn<Ticket, Long> y = new TableColumn<>("y");
        y.setCellValueFactory(new PropertyValueFactory<>("coordinatesY"));

        TableColumn<Ticket, ?> ticketDetailsColumn = new TableColumn<>("Ticket Details");
        ticketDetailsColumn.getColumns().addAll(id, name, price, discount, refundable, createdBy, creationDate);

        TableColumn<Ticket, ?> venueDetailsColumn = new TableColumn<>("Venue Details");
        venueDetailsColumn.getColumns().addAll(venueName, venueCapacity, venueType);

        TableColumn<Ticket, ?> coordinatesDetails = new TableColumn<>("Coordinates Details");
        coordinatesDetails.getColumns().addAll(x, y);

        table.getColumns().addAll(ticketDetailsColumn, venueDetailsColumn, coordinatesDetails);
        table.getSortOrder().add(price);
//        table.getSelectionModel().clearAndSelect(3);
        table.setSortPolicy(var1 -> {

            var comparator = var1.getComparator();
            if (comparator == null){
                price.setSortType(TableColumn.SortType.ASCENDING);





//                    sortedData.setComparator(Comparator.naturalOrder());
//                    pagination.setPageFactory(pageIndex -> createPage(pageIndex));
                return true;
            }
            if (comparator.equals(sortedData.getComparator())){
                comparator = comparator.reversed();
            }
            sortedData.setComparator(comparator);
//                FXCollections.sort(sortedData, sortedData.getComparator());
            pagination.setPageFactory(pageIndex -> createPage(pageIndex));
            return false;
        });

        nodeAndPropertyKeys.put(new TableColumnAdapter(id),"IdLabel");
        nodeAndPropertyKeys.put(new TableColumnAdapter(name),"NameLabel");
        nodeAndPropertyKeys.put(new TableColumnAdapter(price),"PriceLabel");
        nodeAndPropertyKeys.put(new TableColumnAdapter(discount),"DiscountLabel");
        nodeAndPropertyKeys.put(new TableColumnAdapter(refundable),"RefundableLabel");
        nodeAndPropertyKeys.put(new TableColumnAdapter(createdBy),"CreatedByLabel");
        nodeAndPropertyKeys.put(new TableColumnAdapter(creationDate),"CreationDateLabel");
        nodeAndPropertyKeys.put(new TableColumnAdapter(ticketType),"TypeLabel");
        nodeAndPropertyKeys.put(new TableColumnAdapter(venueName),"NameLabel");
        nodeAndPropertyKeys.put(new TableColumnAdapter(venueType),"TypeLabel");
        nodeAndPropertyKeys.put(new TableColumnAdapter(venueCapacity),"CapacityLabel");
        nodeAndPropertyKeys.put(new TableColumnAdapter(x),"xLabel");
        nodeAndPropertyKeys.put(new TableColumnAdapter(y),"yLabel");
        nodeAndPropertyKeys.put(new TableColumnAdapter(ticketDetailsColumn),"TicketDetailsLabel");
        nodeAndPropertyKeys.put(new TableColumnAdapter(coordinatesDetails),"CoordinatesDetailsLabel");
        nodeAndPropertyKeys.put(new TableColumnAdapter(venueDetailsColumn),"VenueDetailsLabel");
        mainScene.updateTexts();
        return table;
    }
    @SneakyThrows
    private Pagination createPagination() {
        sortedData = new SortedList<>(ticketStorage.getData());
//        sortedData.comparatorProperty().addListener((obs, oldComparator, newComparator) -> {
//            // Устанавливаем новый компаратор
//            sortedData.setComparator(newComparator);
//            // Обновляем содержимое таблицы на первой странице пагинации
//        });


        int totalPageCount = (int) Math.ceil((double) sortedData.size() / ROWS_PER_PAGE);
        Pagination pagination = new Pagination(totalPageCount, 0);
        pagination.setPageFactory(this::createPage);
        return pagination;
    }



    private VBox createPage(int pageIndex) {
        int fromIndex = pageIndex * ROWS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ROWS_PER_PAGE, sortedData.size());

        if (sortedData.getComparator() == null) {
            sortedData.setComparator(Comparator.naturalOrder());
        }



        ObservableList<Ticket> pageData = FXCollections.observableArrayList(sortedData.subList(fromIndex, toIndex));
        table.setItems(pageData);



        VBox box = new VBox(table);
        box.setPadding(new Insets(20));
        return box;
    }
}

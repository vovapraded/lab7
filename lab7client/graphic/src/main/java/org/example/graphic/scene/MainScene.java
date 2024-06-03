package org.example.graphic.scene;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.util.Callback;
import lombok.SneakyThrows;
import org.common.dto.*;

import java.time.LocalDateTime;
import java.util.Comparator;

public class MainScene extends MyScene {
    private static final int ROWS_PER_PAGE = 10; // Количество строк на страницу

    private SortedList<Ticket> sortedData; // SortedList для поддержания сортировки
    private ObservableList<Ticket> data;
    private Pagination pagination;
    private TableView<Ticket> table;

    public void createMainScene() {
        BorderPane root = new BorderPane();
        Button logOut = new Button("");
        Text loginLabel1 = new Text();
        Text loginLabel2 = new Text(": " + Application.getLogin());
        HBox username = new HBox(loginLabel1, loginLabel2);
        VBox userBox = new VBox(username, logOut);
        HBox userBoxWrapper = new HBox(userBox);
        userBoxWrapper.setAlignment(Pos.TOP_RIGHT);
        userBox.setPadding(new Insets(20));
        userBox.setSpacing(10);

        createChangeLocaleBox();
        nodeAndPropertyKeys.put(logOut, "LogOut");
        nodeAndPropertyKeys.put(loginLabel1, "LoginLabel");
        logOut.setOnAction(e -> Application.switchToLoginScene());
        root.setTop(userBoxWrapper);

        HBox changeLocaleWrapper = new HBox(changeLocaleLabel);
        changeLocaleWrapper.setPadding(new Insets(20));
        changeLocaleWrapper.setAlignment(Pos.BOTTOM_RIGHT);
        root.setBottom(changeLocaleWrapper);

        pagination = createPagination();
        root.setCenter(pagination);
        table = createTable();

        scene = new Scene(root, 800, 600);

        

    }

    @SneakyThrows
    private Pagination createPagination() {
        data = FXCollections.observableArrayList(controller.show());
        sortedData = new SortedList<>(data);
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

    @SneakyThrows
    private TableView<Ticket> createTable() {
        table = new TableView<>();
        table.setPadding(new Insets(30));

        TableColumn<Ticket, Long> id = new TableColumn<>("Id");
        id.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Ticket, String> name = new TableColumn<>("Name");
        name.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Ticket, LocalDateTime> creationDate = new TableColumn<>("Creation date");
        creationDate.setCellValueFactory(new PropertyValueFactory<>("creationDate"));

        TableColumn<Ticket, Long> price = new TableColumn<>("Price");
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        price.setSortable(true);

        price.setSortType(TableColumn.SortType.ASCENDING);


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



        return table;
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

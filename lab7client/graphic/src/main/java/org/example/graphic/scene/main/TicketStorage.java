package org.example.graphic.scene.main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;
import org.common.dto.Ticket;
import org.controller.MyController;
import org.example.graphic.scene.main.command.filter.Filter;
import org.example.graphic.scene.main.command.filter.TicketFilter;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

 @Setter
public class TicketStorage {
    @Getter
    private ObservableList<Ticket> data;
    private ObservableList<Ticket> filteredData;
    private Filter filter = new Filter();
    private TicketFilter ticketFilter;

    public ObservableList<Ticket> getFilteredData() {
        filter.filter(ticketFilter,filteredData);
        return filteredData;
    }

    public TicketStorage() {
        try {
            data = FXCollections.observableArrayList(MyController.getInstance().show());
            filteredData = FXCollections.observableArrayList(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    //TODO Переписать
    public ObservableList<WrappedTicket> getWrappedData() {
        Map<String, List<Ticket>> groupedByAuthor = data.stream()
                .collect(Collectors.groupingBy(Ticket::getCreatedBy));

        ObservableList<WrappedTicket> wrappedTickets = groupedByAuthor.entrySet().stream()
                .flatMap(entry -> {
                    Color randomColor = getRandomColor();
                    return entry.getValue().stream()
                            .map(ticket -> new WrappedTicket(ticket, randomColor));
                })
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        return wrappedTickets;
    }
    private  Color getRandomColor(){
        Random random = new Random();
        double red = random.nextDouble();
        double green = random.nextDouble();
        double blue = random.nextDouble();
        Color randomColor = new Color(red, green, blue, 0.2);
        return randomColor;
    }



}

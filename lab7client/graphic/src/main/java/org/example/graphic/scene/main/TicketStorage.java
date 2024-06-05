package org.example.graphic.scene.main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;
import org.common.dto.Ticket;
import org.controller.MyController;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Getter @Setter
public class TicketStorage {
    private ObservableList<Ticket> data;

    public TicketStorage() {
        try {
            data = FXCollections.observableArrayList(MyController.getInstance().show().subList(5,10));
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

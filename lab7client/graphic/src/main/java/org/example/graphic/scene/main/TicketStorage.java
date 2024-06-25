package org.example.graphic.scene.main;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;
import org.common.dto.Ticket;
import org.controller.MyController;
import org.example.graphic.scene.main.command.filter.Filter;
import org.example.graphic.scene.main.command.filter.TicketFilter;
import org.example.graphic.scene.main.draw.animation.AnimatedTicket;
import org.example.graphic.scene.main.draw.entity.CommonTicket;
import org.example.graphic.scene.main.draw.entity.DrawingTicket;
import org.example.graphic.scene.main.draw.entity.SelectedTicket;

import java.util.*;
import java.util.stream.Collectors;

@Setter     @Getter
 public class TicketStorage {
    private ObservableList<Ticket> data;
    private ObservableList<DrawingTicket> wrappedFilteredData = FXCollections.observableArrayList();
    private ObservableList<Ticket> filteredData =   FXCollections.observableArrayList();
    public void updateFilteredData(){
        wrappedFilteredData.setAll(wrappedData.stream().filter(ticket -> filter.check(ticketFilter,ticket)).toList());
        filteredData.setAll(wrappedFilteredData.stream().map(DrawingTicket::getTicket).toList());
    }


    private  ObservableList<DrawingTicket> wrappedData =   FXCollections.observableArrayList();
     private Filter filter = new Filter();
    private TicketFilter ticketFilter = new TicketFilter();
     private final HashMap<String,Color> createdByAndColor = new HashMap<>();

    public TicketStorage() {
        try {
            data = FXCollections.observableArrayList(MyController.getInstance().show());

            data.addListener((ListChangeListener.Change<? extends Ticket> change) -> {
                        while (change.next()){
                            if (change.wasRemoved()){
                                wrappedData.removeIf(drawingTicket -> change.getRemoved().contains(drawingTicket.getTicket()));
                            }
                            if (change.wasAdded()){
                                wrappedData.addAll(change.getAddedSubList().stream().map(ticket ->{
                                    Color color = createdByAndColor.get(ticket.getCreatedBy());
                                    if (color == null){
                                        color = getRandomColor();
                                        createdByAndColor.put(ticket.getCreatedBy(),color);
                                    }
                                    return  new AnimatedTicket(ticket,color);
                                }).toList());
                            }
                        }
            });

            generateWrappedData();
            updateFilteredData();
            wrappedData.addListener((ListChangeListener.Change<? extends DrawingTicket> change) -> {
                while (change.next()) {
                    if (change.wasRemoved()) {
                        wrappedFilteredData.removeIf(drawingTicket -> change.getRemoved().contains(drawingTicket));
                        filteredData.removeIf(ticket -> change.getRemoved().stream().map(DrawingTicket::getTicket).toList().contains(ticket));
                    }
                    if (change.wasAdded()) {
                        var addedList = (List<DrawingTicket>) change.getAddedSubList().stream().filter(ticket -> filter.check(ticketFilter, ticket)).toList();
                        wrappedFilteredData.addAll(addedList);
                        filteredData.addAll(addedList.stream().map(DrawingTicket::getTicket).toList());
                    }
                }
            });

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    //TODO Переписать
    public void generateWrappedData() {
        Map<String, List<Ticket>> groupedByAuthor = data.stream()
                .collect(Collectors.groupingBy(Ticket::getCreatedBy));

        // Предполагаем, что это ваш метод, где groupedByAuthor и createdByAndColor являются полями класса или доступны в контексте метода.

// Создание потока обработки группированных билетов


        groupedByAuthor.forEach((key, value) -> {
            Color randomColor = getRandomColor();
            createdByAndColor.put(key, randomColor);
            value.forEach(ticket -> {
                wrappedData.add(new CommonTicket(ticket, randomColor));
            });
        });
// Сбор результата в коллекцию


    }
     public void makeTicketSelected(Long id){
         DrawingTicket drawingTicket = wrappedData.stream().filter(drawingTick -> drawingTick.getTicket().getId().equals(id)).findAny().get();
         wrappedData.remove(drawingTicket);
         wrappedData.add( new SelectedTicket(drawingTicket.getTicket(),drawingTicket.getColor()));


     }
     public void unmakeAllTicketsSelected(){
        var selectedTickets = wrappedData.stream().filter(ticket -> ticket instanceof SelectedTicket).toList();
        wrappedData.removeAll(selectedTickets);
        wrappedData.addAll(selectedTickets.stream().map(ticket -> new CommonTicket(ticket.getTicket(),((SelectedTicket) ticket).getOldColor())).toList());



     }
    private  Color getRandomColor(){
        Random random = new Random();
        double red = random.nextDouble();
        double green = random.nextDouble();
        double blue = random.nextDouble();
        Color randomColor = new Color(red, green, blue, 0.2);
        return randomColor;
    }

    public Optional<DrawingTicket> findSelectedTicket() {
        return wrappedFilteredData.stream().filter(drawingTicket -> drawingTicket instanceof SelectedTicket).findFirst();

    }
}

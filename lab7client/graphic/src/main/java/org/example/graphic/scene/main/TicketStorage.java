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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Setter     @Getter
 public class TicketStorage {
    private ObservableList<Ticket> data;
    private ObservableList<Ticket> filteredData;
    private  ObservableList<DrawingTicket> filteredWrappedData;
     private Filter filter = new Filter();
    private TicketFilter ticketFilter = new TicketFilter();
     private final HashMap<String,Color> createdByAndColor = new HashMap<>();

     public void filter(){
         filteredData.setAll(data.stream().filter(ticket -> filter.check(ticketFilter,ticket)).toList());
     }
    public TicketStorage() {
        try {
            data = FXCollections.observableArrayList(MyController.getInstance().show());
            filteredData = FXCollections.observableArrayList(data);
            generateFilteredWrappedData();
            data.addListener((ListChangeListener.Change<? extends Ticket> change) -> {
                while (change.next()) {
                    if (change.wasAdded()) {
                        var addedTickets = change.getAddedSubList();
                        var addedWrappedTickets = addedTickets.stream()
                                .filter(ticket -> filter.check(ticketFilter,ticket))
                                .map(ticket -> {
                                    Color color = createdByAndColor.get(ticket.getCreatedBy());
                                    if (color == null) {
                                        color = getRandomColor();
                                        createdByAndColor.put(ticket.getCreatedBy(),color);
                                    }
                                    return new AnimatedTicket(ticket,color);
                                })
                                .toList();
                        filteredData.addAll(addedTickets);
                    }
                    if (change.wasRemoved()){
                        var removedTickets = change.getRemoved();
                        filteredData.removeIf(ticket -> removedTickets.contains(ticket));
                    }
                }

            });
            filteredData.addListener((ListChangeListener.Change<? extends Ticket> change) -> {
                        while (change.next()){
                            if (change.wasRemoved()){
                                filteredWrappedData.removeIf(drawingTicket -> change.getRemoved().contains(drawingTicket.getTicket()));
                            }
                            if (change.wasAdded()){
                                filteredWrappedData.addAll(change.getAddedSubList().stream().map(ticket ->{
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

            generateFilteredWrappedData();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    //TODO Переписать
    public void generateFilteredWrappedData() {
        Map<String, List<Ticket>> groupedByAuthor = filteredData.stream()
                .collect(Collectors.groupingBy(Ticket::getCreatedBy));

        // Предполагаем, что это ваш метод, где groupedByAuthor и createdByAndColor являются полями класса или доступны в контексте метода.

// Создание потока обработки группированных билетов
        filteredWrappedData = FXCollections.observableArrayList();

        groupedByAuthor.forEach((key, value) -> {
            Color randomColor = getRandomColor();
            createdByAndColor.put(key, randomColor);
            value.forEach(ticket -> {
                filteredWrappedData.add(new CommonTicket(ticket, randomColor));
            });
        });
// Сбор результата в коллекцию


    }
     public void makeTicketSelected(Long id){
         filteredWrappedData.replaceAll(drawingTicket -> {
             if (drawingTicket.getTicket().getId().equals(id)){
                 return new SelectedTicket(drawingTicket.getTicket(),drawingTicket.getColor());
             }else{
                 return drawingTicket;
             }
         });

     }
     public void unmakeAllTicketsSelected(){
         filteredWrappedData.replaceAll(drawingTicket -> {
             if (drawingTicket instanceof SelectedTicket){
                 return new CommonTicket(drawingTicket.getTicket(),((SelectedTicket) drawingTicket).getOldColor());
             }else{
                 return drawingTicket;
             }
         });
         System.out.println(filteredWrappedData);

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

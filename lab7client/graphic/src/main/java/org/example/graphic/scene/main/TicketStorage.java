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
import org.example.graphic.scene.main.draw.entity.CommonTicket;
import org.example.graphic.scene.main.draw.entity.DrawingTicket;
import org.example.graphic.scene.main.draw.entity.SelectedTicket;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

 @Setter     @Getter
 public class TicketStorage {
    private ObservableList<Ticket> data;
    private ObservableList<Ticket> filteredData;
    private  ObservableList<DrawingTicket> filteredWrappedData;
     private Filter filter = new Filter();
    private TicketFilter ticketFilter;

     public void filter(){
    filteredData.setAll(data);
    filter.filter(ticketFilter,filteredData);
    generateFilteredWrappedData();
     }
    public TicketStorage() {
        try {
            data = FXCollections.observableArrayList(MyController.getInstance().show());
            filteredData = FXCollections.observableArrayList(data);
            generateFilteredWrappedData();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    //TODO Переписать
    public void generateFilteredWrappedData() {
        Map<String, List<Ticket>> groupedByAuthor = filteredData.stream()
                .collect(Collectors.groupingBy(Ticket::getCreatedBy));

       filteredWrappedData = groupedByAuthor.entrySet().stream()
                .flatMap(entry -> {
                    Color randomColor = getRandomColor();
                    return entry.getValue().stream()
                            .map(ticket -> new CommonTicket(ticket, randomColor));
                })
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        System.out.println(filteredWrappedData);

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

package org.example.graphic.scene.main.command.filter;


import javafx.collections.ObservableList;
import org.common.dto.Ticket;

import java.util.List;
import java.util.stream.Collectors;
public class Filter {
    public void filter(TicketFilter ticketFilter, ObservableList<Ticket> tickets) {
        if (ticketFilter!=null) {
            List<Ticket> filteredTickets = tickets.stream()
                    .filter(ticket -> check(ticketFilter, ticket))
                    .collect(Collectors.toList());
            tickets.setAll(filteredTickets);
        }
    }

    public boolean check(TicketFilter ticketFilter, Ticket ticket){
        var res = true;
        if (ticketFilter.getIdMin()!= null){
            res = res && ticket.getId() >= ticketFilter.getIdMin();
        }
        if (ticketFilter.getIdMax()!= null){
            res = res && ticket.getId() <= ticketFilter.getIdMax();
        }
        if (ticketFilter.getDateMin()!= null){
            res = res && !ticket.getCreationDate().isBefore(ticketFilter.getDateMin());
        }
        if (ticketFilter.getDateMax()!= null){
            res = res && !ticket.getCreationDate().isAfter(ticketFilter.getDateMax());
        }

        if (ticketFilter.getPriceMin()!= null && ticket.getPrice()!=null){
            res = res && ticket.getPrice() >= ticketFilter.getPriceMin();
        }
        if (ticketFilter.getPriceMax()!= null && ticket.getPrice()!=null){
            res = res && ticket.getPrice() <= ticketFilter.getPriceMax();
        }
        if (ticketFilter.getDiscountMin()!= null  && ticket.getDiscount()!=null){
            res = res && ticket.getDiscount() >= ticketFilter.getDiscountMin();
        }

        if (ticketFilter.getVenueFilter().getCapacityMin()!= null  && ticket.getVenue().getCapacity()!=null){
            res = res &&  ticket.getVenue().getCapacity() >= ticketFilter.getVenueFilter().getCapacityMin();
        }

        if (ticketFilter.getVenueFilter().getCapacityMax()!= null  && ticket.getVenue().getCapacity()!=null){
            res = res &&  ticket.getVenue().getCapacity() <= ticketFilter.getVenueFilter().getCapacityMax();
        }

        if (ticketFilter.getCoordinatesFilter().getXMin()!= null  && ticket.getCoordinates().getX()!=null){
            res = res &&   ticket.getCoordinates().getX() >= ticketFilter.getCoordinatesFilter().getXMin();
        }


        if (ticketFilter.getCoordinatesFilter().getXMax()!= null  && ticket.getCoordinates().getX()!=null){
            res = res &&   ticket.getCoordinates().getX() <= ticketFilter.getCoordinatesFilter().getXMax();
        }

        if (ticketFilter.getCoordinatesFilter().getYMin()!= null){
            res = res &&   ticket.getCoordinates().getY() >= ticketFilter.getCoordinatesFilter().getYMin();
        }


        if (ticketFilter.getCoordinatesFilter().getYMax()!= null){
            res = res &&   ticket.getCoordinates().getY() <= ticketFilter.getCoordinatesFilter().getYMax();
        }

        if (ticketFilter.getVenueFilter().getPartOfName()!= null  && ticket.getVenue().getName()!=null){
            res = res && ticket.getVenue().getName().contains(ticketFilter.getVenueFilter().getPartOfName()) ;
        }
        if (ticketFilter.getPartOfName()!= null  && ticket.getName()!=null){
            res = res && ticket.getName().contains(ticketFilter.getPartOfName()) ;
        }
        res = res && ticketFilter.getTicketTypes().contains(ticket.getTicketType());
        res = res && ticketFilter.getVenueFilter().getVenueTypes().contains(ticket.getVenue().getVenueType());
        res = res && ticketFilter.getRefundable().contains(ticket.getRefundable());
        return res;


    }
}

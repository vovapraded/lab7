package org.controller.connector.to.client;

import org.common.dto.Ticket;
import org.example.connector.to.controller.ConsoleEventListener;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class ConsoleEventListenerImpl implements ConsoleEventListener {

    private final Queue<String> queueMessage = new ArrayDeque<String>() ;
    private final Queue<List<Ticket>> queueTicket = new ArrayDeque<List<Ticket>>() ;

    @Override
    public void onEvent(String message) {
        queueMessage.add(message);
    }

    @Override
    public void onEvent(List<Ticket> tickets) {
        queueTicket.add(tickets);
    }

    public String getMessage(){
       String message = null;
        while (message==null){
            message= queueMessage.poll();
        }
        return message;
    }
    public List<Ticket> getTickets(){
        List<Ticket> tickets = null;
        while (tickets==null){
            tickets= queueTicket.poll();
        }
        return tickets;
    }
}

package org.controller.connector.to.client;

import org.common.dto.Ticket;
import org.controller.exception.ExceptionFromServer;
import org.example.connector.to.controller.ConsoleEventListener;
import org.example.utility.NoResponseException;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class ConsoleEventListenerImpl implements ConsoleEventListener {

    private final Queue<String> queueMessage = new ArrayDeque<String>() ;
    private final Queue<Exception> queueException = new ArrayDeque<Exception>() ;
    private final Queue<List<Ticket>> queueTicket = new ArrayDeque<List<Ticket>>() ;

    @Override
    public void onEvent(String message) {
        queueMessage.add(message);
    }

    @Override
    public void onEvent(Exception e){
        queueException.add(e);

    }

    @Override
    public void onEvent(List<Ticket> tickets) {

        queueTicket.add(tickets);
    }

    public String getMessage() throws Exception{
       String message = null;
        Exception e = null;
        while (message==null){
            message= queueMessage.poll();
            e = queueException.poll();
            if (e!=null) throw e;
        }

        return message;
    }
    public List<Ticket> getTickets() throws Exception{
        List<Ticket> tickets = null;
        Exception e = null;
        while (tickets==null){
            tickets= queueTicket.poll();
            e = queueException.poll();
            if (e!=null) throw e;
        }
        return tickets;
    }
}

package org.example.managers;

import org.common.commands.Command;
import org.common.dto.Ticket;
import org.common.managers.ResponseManager;
import org.common.network.Response;
import org.example.connection.ResponsePublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CurrentResponseManager implements ResponseManager {
    private static final Logger logger = LoggerFactory.getLogger(CurrentResponseManager.class);

    private HashMap<Command,Response> responses = new HashMap<>();
    @Override
    public void initResponse(Command command, Response response){
        responses.put(command,response);
    }
    @Override
    public Response getResponse(Command command){
        return  responses.get(command);
    }
    @Override
    public void addToSend(String s, Command command){
        responses.get(command).getMessage().add(s);

    }

    @Override
    public void addToSend(List<Ticket> tickets, Command command) {
        if (responses.get(command).getTickets()==null){
            responses.get(command).setTickets( new ArrayList<Ticket>());
        }
        responses.get(command).getTickets().addAll(tickets);
    }
    @Override
    public void setException(Exception e,Command command) {

        responses.get(command).setException(e);
    }

    @Override
    public void send(Command command){
        var response = responses.get(command);
        ResponsePublisher.sendResponse(response, response.getAddress());
        logger.debug("Респонс на комманду "+command.getClass().getName()+" отправлен по адрессу "+response.getAddress());

    }
}

package org.common.managers;

import org.common.commands.Command;
import org.common.dto.Ticket;
import org.common.network.Response;

import java.util.List;

public interface ResponseManager {

    void initResponse(Command command, Response response);

    Response getResponse(Command command);

    void addToSend(String s, Command command);

    void addToSend(List<Ticket> tickets, Command command);

    void send(Command command);
}

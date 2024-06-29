package org.controller;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.common.commands.*;
import org.common.commands.authorization.Login;
import org.common.commands.authorization.Register;
import org.common.commands.inner.objects.Authorization;
import org.common.dto.Ticket;
import org.controller.connector.to.client.ConsoleEventListenerImpl;
import org.example.Main;
import org.example.utility.ValidatorTicket;

import java.util.List;
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MyController {
     {
        Initializer.init();
    }
    @Getter
    private static final MyController instance = new MyController();
    private final ConsoleEventListenerImpl listener = Initializer.getListener();
    private final Authorization authorization = new Authorization();
    private final ValidatorTicket validatorTicket= new ValidatorTicket();
    public synchronized   String login(String login,String password) throws Exception {
        authorization.setLogin(login);
        authorization.setPassword(password);
        Login command = new Login();
        sendCommand(command);
        return  listener.getMessage();

    }
    public synchronized String register(String login, String password) throws Exception {
        authorization.setLogin(login);
        authorization.setPassword(password);
        Register command = new Register();
        sendCommand(command);
        return  listener.getMessage();
    }
    public synchronized List<Ticket> show() throws Exception {
        Show command = new Show();
        sendCommand(command);
        return  listener.getTickets();
    }
    public synchronized ImmutablePair<String, List<Ticket>> remove(Long id) throws Exception {
        RemoveKey command = new RemoveKey();
        command.setStringArg(id.toString());
        sendCommand(command);
        return  listener.getMessageAndTickets();
    }
    public synchronized ImmutablePair<String, List<Ticket>> insert(Ticket ticket) throws Exception {
        validatorTicket.validateTicket(ticket);
        Insert command = new Insert();
        command.setTicketArg(ticket);
        command.setStringArg(ticket.getId().toString());
        sendCommand(command);

        return   listener.getMessageAndTickets();


    }
    public synchronized ImmutablePair<String, List<Ticket>> update(Ticket ticket) throws Exception {
        Update command = new Update();
        command.setTicketArg(ticket);
        command.setStringArg(ticket.getId().toString());
        sendCommand(command);
        return  listener.getMessageAndTickets();

    }
    private synchronized void sendCommand(Command command) throws Exception {
        command.setAuthorization(authorization);
        Main.handleCommand(command);
    }

//    private S
}

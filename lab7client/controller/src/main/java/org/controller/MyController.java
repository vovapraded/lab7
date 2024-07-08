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
import org.common.network.Response;
import org.example.Main;
import org.example.utility.ValidatorTicket;

import java.util.List;
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MyController {
    
    @Getter
    private static final MyController instance = new MyController();
    private final Authorization authorization = new Authorization();
    private final ValidatorTicket validatorTicket= new ValidatorTicket();
    public synchronized   String login(String login,String password) throws Exception {
        authorization.setLogin(login);
        authorization.setPassword(password);
        Login command = new Login();
        var resp = sendCommand(command);
        return  resp.getMessageBySingleString();

    }
    public synchronized String register(String login, String password) throws Exception {
        authorization.setLogin(login);
        authorization.setPassword(password);
        Register command = new Register();
        var resp = sendCommand(command);
        return  resp.getMessageBySingleString();
    }
    public synchronized List<Ticket> show() throws Exception {
        Show command = new Show();
        var resp = sendCommand(command);
        return  resp.getTickets();
    }
    public synchronized ImmutablePair<String, List<Ticket>> remove(Long id) throws Exception {
        RemoveKey command = new RemoveKey();
        command.setStringArg(id.toString());
        var resp = sendCommand(command);

        return  new ImmutablePair<>(resp.getMessageBySingleString(),resp.getTickets());
    }
    public synchronized ImmutablePair<String, List<Ticket>> insert(Ticket ticket) throws Exception {
        validatorTicket.validateTicket(ticket);
        Insert command = new Insert();
        command.setTicketArg(ticket);
        command.setStringArg(ticket.getId().toString());
        var resp = sendCommand(command);


        return  new ImmutablePair<>(resp.getMessageBySingleString(),resp.getTickets());


    }
    public synchronized ImmutablePair<String, List<Ticket>> update(Ticket ticket) throws Exception {
        Update command = new Update();
        command.setTicketArg(ticket);
        command.setStringArg(ticket.getId().toString());
        var resp = sendCommand(command);
        return  new ImmutablePair<>(resp.getMessageBySingleString(),resp.getTickets());

    }
    private Response sendCommand(Command command) throws Exception {
        command.setAuthorization(authorization);
        var respOpt = Main.handleCommand(command);
        if (respOpt.isPresent()){
            var resp = respOpt.get();
            System.out.println("Ответ на командку "+command.getClass()+resp.getMessageBySingleString());
            if (resp.getException()!=null){
                throw resp.getException();
            }
            return resp;
        }else {
            //поменять
            return new Response();
        }
    }

//    private S
}

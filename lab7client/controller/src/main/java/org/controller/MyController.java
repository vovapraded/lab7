package org.controller;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.common.commands.Command;
import org.common.commands.Insert;
import org.common.commands.Show;
import org.common.commands.authorization.Login;
import org.common.commands.authorization.Register;
import org.common.commands.inner.objects.Authorization;
import org.common.dto.Ticket;
import org.controller.connector.to.client.ConsoleEventListenerImpl;
import org.example.Main;

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
    public  String login(String login,String password) throws Exception {
        authorization.setLogin(login);
        authorization.setPassword(password);
        Login command = new Login();
        sendCommand(command);
        return  listener.getMessage();

    }
    public String register(String login, String password) throws Exception {
        authorization.setLogin(login);
        authorization.setPassword(password);
        Register command = new Register();
        sendCommand(command);
        return  listener.getMessage();
    }
    public List<Ticket> show() throws Exception {

        Show command = new Show();
        sendCommand(command);
        return  listener.getTickets();
    }
    public String insert(Ticket ticket) throws Exception {

        Insert command = new Insert();
        command.setTicketArg(ticket);
        command.setStringArg(ticket.getId().toString());
        sendCommand(command);
        return  listener.getMessage();

    }
    private  void sendCommand(Command command) throws Exception {
        command.setAuthorization(authorization);
        Main.handleCommand(command);
    }

//    private S
}

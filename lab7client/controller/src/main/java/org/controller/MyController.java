package org.controller;

import org.common.commands.Command;
import org.common.commands.Show;
import org.common.commands.authorization.Login;
import org.common.commands.authorization.Register;
import org.common.commands.inner.objects.Authorization;
import org.common.dto.Ticket;
import org.controller.connector.to.client.ConsoleEventListenerImpl;
import org.example.Main;

import java.util.List;

public class MyController {
    {
        Initializer.init();
    }
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
    private  void sendCommand(Command command) throws Exception {
        command.setAuthorization(authorization);
        Main.handleCommand(command);
    }

//    private S
}
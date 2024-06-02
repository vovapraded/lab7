package org.controller;

import org.common.commands.Command;
import org.common.commands.authorization.Login;
import org.common.commands.authorization.Register;
import org.common.commands.inner.objects.Authorization;
import org.controller.connector.to.client.ConsoleEventListenerImpl;
import org.example.Main;

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
    private  void sendCommand(Command command) throws Exception {
        command.setAuthorization(authorization);
        Main.handleCommand(command);
    }

//    private S
}

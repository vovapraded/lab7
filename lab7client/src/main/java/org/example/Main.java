package org.example;

import lombok.Getter;
import org.common.commands.Command;
import org.common.utility.Console;
import org.example.authorization.AuthorizationManager;
import org.example.commands.ClientCommand;
import org.example.connection.UdpClient;
import org.example.connector.to.controller.ConsoleEventPublisher;
import org.example.utility.CurrentConsole;

import java.io.IOException;

/**
 *Main class
 */

public class Main {

    private final static UdpClient udpClient =  UdpClient.getInstance();
    @Getter
    private final static ConsoleEventPublisher consoleEventPublisher = new ConsoleEventPublisher();
    private  static final Console currentConsole = CurrentConsole.getInstance();



    public static void main(String[] args) throws IOException {
    }
    public static void sendMessageToController(String message){
        consoleEventPublisher.sendMessageToController(message);
    }
    public static void handleCommand(Command command) throws Exception{
                    //получаем команду
                    //если команда не клиентская
                    if (!(command instanceof ClientCommand)) {
                            udpClient.getUdpSender().sendCommand(command);
                            var resp = udpClient.getUdpReceiver().getResponse(false);
                            if (!resp.isPasswordCorrect() || !resp.isLoginCorrect()) {
                                AuthorizationManager.resetAuth();
                            }
                            currentConsole.print(resp.getMessageBySingleString());


                    }else {
                        command.setConsole(currentConsole);
                        command.execute();
                    }


            }


}
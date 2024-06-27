package org.example;

import lombok.Getter;
import org.common.commands.Command;
import org.common.dto.Ticket;
import org.common.utility.Console;
import org.example.authorization.AuthorizationManager;
import org.example.commands.ClientCommand;
import org.example.connection.UdpClient;
import org.example.connector.to.controller.ConsoleEventPublisher;
import org.example.utility.CurrentConsole;
import org.example.utility.NoResponseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    public static void handleCommand(Command command) throws Exception{
                    //получаем команду
                    //если команда не клиентская
                    if (!(command instanceof ClientCommand)) {
                            udpClient.getUdpSender().sendCommand(command);
                            try {
                                var resp = udpClient.getUdpReceiver().getResponse(false);
                                if (!resp.isPasswordCorrect() || !resp.isLoginCorrect()) {
                                    AuthorizationManager.resetAuth();
                                }
                                var message = resp.getMessageBySingleString();
                                if (resp.getException()!=null){
                                    currentConsole.sendToController(resp.getException());

                                }
                                if (!message.isBlank()){
                                    currentConsole.sendToController(message);
                                }
                                if (resp.getTickets()!=null){
                                    currentConsole.sendToController(resp.getTickets());
                                }

                            }catch (NoResponseException e){
                                currentConsole.sendToController(e);
                            }




                    }else {
                        command.setConsole(currentConsole);
                        command.execute();
                    }


            }


}
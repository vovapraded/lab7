package org.example;

import lombok.Getter;
import org.common.commands.Command;
import org.common.network.Response;
import org.common.utility.Console;
import org.example.authorization.AuthorizationManager;
import org.example.commands.ClientCommand;
import org.example.connection.ResponseDistributor;
import org.example.connection.UdpClient;
import org.example.utility.CurrentConsole;

import java.io.IOException;
import java.util.Optional;

/**
 *Main class
 */

public class Main {

    private final static UdpClient udpClient =  UdpClient.getInstance();
    private final static ResponseDistributor responseDistributor = new ResponseDistributor(udpClient.getUdpReceiver());
    @Getter
    private  static final Console currentConsole = CurrentConsole.getInstance();



    public static void main(String[] args) throws IOException {
    }

    public static Optional<Response> handleCommand(Command command) throws Exception{
                    //получаем команду
                    //если команда не клиентская
                    if (!(command instanceof ClientCommand)) {
                            var requestId=udpClient.getUdpSender().sendCommand(command);
                                var resp = responseDistributor.getResponse(requestId,command);
                                if (!resp.isPasswordCorrect() || !resp.isLoginCorrect()) {
                                    AuthorizationManager.resetAuth();
                                }
                                return Optional.of(resp);

                    }else {
                        command.setConsole(currentConsole);
                        command.execute();
                        return Optional.empty();
                    }


            }


}
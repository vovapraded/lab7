package org.example.utility;

import java.io.IOException;

import org.common.commands.authorization.AuthorizationCommand;
import org.common.network.ConnectionException;
import org.common.network.SendException;
import org.common.serial.DeserializeException;
import org.common.serial.SerializeException;
import org.example.authorization.AuthorizationManager;
import org.example.authorization.NoAuthorizationException;
import org.example.connection.UdpClient;
import org.example.managers.*;
import org.common.utility.*;
/**
 *Main class
 */

public class Main {

    public static void main(String[] args) throws IOException {
        UdpClient udpClient =  UdpClient.getInstance();
        CurrentConsole currentConsole = CurrentConsole.getInstance();
        CreatorOfCommands creator = new CreatorOfCommands();
        ParseInput parseInput = new ParseInput();
        while (true) {
            String s = currentConsole.getInput();
            if (!s.isEmpty()) {
                try {
                    parseInput.parseInput(s);
                    String cmd = parseInput.getArg1();
                    String arg2 = parseInput.getArg2();
                    if (parseInput.getArg3Exist() == 1) {
                        throw new InvalidFormatException("Слишком много аргументов");
                    }
                   var command = creator.createCommand(cmd, arg2);
                    if (command!= null) {
                        try {
                            udpClient.getUdpSender().sendCommand(command);
                            var resp = udpClient.getUdpReceiver().getResponse(false);
                            if (!resp.isPasswordCorrect() || !resp.isLoginCorrect()) {
                                AuthorizationManager.resetAuth();
                            }
                            currentConsole.print(resp.getMessageBySingleString());
                        } catch (NoResponseException | SendException | SerializeException | DeserializeException |
                                 ConnectionException e) {
                            currentConsole.print(e.getMessage());
                        }
                    }

                } catch (InvalidFormatException | NoAuthorizationException e) {
                    currentConsole.print(e.getMessage());
                }
            }

        }
    }
}
package org.example.commands;

import org.common.commands.Command;
import org.example.connection.UdpClient;
import org.example.utility.CurrentConsole;
import org.example.utility.NoResponseException;

public class Empty extends Command implements ClientCommand {
    @Override
    public void execute() {
       UdpClient udpClient = UdpClient.getInstance();
        try {
            console.print(udpClient.getResponse(true).getMessageBySingleString());
        }catch (NoResponseException e){
            console.print(e.getMessage());
        }
    }

    @Override
    public void validate(String arg1) {
    }
}
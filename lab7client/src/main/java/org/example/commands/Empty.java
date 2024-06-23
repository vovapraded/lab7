package org.example.commands;

import org.common.commands.Command;
import org.common.serial.DeserializeException;
import org.example.connection.UdpClient;
import org.example.utility.NoResponseException;

public class Empty extends Command implements ClientCommand {
    @Override
    public void execute() {
       UdpClient udpClient = UdpClient.getInstance();
        try {
            var response = udpClient.getUdpReceiver().getResponse(true);

            console.sendToController(response.getMessageBySingleString(),response.isThereEx());
        }catch (NoResponseException | DeserializeException e){
            System.out.println(e.getMessage());
            console.sendToController(e.getMessage(),true);
        }
    }

    @Override
    public void validate(String arg1) {
    }
}

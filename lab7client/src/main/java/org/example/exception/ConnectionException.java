package org.example.exception;

import lombok.Getter;
import lombok.Setter;
import org.common.commands.Command;

public class ConnectionException extends ReceivingException{

    public ConnectionException(String message, Command command) {
        super(message, command);
    }
}

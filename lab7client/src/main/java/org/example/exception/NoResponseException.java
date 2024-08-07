package org.example.exception;

import lombok.Getter;
import lombok.Setter;
import org.common.commands.Command;

public class NoResponseException extends ReceivingException{

    public NoResponseException(String message, Command command) {
        super(message, command);
    }
}

package org.example.exception;

import lombok.Getter;
import lombok.Setter;
import org.common.commands.Command;

public class ReceivingException extends RuntimeException {
    @Setter
    @Getter
    private Command command;

    public ReceivingException(String message, Command command) {
        super(message);
        this.command = command;
    }
}

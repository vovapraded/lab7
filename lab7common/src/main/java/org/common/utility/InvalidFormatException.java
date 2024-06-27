package org.common.utility;

import lombok.Getter;
import lombok.Setter;
import org.common.commands.Command;

import java.io.Serializable;
import java.net.SocketAddress;

public class InvalidFormatException extends RuntimeException  implements Serializable {
    @Getter @Setter
private transient   Command command;
    public InvalidFormatException(String message, Command command) {
        super(message);
        this.command = command;
    }
    public InvalidFormatException(String message) {
        super(message);
    }

}

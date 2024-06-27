package org.common.commands.authorization;


import lombok.Getter;
import lombok.Setter;
import org.common.commands.Command;

import java.io.Serializable;

public class NoAccessException extends RuntimeException implements Serializable {

    public NoAccessException(String message) {
        super(message);
    }

}

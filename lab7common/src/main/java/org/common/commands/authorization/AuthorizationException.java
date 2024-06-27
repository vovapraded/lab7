package org.common.commands.authorization;

import java.io.Serializable;

public class AuthorizationException extends RuntimeException implements Serializable {
    public AuthorizationException(String message) {
        super(message);
    }

}

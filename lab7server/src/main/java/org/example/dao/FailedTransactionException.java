package org.example.dao;

import java.io.Serializable;

public class FailedTransactionException extends RuntimeException implements Serializable {
    public FailedTransactionException(String message) {
        super(message);
    }

}

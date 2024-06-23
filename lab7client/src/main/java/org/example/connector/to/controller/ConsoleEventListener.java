package org.example.connector.to.controller;

import org.common.dto.Ticket;

import java.util.ArrayList;
import java.util.List;

public interface ConsoleEventListener {
    void onEvent(String message,boolean isThereEx);
    void onEvent(List<Ticket> tickets);

}

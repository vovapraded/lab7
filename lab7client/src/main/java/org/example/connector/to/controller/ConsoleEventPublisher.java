package org.example.connector.to.controller;

import org.common.commands.Command;

import java.util.ArrayList;
import java.util.List;

public class ConsoleEventPublisher {
    private List<ConsoleEventListener> listeners = new ArrayList<>();
    // Метод для добавления слушателя
    public void addListener(ConsoleEventListener listener) {
        listeners.add(listener);
    }
    // Метод для удаления слушателя
    public void removeListener(ConsoleEventListener listener) {
        listeners.remove(listener);
    }
    public void sendMessageToController(String message) {
        for (ConsoleEventListener listener : listeners) {
            listener.onEvent(message);
        }
    }
}

package org.controller.connect.to.graphic;

import java.util.ArrayList;
import java.util.List;

public class ExceptionPublisher {
    private static final List<ExceptionListener> listeners = new ArrayList<>();

    public static void addEventListener(ExceptionListener listener) {
        listeners.add(listener);
    }

    public static void removeEventListener(ExceptionListener listener) {
        listeners.remove(listener);
    }

    public static void notifyListeners(Exception e) {
        for (ExceptionListener listener : listeners) {
            listener.onEvent(e);
        }
    }
}

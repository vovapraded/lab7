package org.example.connection;

import org.common.network.Response;

import java.net.SocketAddress;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

public class ResponsePublisher {
    private static List<ResponseListener> listeners = new ArrayList<>();

    // Метод для добавления слушателей
    public static void addListener(ResponseListener listener) {
        listeners.add(listener);
    }

    // Метод для генерации события
    public static  void sendResponse(Response response, SocketChannel channel) {
        for (ResponseListener listener : listeners) {
            listener.onResponse(response,channel);

        }
    }
}

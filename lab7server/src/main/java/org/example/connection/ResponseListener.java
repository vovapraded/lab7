package org.example.connection;

import org.common.network.Response;

import java.net.SocketAddress;
import java.nio.channels.SocketChannel;

public interface ResponseListener {
    void onResponse(Response response, SocketChannel channel);
}

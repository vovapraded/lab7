package org.example.connection;

import org.common.network.RequestId;
import org.common.network.Response;

import java.net.SocketAddress;

public interface ResponseListener {
    void onResponse(Response response, RequestId requestId);
}

package org.common.network;

import lombok.Getter;
import lombok.ToString;

import java.net.SocketAddress;
@Getter
@ToString
public class RequestId {
    private final SocketAddress address;
    private final byte id;

    public RequestId(SocketAddress address,byte id ) {
        this.id = id;
        this.address = address;
    }
    public RequestId(byte id ) {
        this.id = id;
        this.address = null;
    }
}

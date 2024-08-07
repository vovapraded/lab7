package org.common.network;

import lombok.Getter;
import lombok.ToString;

import java.net.SocketAddress;
@Getter
@ToString
public class RequestId {
    private final SocketAddress address;
    private final Byte id;

    public RequestId(SocketAddress address,Byte id ) {
        this.id = id;
        this.address = address;
    }
    public RequestId(Byte id ) {
        this.id = id;
        this.address = null;
    }
}

package org.example.connection;

import lombok.Builder;
import lombok.Getter;

@Builder @Getter
public class ReceivedPacket {
    private final int number;
    private final byte[] data;

}

package org.example.connection;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
@Getter @Setter
public class ReceivedPacketsList {
    private final ArrayList<ReceivedPacket> packets = new ArrayList<ReceivedPacket>();
    private volatile int sizeOfRequest = Integer.MAX_VALUE;
    private volatile long timeOfLastPacket = System.currentTimeMillis();

}

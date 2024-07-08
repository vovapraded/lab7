package org.example.connection;

import com.google.common.primitives.Bytes;
import lombok.SneakyThrows;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.common.network.ConnectionException;
import org.common.network.RequestId;
import org.common.network.Response;
import org.common.serial.Deserializer;
import org.common.utility.CodingUtil;
import org.example.utility.NoResponseException;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class UdpReceiver {
    private final int PACKET_SIZE = UdpClient.getPACKET_SIZE();
    private final int DATA_SIZE = UdpClient.getDATA_SIZE();
    //Лист пакетов
    private final ConcurrentHashMap<Byte, List<ImmutablePair<byte[], Integer>>> result = new ConcurrentHashMap<Byte, List<ImmutablePair<byte[], Integer>>>();

    private final DatagramChannel client;
    private final PacketReceiver packetReceiver;
    private final PacketHandler packetHandler;
    private final AtomicInteger countOfRequests = new AtomicInteger(0);
    private final TimeoutChecker timeoutChecker = new TimeoutChecker();

    public UdpReceiver(DatagramChannel client) {
        this.client = client;
        packetReceiver = new PacketReceiver(client);
        packetHandler = new PacketHandler();
    }

    public Response getResponse() throws NoResponseException {
        countOfRequests.incrementAndGet();
            packetReceiver.run(timeoutChecker);

            packetHandler.run();
        Future<Response> responseFuture = null;
        System.out.println("Пытаемся достать фьюче");
        while (packetHandler.isWork() && responseFuture == null) {
            responseFuture = packetHandler.getFuturesOfResponses().poll();
        }
        System.out.println("Достали фьюче"+responseFuture);
        countOfRequests.decrementAndGet();
        if (responseFuture == null)
            throw new NoResponseException("NoResponse");
        Response response = null;
        try {
            response = responseFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        if (countOfRequests.get() == 0) {
            timeoutChecker.shutdown();
        }
        return response;


//        return Deserializer.deserialize(receiveData(), Response.class);


    }
}

package org.example.connection;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.example.exception.ConnectionException;
import org.common.network.Response;
import org.example.exception.NoResponseException;
import org.example.exception.ReceivingException;

import java.nio.channels.DatagramChannel;
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

    public Response getResponse() throws ReceivingException {
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
            throw new NoResponseException("NoResponse",null);
        Response response = null;
        try {
            response = responseFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }finally {
            if (countOfRequests.get() == 0) {
                timeoutChecker.shutdown();
            }
        }

        return response;


//        return Deserializer.deserialize(receiveData(), Response.class);


    }
}

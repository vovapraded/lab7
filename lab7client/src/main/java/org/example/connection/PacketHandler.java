package org.example.connection;
import com.google.common.primitives.Bytes;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.common.network.RequestId;
import org.common.network.Response;
import org.common.serial.Deserializer;
import org.common.threads.ThreadHelper;
import org.common.utility.*;

import javax.swing.*;
import java.util.*;
import java.util.concurrent.*;


public class PacketHandler {
    @Setter
    private static volatile boolean interrupt = false;
    @Getter
    private static volatile boolean work=false ;

    private final ExecutorService poolForProcessing = ThreadHelper.getPoolForProcessing();
    private final int PACKET_SIZE = UdpClient.getPACKET_SIZE();
    private final int DATA_SIZE =UdpClient.getDATA_SIZE();

    private final BlockingQueue<CompletableFuture<ArrayList<byte[]>>> futures = PacketReceiver.getFutures();
    @Getter
    private  final BlockingQueue<Future<Response>> futuresOfResponses = new LinkedBlockingQueue<>() ;


    //Лист пакетов
    private final ConcurrentHashMap<Byte, ReceivedPacket> result = new ConcurrentHashMap<Byte,ReceivedPacket>();

    public void run(){
        if (!work){
            work=true;
            interrupt=false;
            CompletableFuture.runAsync(()->handleData());
        }
    }

    @SneakyThrows
    public void handleData() {
        while (!interrupt  || !futures.isEmpty()) {
            var future = futures.poll();
            if (future != null) {
                if (future.isDone()) {

                    if (!future.isCompletedExceptionally())
                        handleArrayData( future.get());
                    else {
                        try {
                            future.get();
                        }catch (Exception e){
                            while (e instanceof ExecutionException){
                                e = (Exception) e.getCause();
                            }
                            var resp =new Response();
                            resp.setException(e);
                            resp.setRequestId(new RequestId(null));
                            futuresOfResponses.put(CompletableFuture.supplyAsync(()->resp));
                        }
                    }
                } else {
                    futures.put(future);
                }
            }
        }
        work=false;
    }

    private void handleArrayData(ArrayList<byte[]> arrayData) {
        for (byte[] data : arrayData) {
            var responseId = data[data.length - 5];
            var lastChunks = Arrays.copyOfRange(data, DATA_SIZE + 1, PACKET_SIZE);
            var packetNumber = CodingUtil.decodingInt(lastChunks);
            data = Arrays.copyOf(data, DATA_SIZE);

            result.putIfAbsent(responseId, new ReceivedPacket());
            var receivedPacket = result.get(responseId);
            System.out.println("Пакет номер "+packetNumber+" запроса "+responseId+" обработан");
            var sizeOfResponse =receivedPacket.getSizeOfResponse();
            if (packetNumber < 0) {
                sizeOfResponse = -packetNumber;
            }
            receivedPacket.setSizeOfResponse(sizeOfResponse);
            var list = receivedPacket.getPackets();
            synchronized (list) {
                list.add(new ImmutablePair<byte[], Integer>(data, Math.abs(packetNumber)));
                if (list.size() == receivedPacket.getSizeOfResponse()) {
                    try {
                        futuresOfResponses.put(CompletableFuture.supplyAsync(()->sortPackets(list,responseId),poolForProcessing));
                        System.out.println("положили future");
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

    }

    private Response sortPackets(List<ImmutablePair<byte[], Integer>> list,byte responseId) {
        byte[] responseData;
        synchronized (list) {
            list.sort(Comparator.comparing(pair -> pair.getRight()));
            responseData = list.stream().map((pair) -> pair.getLeft()).reduce(new byte[0], (arr1, arr2) ->
                    Bytes.concat(arr1, arr2));
            list.clear();
        }
        var response = Deserializer.deserialize(responseData, Response.class);
        response.setRequestId(new RequestId(responseId));
        return response;

    }


}

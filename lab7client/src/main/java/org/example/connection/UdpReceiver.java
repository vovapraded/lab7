package org.example.connection;

import com.google.common.primitives.Bytes;
import lombok.SneakyThrows;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.common.network.ConnectionException;
import org.common.network.Response;
import org.common.serial.DeserializeException;
import org.common.serial.Deserializer;
import org.common.utility.CodingUtil;
import org.example.utility.NoResponseException;

import java.io.IOException;
import java.net.ConnectException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class UdpReceiver {
    private volatile boolean received = false;
    private volatile boolean timeoutIsPassed = false;
    private final int PACKET_SIZE ;
    private final int DATA_SIZE ;
    private final int TIMEOUT = 5000;
    private volatile int  sizeOfResponse = Integer.MAX_VALUE;

    private  final ExecutorService poolForReceiving = Executors.newFixedThreadPool(10);
//очередь массивов пакетов
    private final BlockingQueue<Future<ArrayList<byte[]>>> futures = new LinkedBlockingQueue<Future<ArrayList<byte[]>>>();

//Лист пакетов
    final List<ImmutablePair<byte[],Integer>> result = Collections.synchronizedList(new ArrayList<>());



    private final DatagramChannel client;

    public UdpReceiver(int packetSize, DatagramChannel client) {
        PACKET_SIZE = packetSize;
        DATA_SIZE =  PACKET_SIZE - 4;
        this.client = client;
    }

    private ArrayList<byte[]> receiveData(int bufferSize, Set<SelectionKey> keys) throws IOException, NoResponseException {
        var buffer = ByteBuffer.allocate(bufferSize);
        var iter = keys.iterator();
        var arrayData = new ArrayList<byte[]>();
        while (iter.hasNext()) {
            SelectionKey key = iter.next();
            iter.remove();
            while (key.isValid() && key.isReadable()) {
                byte[] data = new byte[bufferSize];
                client.receive(buffer);
                if (!buffer.hasRemaining()) {

                    buffer.flip();
                    buffer.get(data);
                    // Читаем данные из буфера
                    arrayData.add(data);

                    buffer.clear();
                }else {
//                    System.out.println(Thread.currentThread().getName()+" закончил считывание");
                    break;
                }
            }

        }
        return arrayData;
    }





    @SneakyThrows
    public Response getResponse(boolean isOnce) throws  NoResponseException, DeserializeException {
        init();
        var timeout = isOnce ? 1 : TIMEOUT;

        CompletableFuture<Void> futureReceiveData = CompletableFuture.supplyAsync(() -> {
            receiveData();
            return null;
        });

        var timeoutMonitoringThread =  new Thread(() -> {
            monitoringTimeout(timeout);
        }, "TimeoutMonitoringThread");
        timeoutMonitoringThread.start();

        CompletableFuture<Void> futureHandleData = CompletableFuture.supplyAsync(() -> {
            handleData();
            return null;
        });
        try {
            futureHandleData.get();
        }catch (Throwable e){
            while (e instanceof ExecutionException ){
                e = e.getCause();
            }
            throw e;
        }

        if (!received) throw new NoResponseException("Нет ответа");
        var binaryResponse=sortPackets();
        var response = Deserializer.deserialize(binaryResponse, Response.class);
        return response;



//        return Deserializer.deserialize(receiveData(), Response.class);
    }

    @SneakyThrows
    private void monitoringTimeout(int timeout){
        synchronized (this) {
            wait(timeout);
            timeoutIsPassed = !received;
        }
    }
    @SneakyThrows
    private void receiveData() throws NoResponseException {
        Selector selector = Selector.open();
        client.register(selector, SelectionKey.OP_READ);
        while (!received && !timeoutIsPassed) {
            var countKeys = selector.selectNow();
            if (countKeys != 0) {
                var future = CompletableFuture.supplyAsync(() -> {
                    try {
                        return receiveData(PACKET_SIZE, selector.selectedKeys());
                    } catch (IOException e) {
                        throw new ConnectionException("Ошибка подключения");
                    }
                },poolForReceiving);
                futures.put(future);
            }
        }
        selector.close();
    }
    @SneakyThrows
    private void handleData() {
        while (!received && (!timeoutIsPassed || !futures.isEmpty())) {
            var future = futures.poll();
            if (future != null) {
                if (future.isDone()) {
                    handleArrayData(future.get());
                } else {
                    futures.put(future);
                }
            }
        }
    }
        private void handleArrayData(ArrayList<byte[]> arrayData)  {
            for (byte[] data : arrayData){
                var lastChunks = Arrays.copyOfRange( data, DATA_SIZE, PACKET_SIZE);
                var packetNumber = CodingUtil.decodingInt(lastChunks);
                data = Arrays.copyOf(data, DATA_SIZE);
                if (packetNumber < 0) {
                    sizeOfResponse =  -packetNumber;
                }
                synchronized (result) {
                    result.add(new ImmutablePair<byte[], Integer>(data,  Math.abs(packetNumber)));
                }
                if (result.size() == sizeOfResponse){
                    received = true;
                    synchronized (this){
                        notify();
                    }
                }
            }

    }
    private byte[] sortPackets()  {
        result.sort(Comparator.comparing(pair -> pair.getRight()));
        var response = result.stream().map((pair) -> pair.getLeft()).reduce(new byte[0], (arr1, arr2) ->
                Bytes.concat(arr1, arr2));
        return response;
    }
    private void init(){
        received = false;
        timeoutIsPassed = false;
        sizeOfResponse = Integer.MAX_VALUE;
        futures.clear();
        result.clear();
    }



}

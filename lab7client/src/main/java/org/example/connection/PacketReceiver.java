package org.example.connection;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.example.exception.ConnectionException;
import org.common.threads.ThreadHelper;
import org.example.exception.NoResponseException;
import org.example.exception.ReceivingException;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.*;

public class PacketReceiver {
    private final DatagramChannel client;
    @Setter @Getter
    private static volatile boolean work = false;
    private final int PACKET_SIZE = UdpClient.getPACKET_SIZE();
    private final int DATA_SIZE =UdpClient.getDATA_SIZE();
    private static final ExecutorService poolForReceiving = ThreadHelper.getPoolForReceiving();
    //очередь массивов пакетов
    @Getter
    private static final BlockingQueue<CompletableFuture<ArrayList<byte[]>>> futures = new LinkedBlockingQueue<CompletableFuture<ArrayList<byte[]>>>();

    public PacketReceiver(DatagramChannel client) {
        this.client = client;
    }
    public void  run(TimeoutChecker timeoutChecker){
        if (!work) {
            work = true;
            CompletableFuture<Void> futureReceiveData = CompletableFuture.runAsync(() -> {
                receiveData();
            }, poolForReceiving);
        }
        timeoutChecker.run();

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
                } else {
//                    System.out.println(Thread.currentThread().getName()+" закончил считывание");
                    break;
                }
            }

        }
        return arrayData;
    }
    @SneakyThrows
    private  void receiveData() throws ReceivingException {
        try (Selector selector = Selector.open()) {
            client.register(selector, SelectionKey.OP_READ);
            CompletableFuture future = null;
            while (work) {
                var countKeys = selector.selectNow();
                if (countKeys != 0) {
                    future = CompletableFuture.supplyAsync(() -> {
                        try {
                            System.out.println(selector
                            );
                            System.out.println(selector.isOpen());
                            System.out.println(selector.selectedKeys());
                            return receiveData(PACKET_SIZE, selector.selectedKeys());
                        } catch (IOException e) {
                            throw new ConnectionException("ConnectionError", null);
                        }
                    }, poolForReceiving);
                    futures.put(future);
                }
            }
            while (!future.isDone()){

            }
        }
    }

}

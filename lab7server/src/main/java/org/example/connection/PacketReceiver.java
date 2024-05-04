package org.example.connection;

import lombok.SneakyThrows;
import org.apache.commons.lang3.tuple.ImmutablePair;
import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Selector;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;

import org.example.managers.CurrentResponseManager;
import org.example.threads.ThreadHelper;
import org.example.utility.ReceiveDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PacketReceiver extends Thread {
    private UdpServer udpServer;
    private static final int PACKET_SIZE = 1024;
    private final ServerSocketChannel serverSocketChannel;
    private final CurrentResponseManager responseManager;
    private final Selector selectorAccept = Selector.open()  ;
    private final Selector selectorRead = Selector.open();

    private static final Logger logger = LoggerFactory.getLogger(PacketReceiver.class);
    ExecutorService poolForReceiving;

    public PacketReceiver(ServerSocketChannel socket, CurrentResponseManager responseManager) throws IOException {
        this.serverSocketChannel = socket;
        this.responseManager = responseManager;
        serverSocketChannel.register(selectorAccept, SelectionKey.OP_ACCEPT);
        this.poolForReceiving= ThreadHelper.getPoolForReceiving();
    }
    public void run(){

            poolForReceiving.submit(()->{
                while (true) {
                    connectClients();
                }
            });
        while (true) {
            var data=receiveData();
            if (data!=null){
                poolForReceiving.submit(new PacketHandler(responseManager,data));
            }
        }
    }
    public void connectClients()  {
        try {
            selectorAccept.select();
            Set<SelectionKey> selectedKeys = selectorAccept.selectedKeys();
            for (SelectionKey key : selectedKeys) {
                if (key.isAcceptable()) {
                    var socketChannel = serverSocketChannel.accept();
                    if (socketChannel != null) {
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selectorRead, SelectionKey.OP_READ);
                        logger.debug("Клиент " + socketChannel.getRemoteAddress() + " подключен");
                    }
                }
            }
            selectedKeys.clear();

        } catch (IOException e) {
            throw new ReceiveDataException("Не удалось подключиться");
        }
    }

        public ImmutablePair<SocketChannel,byte[]> receiveData() throws ReceiveDataException {
        ByteBuffer buffer = ByteBuffer.allocate(PACKET_SIZE);
         try {
                selectorRead.selectNow();
                Set<SelectionKey> selectedKeys = selectorRead.selectedKeys();
                for (SelectionKey key : selectedKeys) {
                    if (key.isReadable()) {
                        buffer.clear();
                        var socketChannel=(SocketChannel) key.channel();
                        socketChannel.read(buffer);
                        buffer.flip();
                        byte[] data = new byte[buffer.remaining()];
                        buffer.get(data);
                        selectedKeys.clear();
                        return new ImmutablePair<>(socketChannel, data);
                    }
                }
            } catch (IOException e) {
                throw new ReceiveDataException("Не удалось получить данные");
            }
         return null;
    }


}

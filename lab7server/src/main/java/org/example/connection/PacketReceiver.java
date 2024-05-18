package org.example.connection;

import org.apache.commons.lang3.tuple.ImmutablePair;
import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.Selector;
import java.nio.channels.SelectionKey;
import java.util.*;
import java.util.concurrent.ExecutorService;

import org.example.managers.CurrentResponseManager;
import org.example.threads.ThreadHelper;
import org.example.utility.ReceiveDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PacketReceiver extends Thread {
    private UdpServer udpServer;
    private static final int PACKET_SIZE = 1024;
    private final DatagramChannel datagramChannel;
    private final CurrentResponseManager responseManager;
    private Selector selector;
    private static final Logger logger = LoggerFactory.getLogger(PacketReceiver.class);
    ExecutorService poolForReceiving;

    public PacketReceiver(DatagramChannel datagramChannel, CurrentResponseManager responseManager) throws IOException {
        this.datagramChannel = datagramChannel;
        this.responseManager = responseManager;
        this.selector = Selector.open();
        this.datagramChannel.register(selector, SelectionKey.OP_READ);
        this.poolForReceiving= ThreadHelper.getPoolForReceiving();
    }
    public void run(){
        while (true) {
            var data=receiveData();
            if (data!=null){
                poolForReceiving.submit(new PacketHandler(responseManager,data));
            }
        }
    }
    public ImmutablePair<SocketAddress,byte[]> receiveData() throws ReceiveDataException {
        ByteBuffer buffer = ByteBuffer.allocate(PACKET_SIZE);
        SocketAddress addr = null;
         try {
                selector.select();
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                for (SelectionKey key : selectedKeys) {
                    if (key.isReadable()) {
                        buffer.clear();
                        addr = datagramChannel.receive(buffer);
                        buffer.flip();
                        byte[] data = new byte[buffer.remaining()];
                        buffer.get(data);
                        return new ImmutablePair<>(addr, data);
                    }
                }
                selectedKeys.clear();
            } catch (IOException e) {
                throw new ReceiveDataException("Не удалось получить данные");
            }
         return null;
    }


}

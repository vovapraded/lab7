package org.example.connection;

import org.common.network.Response;
import org.common.serial.SerializeException;
import org.common.serial.Serializer;
import org.common.utility.PropertyUtil;
import org.example.managers.CurrentResponseManager;
import org.example.threads.HashmapCleaner;
import org.example.threads.ThreadHelper;
import org.example.utility.ReceiveDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.AbstractMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class UdpServer implements ResponseListener {

    private final InetSocketAddress serverAddress;

    private final ByteBuffer buffer = ByteBuffer.allocate(1024);
    private ConcurrentHashMap<SocketAddress, AbstractMap.SimpleEntry< byte[], Integer>> clients = new ConcurrentHashMap<>();

    private final CurrentResponseManager responseManager;
    private final int PACKET_SIZE = 1024;
    private final int DATA_SIZE = PACKET_SIZE - 1;

    private static final Logger logger = LoggerFactory.getLogger(UdpServer.class);
    private ServerSocketChannel serverSocketChannel;
    private final ResponseSender responseSender;
    private boolean running=true;

    public UdpServer(CurrentResponseManager responseManager) throws IOException {
        this.responseManager = responseManager;
        this.serverAddress = new InetSocketAddress(PropertyUtil.getAddress(),PropertyUtil.getPort());
        openNewSocket();
        this.responseSender = new ResponseSender();
        logger.debug("Открыт сокет");
        ThreadHelper.getPoolForReceiving().submit(new PacketReceiver(serverSocketChannel,responseManager));
        logger.debug("Ресивер пакетов запущен");
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        // Запускаем поток HashmapCleaner с интервалом в TIMEOUT миллисекунд
        scheduler.scheduleAtFixedRate(new HashmapCleaner(), 0, HashmapCleaner.getTIMEOUT(), TimeUnit.MILLISECONDS);
        logger.debug("Hashmap Cleaner запущен");


    }


    private void openNewSocket(){
        try {
            this.serverSocketChannel =   ServerSocketChannel.open();
            serverSocketChannel.bind(serverAddress);
            serverSocketChannel.configureBlocking(false); // Устанавливаем неблокирующий режим
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void run() throws ReceiveDataException {

//        while (running) {
//            poolForReceiving.submit(new ReceiveThread(this));
//
////                executor.executeCommand(command,address);
//        }
    }

//    public void connectToClient(SocketAddress addr) throws SocketException {
//        datagramChannel.setSoTimeout(10000);
//
//    }
//
//    public void disconnectFromClient() throws SocketException {
//        datagramChannel.setSoTimeout(0);
//
//    }







    @Override
    public void onResponse(Response response, SocketChannel channel) {
        try {
            responseSender.sendData(Serializer.serialize(response),channel);
        }catch (SerializeException e){
            logger.error("Не получилось сериализовать ответ клиенту: "+channel);
        }
    }
}

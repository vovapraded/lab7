package org.example.connection;

import lombok.Getter;
import org.common.network.RequestId;
import org.common.network.Response;
import org.common.serial.SerializeException;
import org.common.serial.Serializer;
import org.common.utility.PropertyUtil;
import org.example.managers.CurrentResponseManager;
import org.common.threads.ThreadHelper;
import org.example.utility.ReceiveDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.concurrent.*;

public class UdpServer implements ResponseListener {

    private final InetSocketAddress serverAddress;

    private final ByteBuffer buffer = ByteBuffer.allocate(1024);

    private final CurrentResponseManager responseManager;
    @Getter
    private static final int PACKET_SIZE = 1024;
    @Getter
    private static final int DATA_SIZE = PACKET_SIZE - 5;
    private static final int RECEIVE_BUFFER_SIZE = 2 * 1024 * 1024; // 2 MB

    private static final Logger logger = LoggerFactory.getLogger(UdpServer.class);
    private DatagramChannel datagramChannel;
    private final ResponseSender responseSender;
    private boolean running=true;

    public UdpServer(CurrentResponseManager responseManager) throws IOException {
        this.responseManager = responseManager;
        this.serverAddress = new InetSocketAddress(PropertyUtil.getAddress(),PropertyUtil.getPort());
        openNewSocket();
        this.responseSender = new ResponseSender(datagramChannel);
        logger.debug("Открыт сокет");
        ThreadHelper.getPoolForReceiving().submit(new PacketReceiver(datagramChannel,responseManager));
        logger.debug("Ресивер пакетов запущен");
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        // Запускаем поток HashmapCleaner с интервалом в TIMEOUT миллисекунд
        logger.debug("Hashmap Cleaner запущен");

    }


    private void openNewSocket(){
        try {
            this.datagramChannel = DatagramChannel.open();
            datagramChannel.configureBlocking(false); // Устанавливаем неблокирующий режим
            datagramChannel.bind(serverAddress);
            datagramChannel.socket().setReceiveBufferSize(RECEIVE_BUFFER_SIZE);
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
    public void onResponse(Response response, RequestId requestId) {
        try {
            responseSender.sendData(Serializer.serialize(response),requestId);
        } catch (IOException e) {
            logger.error("Не получилось отправить ответ на запрос: "+requestId);
        }catch (SerializeException e){
            logger.error("Не получилось сериализовать ответ на запрос: "+requestId);
        }
    }
}

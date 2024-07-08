package org.example.connection;

import com.google.common.primitives.Bytes;
import org.common.network.RequestId;
import org.common.utility.CodingUtil;
import org.common.threads.ThreadHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Arrays;

public class ResponseSender {
    private final int PACKET_SIZE = UdpServer.getPACKET_SIZE();
    private final int DATA_SIZE = UdpServer.getDATA_SIZE();
    private final DatagramChannel datagramChannel;
    private static final Logger logger = LoggerFactory.getLogger(ResponseSender.class);


    public ResponseSender(DatagramChannel datagramChannel) {
        this.datagramChannel = datagramChannel;
    }

    public void sendData(byte[] data, RequestId requestId) throws IOException {
        var size = (int) Math.ceil(data.length / (double) DATA_SIZE);
        for (int i = 0; i < size; i++) {
            int finalI = i;
            ThreadHelper.getPoolForSending().submit(() ->
            {
                byte [] packet = null;
                if (finalI == size-1){
                    packet = Bytes.concat(Arrays.copyOfRange(data, finalI * DATA_SIZE, (finalI + 1) * DATA_SIZE),new byte[]{requestId.getId()}, CodingUtil.encodingInt( -(finalI + 1)));
                }else {
                    packet = Bytes.concat(Arrays.copyOfRange(data, finalI * DATA_SIZE, (finalI + 1) * DATA_SIZE),new byte[]{requestId.getId()},CodingUtil.encodingInt((finalI + 1)));
                }
                sendPacket(packet, finalI, requestId, size);
            });
        }


    }


    private void sendPacket(byte[] packet, int i, RequestId requestId, int size) {
        ByteBuffer buffer = ByteBuffer.wrap(packet);
        var address = requestId.getAddress();
        try {

            datagramChannel.send(buffer, address);
            logger.debug("Пакет " + (i + 1) + " из " + size + "запроса "+requestId+" отправлен клиенту");
        } catch (IOException e) {
            logger.error("Не удалось на запрос "+requestId+ " отправить пакет " + (i + 1) + " из " + size);
        }


    }
}
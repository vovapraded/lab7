package org.example.connection;

import com.google.common.primitives.Bytes;
import lombok.SneakyThrows;
import org.example.threads.ThreadHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

public class ResponseSender {
    private final int PACKET_SIZE = 1024;
    private final int DATA_SIZE = PACKET_SIZE - 1;
    private static final Logger logger = LoggerFactory.getLogger(ResponseSender.class);


    public ResponseSender() {
    }

    public void sendData(byte[] data, SocketChannel channel)  {
        byte[][] packets=new byte[(int)Math.ceil(data.length / (double)DATA_SIZE)][PACKET_SIZE];
        for (int i = 0; i<packets.length;i++){
            if (i == packets.length - 1) {
                packets[i] = Bytes.concat(Arrays.copyOfRange(data,i*DATA_SIZE,(i+1)*DATA_SIZE), new byte[]{(byte) -(i+1)});
            } else {
                packets[i] = Bytes.concat(Arrays.copyOfRange(data,i*DATA_SIZE,(i+1)*DATA_SIZE), new byte[]{(byte) (i+1)});
            }
        }
        for (int i = 0; i < packets.length; i++) {
            byte[] packet = packets[i];            // Отправляем пакет на указанный адрес
            int finalI = i;
            ThreadHelper.getPoolForSending().submit(()->
            {
                // Создаем буфер для текущего пакета
                ByteBuffer buffer = ByteBuffer.wrap(packet);
                try {
                    channel.write(buffer);
                    logger.debug("Пакет "+ (finalI + 1) + " отправлен клиенту "+channel.getRemoteAddress());
                } catch (IOException e) {
                    try {
                        logger.error("Не удалось отправить пакет "+(finalI + 1)+" клиенту "+channel.getRemoteAddress());
                    } catch (IOException ex) {
                        logger.error("Не удалось отправить пакет "+(finalI + 1)+" клиенту с неизвестным адрессом");

                    }

                }
            });



        }
    }

}

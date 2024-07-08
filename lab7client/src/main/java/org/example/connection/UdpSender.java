package org.example.connection;

import com.google.common.primitives.Bytes;
import org.common.commands.Command;
import org.common.network.SendException;
import org.common.serial.SerializeException;
import org.common.serial.Serializer;
import org.common.utility.CodingUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Arrays;

public class UdpSender {
    private final int PACKET_SIZE = UdpClient.getPACKET_SIZE() ;
    private final int DATA_SIZE = UdpClient.getDATA_SIZE();
    private final DatagramChannel client;
    private final InetSocketAddress serverSocketAddress;
    private byte requestId=0;

    public UdpSender(DatagramChannel client, InetSocketAddress serverSocketAddress) {
        this.client = client;
        this.serverSocketAddress = serverSocketAddress;
    }

    public byte sendCommand(Command command) throws SerializeException {
        try {
            return sendData(Serializer.serialize(command));
        } catch (IOException e) {
            throw new SendException("Ошибка отправки данных, поробуйте ещё раз");
        }


    }
    public byte sendData(byte[] data) throws IOException {
        byte[][] packets=new byte[(int)Math.ceil(data.length / (double)DATA_SIZE)][PACKET_SIZE];
        for (int i = 0; i<packets.length;i++){
            if (i == packets.length - 1) {
                packets[i] = Bytes.concat(Arrays.copyOfRange(data, i * DATA_SIZE, (i + 1) * DATA_SIZE),new byte[]{requestId}, CodingUtil.encodingInt(-i - 1));
            }
            else {
                packets[i] = Bytes.concat(Arrays.copyOfRange(data, i * DATA_SIZE, (i + 1) * DATA_SIZE), CodingUtil.encodingInt(i + 1));
            }

        }
        requestId += 1;
        for (byte[] packet : packets) {
            ByteBuffer buffer = ByteBuffer.wrap(packet);
            client.send(buffer, serverSocketAddress);
        }
        return (byte) (requestId - 1);
    }

}

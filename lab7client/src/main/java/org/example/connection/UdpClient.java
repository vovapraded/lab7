package org.example.connection;
import lombok.Getter;
import lombok.SneakyThrows;

import org.common.commands.Command;
import org.common.network.Response;
import org.common.network.SendException;
import org.common.serial.DeserializeException;
import org.common.serial.Deserializer;
import org.common.serial.SerializeException;
import org.common.serial.Serializer;
import org.example.utility.CurrentConsole;
import org.example.utility.NoResponseException;
import org.common.utility.*;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.*;
import java.util.concurrent.*;


public class UdpClient  {
    private final int PACKET_SIZE = 1024;
    private final int DATA_SIZE = PACKET_SIZE - 4;
    private static final int RECEIVE_BUFFER_SIZE = 2 * 1024 * 1024; // 2 MB

    private final Console currentConsole = CurrentConsole.getInstance();


    private final DatagramChannel client;

    private UdpClient(){
    }
    @Getter
    private static UdpClient instance = new UdpClient();
    private final InetAddress serverAddress;


    private final InetSocketAddress serverSocketAddress;

    private final int serverPort = PropertyUtil.getPort();
    private  SocketAddress clientAddress;
    {
        try {
            serverAddress = InetAddress.getByName(PropertyUtil.getAddress());
            serverSocketAddress = new InetSocketAddress(serverAddress, PropertyUtil.getPort());
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        boolean channelIsOpen = false;
        try {
            this.client = DatagramChannel.open().bind(null).connect(serverSocketAddress);
            this.client.configureBlocking(false);
            this.client.socket().setReceiveBufferSize(RECEIVE_BUFFER_SIZE);
            channelIsOpen=true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
@Getter
    private final UdpSender udpSender = new UdpSender(PACKET_SIZE,client,serverSocketAddress);
    @Getter
    private final UdpReceiver udpReceiver = new UdpReceiver(PACKET_SIZE,client);








}

package org.example.connection;
import lombok.Getter;

import org.example.utility.CurrentConsole;
import org.common.utility.*;

import java.io.IOException;
import java.net.*;
import java.nio.channels.DatagramChannel;


public class UdpClient  {
    @Getter
    private static final int PACKET_SIZE = 1024;
    @Getter
    private static final int DATA_SIZE = PACKET_SIZE - 5;
    private static final int RECEIVE_BUFFER_SIZE = 2 * 1024 * 1024; // 2 MB

    private final Console currentConsole = CurrentConsole.getInstance();


    private final DatagramChannel client;

    private UdpClient(){
    }
    private final static UdpClient instance = new UdpClient();

    public static UdpClient getInstance() {
        return instance;
    }

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
    private final UdpSender udpSender = new UdpSender(client,serverSocketAddress);
    @Getter
    private final UdpReceiver udpReceiver = new UdpReceiver(client);








}

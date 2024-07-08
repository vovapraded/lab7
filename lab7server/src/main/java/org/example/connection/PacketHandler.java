package org.example.connection;

import com.google.common.primitives.Bytes;
import lombok.Getter;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.common.commands.Command;
import org.common.network.RequestId;
import org.common.serial.Deserializer;
import org.common.utility.CodingUtil;
import org.example.managers.CurrentResponseManager;
import org.example.managers.ExecutorOfCommands;
import org.common.threads.ThreadHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;

public class PacketHandler extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(PacketHandler.class);
    private final CurrentResponseManager responseManager;
    private final int PACKET_SIZE = UdpServer.getPACKET_SIZE();
    private final int DATA_SIZE = UdpServer.getDATA_SIZE();
    private ImmutablePair<RequestId,byte[]> requestIdAndPacket;
    private ExecutorService poolForProcessing;
    //храним арайлист пакетов, также храним пару <sizeOfRequest,timeReceivingLast>
//    private static ConcurrentHashMap<SocketAddress, ReceivedPacketsList> hashMap = new ConcurrentHashMap<>();
    @Getter

    private static final ConcurrentHashMap<RequestId,ReceivedPacketsList> hashMapForReceivedPackets = new ConcurrentHashMap<>();


    public PacketHandler(CurrentResponseManager responseManager, ImmutablePair<RequestId, byte[]> requestIdAndPacket) {
        this.responseManager = responseManager;
        this.requestIdAndPacket = requestIdAndPacket;
        this.poolForProcessing = ThreadHelper.getPoolForProcessing();

    }
    public void run() {
                logger.debug("Обработчик пакетов запущен");
                var requestId = requestIdAndPacket.getLeft();
                var packet = requestIdAndPacket.getRight();
                handlePacket(requestId, packet);
    }


    private void handlePacket(RequestId requestId, byte[] bytes) {
        var address = requestId.getAddress();



        var lastChunks = Arrays.copyOfRange( bytes, DATA_SIZE+1, PACKET_SIZE);
        var packetNumber = CodingUtil.decodingInt(lastChunks);
        var isLastPacket = packetNumber < 0;
        packetNumber = Math.abs(packetNumber);

        hashMapForReceivedPackets.putIfAbsent (requestId, new ReceivedPacketsList());
        var receivedPacketsList = hashMapForReceivedPackets.get(requestId);
        if (receivedPacketsList.getTimeInit()+5000<System.currentTimeMillis()){
            receivedPacketsList = new ReceivedPacketsList();
            hashMapForReceivedPackets.put(requestId, receivedPacketsList);
        }
        var resultArray = receivedPacketsList.getPackets();

        var data = Arrays.copyOf(bytes, DATA_SIZE);
        ReceivedPacket packet = ReceivedPacket.builder()
                .data(data)
                .number(packetNumber)
                .build();
        logger.debug("Пакет "+packetNumber+" получен от клиента "+address);

        synchronized (resultArray) {
            resultArray.add(packet);
        }
        if (isLastPacket ) {
                receivedPacketsList.setSizeOfRequest(packetNumber);
        }
        int sizeOfRequest = receivedPacketsList.getSizeOfRequest();
        if (sizeOfRequest == resultArray.size()){
            var result = sortPackets(resultArray);
            handleRequest(requestId,result);
            hashMapForReceivedPackets.remove(requestId);

        }
            receivedPacketsList.setTimeOfLastPacket(System.currentTimeMillis());


    }
    private byte[] sortPackets(ArrayList<ReceivedPacket> resultArray)  {
        resultArray.sort(Comparator.comparing(packet -> packet.getNumber()));
        return resultArray.stream().parallel()
                .map((packet) -> packet.getData()).reduce(new byte[0], (arr1, arr2) ->
                Bytes.concat(arr1, arr2));
    }
    private void handleRequest(RequestId requestId, byte[] result) {
        var command= Deserializer.deserialize(result, Command.class);
        var address = requestId.getAddress();
        logger.debug("Команда "+ command.getClass().getName()+" запроса: "+requestId+" получена");
        poolForProcessing.submit(new ExecutorOfCommands(command,responseManager,requestId));
    }
}


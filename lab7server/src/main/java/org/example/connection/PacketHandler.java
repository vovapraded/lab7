package org.example.connection;

import com.google.common.primitives.Bytes;
import lombok.Getter;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.MutablePair;
import org.common.commands.Command;
import org.common.serial.Deserializer;
import org.common.utility.CodingUtil;
import org.example.managers.CurrentResponseManager;
import org.example.managers.ExecutorOfCommands;
import org.example.threads.ThreadHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

public class PacketHandler extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(PacketHandler.class);
    private final CurrentResponseManager responseManager;
    private final int PACKET_SIZE = 1024;
    private final int DATA_SIZE = PACKET_SIZE - 4;
    private ImmutablePair<SocketAddress,byte[]> addrAndPacket ;
    private ExecutorService poolForProcessing;
    //храним арайлист пакетов, также храним пару <sizeOfRequest,timeReceivingLast>
    @Getter
    private static ConcurrentHashMap<SocketAddress, ReceivedPacketsList> hashMap = new ConcurrentHashMap<>();

    public PacketHandler(CurrentResponseManager responseManager, ImmutablePair<SocketAddress, byte[]> addrAndPacket) {
        this.responseManager = responseManager;
        this.addrAndPacket = addrAndPacket;
        this.poolForProcessing = ThreadHelper.getPoolForProcessing();

    }
    public void run() {
                logger.debug("Обработчик пакетов запущен");
                var addr = addrAndPacket.getLeft();
                var packet = addrAndPacket.getRight();
                handlePacket(addr, packet);
    }

    private void handlePacket(SocketAddress address, byte[] bytes) {
        hashMap.putIfAbsent (address, new ReceivedPacketsList());
        var receivedPacketsList = hashMap.get(address);
        var resultArray = receivedPacketsList.getPackets();
        var lastChunks = Arrays.copyOfRange( bytes, DATA_SIZE, PACKET_SIZE);
        var packetNumber = CodingUtil.decodingInt(lastChunks);
        var isLastPacket = packetNumber < 0;
        packetNumber = Math.abs(packetNumber);
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
        int sizeOfRequest;
        sizeOfRequest = receivedPacketsList.getSizeOfRequest();
        if (sizeOfRequest == resultArray.size()){
            var result = sortPackets(resultArray);
            handleRequest(address,result);
            hashMap.remove(address);
        }
            receivedPacketsList.setTimeOfLastPacket(System.currentTimeMillis());


    }
    private byte[] sortPackets(ArrayList<ReceivedPacket> resultArray)  {
        resultArray.sort(Comparator.comparing(packet -> packet.getNumber()));
        return resultArray.stream().parallel()
                .map((packet) -> packet.getData()).reduce(new byte[0], (arr1, arr2) ->
                Bytes.concat(arr1, arr2));
    }
    private void handleRequest(SocketAddress address, byte[] result) {
        var command= Deserializer.deserialize(result, Command.class);
        logger.debug("Команда "+ command.getClass().getName()+" получена с адресса "+address);
        poolForProcessing.submit(new ExecutorOfCommands(command,address,responseManager));
    }
}


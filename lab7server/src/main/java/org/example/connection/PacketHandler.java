package org.example.connection;

import com.google.common.primitives.Bytes;
import lombok.Getter;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.MutablePair;
import org.common.commands.Command;
import org.common.serial.Deserializer;
import org.example.managers.CurrentResponseManager;
import org.example.managers.ExecutorOfCommands;
import org.example.threads.ThreadHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

public class PacketHandler extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(PacketHandler.class);
    private final CurrentResponseManager responseManager;
    private final int PACKET_SIZE = 1024;
    private final int DATA_SIZE = PACKET_SIZE - 1;
    private ImmutablePair<SocketChannel,byte[]> channelAndPacket;
    private ExecutorService poolForProcessing;
    @Getter
    private static ConcurrentHashMap<SocketChannel, MutablePair<ArrayList<ImmutablePair<byte[],Byte>>,MutablePair<Byte,Long>>> hashMap = new ConcurrentHashMap<>();

    public PacketHandler(CurrentResponseManager responseManager, ImmutablePair<SocketChannel, byte[]> channelAndPacket) {
        this.responseManager = responseManager;
        this.channelAndPacket = channelAndPacket;
        this.poolForProcessing = ThreadHelper.getPoolForProcessing();

    }
    public void run() {
                logger.debug("Обработчик пакетов запущен");
                var channel = channelAndPacket.getLeft();
                var packet = channelAndPacket.getRight();
                handlePacket(channel, packet);
    }

    private void handlePacket(SocketChannel channel, byte[] data) {
        hashMap.putIfAbsent (channel,new MutablePair<>(new ArrayList(),new MutablePair<>(Byte.MAX_VALUE,System.currentTimeMillis())));
        var pair = hashMap.get(channel);
        var resultArray = pair.getLeft();
        var lastChunk = data[data.length - 1];
        logger.debug("Пакет "+Math.abs(lastChunk)+" получен от клиента "+channel);
        data = Arrays.copyOf(data, data.length - 1);
        synchronized (resultArray) {
            resultArray.add(new ImmutablePair<byte[], Byte>(data, (byte) Math.abs(lastChunk)));
        }
        if (lastChunk < 0 ) {
                pair.getRight().setLeft((byte) Math.abs(lastChunk));
        }
        var sizeOfRequest = pair.getRight().getLeft();
        if (sizeOfRequest == resultArray.size()){
            var result = sortPackets(resultArray);
            handleRequest(channel,result);
            hashMap.remove(channel);
        }
        pair.getRight().getLeft();
        pair.getRight().setRight(System.currentTimeMillis());
        System.out.println(hashMap);
    }
    private byte[] sortPackets(ArrayList<ImmutablePair<byte[],Byte>> result)  {
        result.sort(Comparator.comparing(pair -> pair.getRight()));
        var request = result.stream().parallel()
                .map((pair) -> pair.getLeft()).reduce(new byte[0], (arr1, arr2) ->
                Bytes.concat(arr1, arr2));
        return request;
    }
    private void handleRequest(SocketChannel channel, byte[] result) {
        var command= Deserializer.deserialize(result, Command.class);
        logger.debug("Команда "+ command.getClass().getName()+" получена с адресса "+channel);
        poolForProcessing.submit(new ExecutorOfCommands(command,channel,responseManager));
    }
}


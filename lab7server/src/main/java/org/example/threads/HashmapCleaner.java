package org.example.threads;

import lombok.Getter;
import org.example.connection.PacketHandler;
import org.example.utility.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

public class HashmapCleaner extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(HashmapCleaner.class);
    @Getter
    private static final int TIMEOUT = 5 * 1000;
    public void run(){
        checkAndClean();
    }
    private static void checkAndClean(){
        var hashMap = PacketHandler.getHashMap();
        long currentTime = System.currentTimeMillis();
        AtomicInteger removedCount = new AtomicInteger(0); // Используем AtomicInteger для атомарного инкремента
        hashMap.values().removeIf(receivedPacketsList -> {
            if ( receivedPacketsList.getTimeOfLastPacket() + TIMEOUT < currentTime) {
                removedCount.incrementAndGet(); // Увеличиваем счетчик при удалении
                return true; // Удаляем запись
            }
            return false; // Не удаляем
        });
       logger.debug("Произошла проверка старых записей hashMap, удалено {} старых записей", removedCount.get());

    }
}

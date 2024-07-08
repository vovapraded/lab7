package org.example.connection;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class TimeoutChecker {
    private final ScheduledExecutorService pool =  Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> scheduledFuture;
    private final int TIMEOUT = 5000;


    public void run(){
//        System.out.println("Таймаут начался "+ LocalDateTime.now());
            if (scheduledFuture!=null) scheduledFuture.cancel(true);
            scheduledFuture = pool.schedule(() -> {
                    PacketReceiver.setWork(false);
                    PacketHandler.setInterrupt(true);
                        System.out.println("Прошел таймаут ");
                    }
                    , TIMEOUT, TimeUnit.MILLISECONDS);
    }
    public void shutdown(){
        if (scheduledFuture!=null) scheduledFuture.cancel(true);
        PacketReceiver.setWork(false);
        PacketHandler.setInterrupt(true);
    }
}

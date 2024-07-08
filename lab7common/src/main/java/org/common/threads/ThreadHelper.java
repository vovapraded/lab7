package org.common.threads;

import lombok.Getter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadHelper {
    @Getter
    private static final ExecutorService poolForReceiving = Executors.newCachedThreadPool();
    @Getter
    private static final ExecutorService poolForProcessing = Executors.newCachedThreadPool();
    @Getter
    private static final ExecutorService poolForSending = Executors.newFixedThreadPool(10);
}

package org.controller.connector.to.client;

import org.common.commands.Command;
import org.example.connector.to.controller.ConsoleEventListener;

import java.util.ArrayDeque;
import java.util.Queue;

public class ConsoleEventListenerImpl implements ConsoleEventListener {

    private final Queue<String> queue = new ArrayDeque<String>() ;
    @Override
    public void onEvent(String message) {
        queue.add(message);
    }
    public String getMessage(){
       String message = null;
        while (message==null){
            message=queue.poll();
        }
        return message;
    }
}

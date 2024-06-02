package org.controller;

import lombok.Getter;
import org.controller.connector.to.client.ConsoleEventListenerImpl;
import org.example.Main;

public class Initializer {

    @Getter
    private static final ConsoleEventListenerImpl listener = new ConsoleEventListenerImpl();
    public static void init(){
        Main.getConsoleEventPublisher().addListener(listener);
    }

//    @SneakyThrows
//    public static void initClient(){
//        Main.main(new String[]{});
//    }
}

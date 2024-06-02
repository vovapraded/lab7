package org.example;

import lombok.SneakyThrows;
import org.example.utility.Main;

public class InitializerClient {
    @SneakyThrows
    public static void init(){
        Main.main(new String[]{});
    }
}

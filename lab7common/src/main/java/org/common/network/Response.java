package org.common.network;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

@Getter @Setter @Builder @AllArgsConstructor @NoArgsConstructor
public class Response implements Serializable {
    @Serial
    private static final long serialVersionUID = "Response".hashCode();
    @Builder.Default
    private ArrayList<String> message = new ArrayList<>();
    private transient SocketChannel channel;
    private boolean loginCorrect;
    private boolean passwordCorrect;
    public String getMessageBySingleString(){
       return  String.join("\n", message);
    }

}

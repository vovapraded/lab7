package org.common.network;

import lombok.*;
import org.common.dto.Ticket;

import java.io.Serial;
import java.io.Serializable;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter  @AllArgsConstructor @Builder @NoArgsConstructor
public class Response implements Serializable {
    @Serial
    private static final long serialVersionUID = "Response".hashCode();
    @Builder.Default
    private ArrayList<String> message = new ArrayList<>();
    @Setter @Builder.Default
    private boolean isThereEx = false;
    private List<Ticket> tickets;


    private transient SocketAddress address;
    private boolean loginCorrect;
    private boolean passwordCorrect;
    public String getMessageBySingleString(){
        var messageBySingleString = String.join("\n", message);
        if (isThereEx ){
            throw new RuntimeException(messageBySingleString);
        }
       return messageBySingleString  ;
    }

}

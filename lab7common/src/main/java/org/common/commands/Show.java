package org.common.commands;

import lombok.SneakyThrows;

import java.io.Serial;
import java.io.Serializable;

/**
 * The command outputs a collection
 */
public class Show extends Command implements Serializable {
    @Serial
    private static final long serialVersionUID = "Show".hashCode();




    @SneakyThrows
    @Override
    public void execute() {
        if (collection.getHashMap().isEmpty()) {
            responseManager.addToSend("Коллекция пуста",this);
        } else {
            collection.getHashMap().values().stream()
                    .sorted()
                    .forEach(ticket -> responseManager.addToSend(ticket.toString(),this) );
        }
        loggerHelper.debug("Команда "+this.getClass().getName()+"от адресса "+responseManager.getResponse(this).getChannel().getRemoteAddress() +" выполнена");
        responseManager.send(this);
    }

    @Override
    public void validate(String arg1) {

    }
}

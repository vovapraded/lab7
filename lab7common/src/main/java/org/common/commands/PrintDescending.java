package org.common.commands;

import lombok.SneakyThrows;

import java.io.Serial;
import java.io.Serializable;
import java.util.Comparator;

/**
 * The command to display the collection in reverse order
 */
public class PrintDescending extends Command implements Serializable {
    @Serial
    private static final long serialVersionUID = "PrintDescending".hashCode();


    @SneakyThrows
    @Override
    public void execute() {
        if (collection.getHashMap().isEmpty()){
            responseManager.addToSend("Коллекция пуста",this);
        }
        else{
            collection.getHashMap().values().stream()
                    .sorted(Comparator.reverseOrder())
                    .forEach(ticket -> responseManager.addToSend(ticket.toString() ,this));
        }
        loggerHelper.debug("Команда "+this.getClass().getName()+"от адресса "+responseManager.getResponse(this).getChannel().getRemoteAddress() +" выполнена");
        responseManager.send(this);

    }

    @Override
    public void validate(String arg1) {

    }
}
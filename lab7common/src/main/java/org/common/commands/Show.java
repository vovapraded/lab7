package org.common.commands;

import java.io.Serial;
import java.io.Serializable;

/**
 * The command outputs a collection
 */
public class Show extends Command implements Serializable {
    @Serial
    private static final long serialVersionUID = "Show".hashCode();




    @Override
    public void execute() {

            var tickets  = collection.getHashMap().values().stream()
                    .sorted()
                    .toList();
            responseManager.addToSend(tickets,this);
        System.out.println("ОТПРАВИЛИ");


        loggerHelper.debug("Команда "+this.getClass().getName()+"от адресса "+responseManager.getResponse(this).getAddress() +" выполнена");
        responseManager.send(this);
    }

    @Override
    public void validate(String arg1) {

    }
}

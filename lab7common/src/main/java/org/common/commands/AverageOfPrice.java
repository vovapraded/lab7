package org.common.commands;

import lombok.SneakyThrows;

import java.io.Serial;
import java.io.Serializable;


/**
 * The command to display the average price
 */
//sa
public class AverageOfPrice extends Command implements Serializable {
    @Serial
    private static final long serialVersionUID = "AverageOfPrice".hashCode();


    @SneakyThrows
    @Override
    public void execute() {
        var average = collection.getAveragePrice();
        if (average.isEmpty()) {
            responseManager.addToSend("Коллекция пуста", this);
        } else {
            responseManager.addToSend("Средняя цена " + average.getAsDouble(),this);
        }
        loggerHelper.debug("Команда "+this.getClass().getName()+"от адресса "+responseManager.getResponse(this).getChannel().getRemoteAddress() +" выполнена");
        responseManager.send(this);
    }

    @Override
    public void validate(String arg1) {
    }
}
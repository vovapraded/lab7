package org.common.commands;

import java.io.Serial;
import java.io.Serializable;


/**
 * The command to display the average price
 */
//sa
public class AverageOfPrice extends Command implements Serializable {
    @Serial
    private static final long serialVersionUID = "AverageOfPrice".hashCode();


    @Override
    public void execute() {
        var average = collection.getAveragePrice();
        if (average.isEmpty()) {
            responseManager.addToSend("Коллекция пуста", this);
        } else {
            responseManager.addToSend("Средняя цена " + average.getAsDouble(),this);
        }
        loggingSuccess();
        responseManager.send(this);
    }

    @Override
    public void validate(String arg1) {
    }
}
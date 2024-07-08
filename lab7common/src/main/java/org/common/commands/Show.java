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

        addTicketsAndSendAndLogging();
    }

    @Override
    public void validate(String arg1) {

    }
}

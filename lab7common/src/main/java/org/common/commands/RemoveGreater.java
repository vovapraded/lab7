package org.common.commands;


import org.common.dto.Ticket;
import org.common.utility.*;


import java.io.Serial;
import java.io.Serializable;

/**
 * The remove items with a price higher than the specified one command
 */
public class RemoveGreater extends Command implements Serializable {
    @Serial
    private static final long serialVersionUID = "RemoveGreater".hashCode();



    @Override
    public void execute() {
            Ticket ticket = ticketArg;
            collection.removeGreater(ticket);
            console.addToSend("Операция прошла успешно",getAddress());
        console.send(getAddress());

    }

    @Override
    public void validate(String arg1) {
        this.stringArg = arg1;
        if (!Validator.validate(stringArg, TypesOfArgs.Long,false) || Long.parseLong(stringArg)<=0){
            throw new InvalidFormatException("Id должен быть числом > 0",getAddress());
        }
    }
}
package org.common.commands;
import lombok.Getter;
import lombok.Setter;
import org.common.dto.Ticket;
import org.common.managers.Collection;
import org.common.managers.*;
import org.common.utility.*;

import java.io.Serial;
import java.io.Serializable;
import java.net.SocketAddress;

/**
 * Interface for the command
 */
@Setter
@Getter
public abstract class Command implements Serializable {
    @Serial
    private static final long serialVersionUID = "Command".hashCode();

    protected Command() {

    }

    public abstract void execute();
    public abstract void validate(String arg1);
    public void prepareToSend(boolean ticketArgIsNeeded){
        if (ticketArgIsNeeded ) {
            Validator.validate(stringArg,TypesOfArgs.Long,false);
            CreateTicket creator = new CreateTicket(console);
            var ticket= creator.createTicket(null);
            this.setTicketArg(ticket);
        }
    }
    protected String stringArg=null;
    protected Ticket ticketArg=null;
    protected static final Collection collection = Collection.getInstance();
    protected  transient    Console console ;
    private transient SocketAddress address;

}

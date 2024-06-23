package org.common.commands;
import lombok.Getter;
import lombok.Setter;
import org.common.commands.authorization.AuthorizationCommand;
import org.common.commands.inner.objects.Authorization;
import org.common.commands.inner.objects.LoggerHelper;
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
    protected LoggerHelper loggerHelper;
    protected Command() {

    }
//   public void   send()  {
//       responseManager.send(this);
//   }

    public abstract void execute();
    public abstract void validate(String arg1);

    protected String stringArg=null;
    protected Ticket ticketArg=null;
    private Authorization authorization = null;
    protected static final Collection collection = Collection.getInstance();
    protected  transient    Console console ;
    protected  transient    ResponseManager responseManager ;
    public void setConsole (Console console){
        this.console = console;
    }


}

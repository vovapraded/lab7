package org.common.commands;


import lombok.SneakyThrows;
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



    @SneakyThrows
    @Override
    public void execute() {
            Ticket ticket = ticketArg;
            ticketArg.setCreatedBy(getAuthorization().getLogin());
            collection.removeGreater(ticket,getAuthorization().getLogin());
            responseManager.addToSend("Операция прошла успешно",this);
        loggerHelper.debug("Команда "+this.getClass().getName()+"от адресса "+responseManager.getResponse(this).getChannel().getRemoteAddress() +" выполнена");
        responseManager.send(this);

    }

    @Override
    public void validate(String arg1) {
        this.stringArg = arg1;
        if (!Validator.validate(stringArg, TypesOfArgs.Long,false) || Long.parseLong(stringArg)<=0){
            throw new InvalidFormatException("Id должен быть числом > 0",this);
        }
    }
}
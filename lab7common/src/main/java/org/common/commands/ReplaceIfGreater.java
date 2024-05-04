package org.common.commands;

import lombok.SneakyThrows;
import org.common.dto.Ticket;
import org.common.utility.*;

import java.io.Serial;
import java.io.Serializable;


/**
 * The command to replace the item price if it is higher
 */
public class ReplaceIfGreater extends Command implements Serializable {
    @Serial
    private static final long serialVersionUID = "ReplaceIfGreater".hashCode();


    @SneakyThrows
    public void execute(){
        var idStr = stringArg;
        try {
            ValidateId.validateId(idStr, false, collection);
        }catch (InvalidFormatException e){
            e.setCommand(this);
            throw e;
        }
        Long id = Long.parseLong(idStr);
        Ticket oldTicket = collection.getElement(id);
        Ticket newTicket = ticketArg ;
        ticketArg.setCreatedBy(getAuthorization().getLogin());
        if (newTicket.compareTo(oldTicket)>0){
            collection.removeElement(id,getAuthorization().getLogin());
            newTicket.setId(id);
            collection.insertElement(newTicket);
            responseManager.addToSend("Операция прошла успешно. Замена произошла",this);
        }
        else {
            responseManager.addToSend("Операция прошла успешно. Замена не произошла",this);
        }
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

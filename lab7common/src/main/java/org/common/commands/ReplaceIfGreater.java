package org.common.commands;

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


    public void execute(){
        var idStr = stringArg;
        try {
            ValidateId.validateId(idStr, false, collection);
        }catch (InvalidFormatException e){
            e.setCommand(this);
            throw e;
        }
        Long id = Long.parseLong(idStr);
        ticketArg.setCreatedBy(getAuthorization().getLogin());
        ticketArg.setId(id);
        var result=collection.replaceIfGreater(ticketArg);
        var ne = result ? "" : "не ";
        responseManager.addToSend("Замена "+ne+"произошла",this);
        loggerHelper.debug("Команда "+this.getClass().getName()+"от адресса "+responseManager.getResponse(this).getAddress() +" выполнена");
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

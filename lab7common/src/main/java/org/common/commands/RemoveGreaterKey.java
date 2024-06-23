package org.common.commands;


import org.common.utility.InvalidFormatException;
import org.common.utility.TypesOfArgs;
import org.common.utility.Validator;

import java.io.Serial;
import java.io.Serializable;

/**
 * The command to delete items with an id greater than the specified number
 */
public class RemoveGreaterKey extends Command implements  Serializable {
    @Serial
    private static final long serialVersionUID = "RemoveGreaterKey".hashCode();



    @Override
    public void execute() {
        Long id = null;
        try {
            id = ValidateId.validateId(stringArg, false, collection);
        }catch (InvalidFormatException e){
            e.setCommand(this);
            throw e;
        }

            collection.removeGreaterKey(id,getAuthorization().getLogin());
            responseManager.addToSend("Операция успешно выполнена",this);
        loggerHelper.debug("Команда "+this.getClass().getName()+"от адресса "+responseManager.getResponse(this).getAddress() +" выполнена");
        responseManager.send(this);



    }

    @Override
    public void validate(String arg1) {
        this.stringArg = arg1;
        if (!Validator.validate(stringArg, TypesOfArgs.Long,false) || Long.parseLong(stringArg)<=0){
            throw new InvalidFormatException("ErrorIdMustBePositive",this);
        }

    }
}
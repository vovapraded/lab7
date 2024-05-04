package org.common.commands;

import lombok.SneakyThrows;
import org.common.utility.InvalidFormatException;
import org.common.utility.TypesOfArgs;
import org.common.utility.Validator;

import java.io.Serial;
import java.io.Serializable;

/**
 * The delete item command
 */
public class RemoveKey extends Command implements Serializable {
    @Serial
    private static final long serialVersionUID = "RemoveKey".hashCode();


    @SneakyThrows
    public void execute(){
        var idStr = stringArg;
        Long id = null;
        try {
            id = ValidateId.validateId(idStr, false, collection);
        }catch (InvalidFormatException e){
            e.setCommand(this);
            throw e;
        }
        collection.removeElement(id,getAuthorization().getLogin());
        responseManager.addToSend("Элемент удалён",this);
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

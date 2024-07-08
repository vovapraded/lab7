package org.common.commands;

import java.io.Serial;
import java.io.Serializable;

/**
 * The clear collection command
 */
public class Clear extends Command implements Serializable {
    @Serial
    private static final long serialVersionUID = "Clear".hashCode();


    @Override
    public void execute() {
        collection.clearCollection(getAuthorization().getLogin());
        responseManager.addToSend("Коллекция пользователя "+getAuthorization().getLogin()+" очищена",this);
        loggingSuccess();
        responseManager.send(this);
    }

    @Override
    public void validate(String arg1) {
    }
}

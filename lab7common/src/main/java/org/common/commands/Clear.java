package org.common.commands;

import lombok.SneakyThrows;

import java.io.Serial;
import java.io.Serializable;

/**
 * The clear collection command
 */
public class Clear extends Command implements Serializable {
    @Serial
    private static final long serialVersionUID = "Clear".hashCode();


    @SneakyThrows
    @Override
    public void execute() {
        collection.clearCollection(getAuthorization().getLogin());
        responseManager.addToSend("Коллекция пользователя "+getAuthorization().getLogin()+" очищена",this);
        loggerHelper.debug("Команда "+this.getClass().getName()+"от адресса "+responseManager.getResponse(this).getChannel().getRemoteAddress() +" выполнена");
        responseManager.send(this);
    }

    @Override
    public void validate(String arg1) {
    }
}

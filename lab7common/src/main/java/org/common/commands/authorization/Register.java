package org.common.commands.authorization;

import lombok.SneakyThrows;
import org.common.commands.Command;

public class Register extends Command implements AuthorizationCommand  {
    @SneakyThrows
    @Override
    public void execute() {
        responseManager.addToSend("Вы успешно зарегестрированны и авторизованны", this);
        loggerHelper.debug("Команда "+this.getClass().getName()+"от адресса "+responseManager.getResponse(this).getChannel().getRemoteAddress() +" выполнена");
        responseManager.send(this);
    }


    @Override
    public void validate(String arg1) {
    }
}

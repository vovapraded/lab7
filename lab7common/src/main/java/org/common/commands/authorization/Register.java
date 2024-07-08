package org.common.commands.authorization;

import org.common.commands.Command;

public class Register extends Command implements AuthorizationCommand  {
    @Override
    public void execute() {
        responseManager.addToSend("SuccessRegisterAndLoggedIn", this);
        loggingSuccess();
        responseManager.send(this);
    }


    @Override
    public void validate(String arg1) {
    }
}

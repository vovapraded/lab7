package org.common.commands.authorization;

import org.common.commands.Command;

public class Login extends Command implements AuthorizationCommand {
    @Override
    public void execute() {
        responseManager.addToSend("SuccessLoggedIn", this);
        loggingSuccess();
        responseManager.send(this);
    }

    @Override
    public void validate(String arg1) {
    }
}

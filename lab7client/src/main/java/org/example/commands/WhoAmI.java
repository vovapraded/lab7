package org.example.commands;

import org.common.commands.Command;
import org.common.utility.PropertyUtil;

public class WhoAmI extends Command implements ClientCommand{
    @Override
    public void execute() {
        var login = PropertyUtil.getLogin();
        if (login == null)
            console.sendToController("NotLoginIn");
        else
            console.sendToController(login);
    }

    @Override
    public void validate(String arg1) {

    }
}

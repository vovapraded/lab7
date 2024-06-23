package org.example.commands;

import org.common.commands.Command;
import org.common.utility.PropertyUtil;

public class WhoAmI extends Command implements ClientCommand{
    @Override
    public void execute() {
        var login = PropertyUtil.getLogin();
        if (login == null)
            console.sendToController("NotLoginIn",true);
        else
            console.sendToController(login,false);
    }

    @Override
    public void validate(String arg1) {

    }
}

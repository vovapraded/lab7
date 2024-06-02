package org.example.commands;

import org.common.commands.Command;
import org.common.utility.PropertyUtil;
import org.example.utility.NoResponseException;

public class WhoAmI extends Command implements ClientCommand{
    @Override
    public void execute() {
        var login = PropertyUtil.getLogin();
        if (login == null)
            console.print("NotLoginIn");
        else
            console.print(login);
    }

    @Override
    public void validate(String arg1) {

    }
}

package org.example.authorization;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.common.commands.Command;
import org.common.commands.authorization.AuthorizationCommand;
import org.common.commands.inner.objects.Authorization;
import org.common.utility.PropertyUtil;
import org.example.utility.CurrentConsole;

import static org.common.utility.PropertyUtil.*;

public class AuthorizationManager {
    private static  CurrentConsole currentConsole;
    @Getter @Setter
    private static boolean isLogined = false;

    public static <T extends Command> void prepareCommand(T command){
        if (command instanceof AuthorizationCommand)
            prepareAuthorizationCommand(command);
        else prepareCommonCommand(command);
    }
    private static void prepareAuthorizationCommand(Command authorizationCommand) {
        currentConsole = (CurrentConsole) authorizationCommand.getConsole();
        var loginAndPassword=inputLoginAndPassword();
        var login = loginAndPassword.getLeft();
        var password = loginAndPassword.getRight();
        Authorization authorization = new Authorization(login,password);
        authorizationCommand.setAuthorization(authorization);
        setLogin(login);
        setPassword(password);
    }
    private static void prepareCommonCommand(Command command)  throws NoAuthorizationException{
//        if (!isLogined) throw new NoAuthorizationException();

        Authorization authorization = new Authorization(getLogin(), getPassword());
        command.setAuthorization(authorization);
    }

    private static ImmutablePair<String,String> inputLoginAndPassword(){
        currentConsole.sendToController("Введите логин:");
        var login = currentConsole.getInputFromCommand(1,1);
        currentConsole.sendToController("Введите пароль:");
        var password = currentConsole.getInputFromCommand(1,1);
        return new ImmutablePair<>(login,password);
    }
    public static void resetAuth(){
       PropertyUtil.setLogin(null);
       PropertyUtil.setPassword(null);
    }
    public static boolean checkAuth(){
        return  PropertyUtil.getPassword() != null &&  PropertyUtil.getLogin() != null;
    }

}

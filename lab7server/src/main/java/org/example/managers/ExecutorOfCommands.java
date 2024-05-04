package org.example.managers;

import lombok.SneakyThrows;
import org.common.commands.Command;
import org.common.commands.authorization.Register;
import org.common.managers.Collection;
import org.common.network.Response;
import org.common.utility.InvalidFormatException;
import org.example.authorization.AuthorizationException;
import org.example.authorization.AuthorizationManager;
import org.common.commands.authorization.NoAccessException;
import org.example.dao.FailedTransactionException;
import org.example.utility.CurrentLoggerHelper;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;

/**
 * A class for executing commands
 */
public class ExecutorOfCommands extends Thread{


    private final CurrentLoggerHelper loggerHelper = new CurrentLoggerHelper();
    private final Collection collection = Collection.getInstance();
    private  final CurrentResponseManager responseManager;
    private static final Logger logger = LoggerFactory.getLogger(ExecutorOfCommands.class);
    private final Command command;
    private final SocketChannel channel;

    public ExecutorOfCommands(Command command, SocketChannel channel, CurrentResponseManager responseManager){
        this.responseManager = responseManager;
        this.command = command;
        this.channel = channel;
    }
    @SneakyThrows
public void run(){
     try {
         executeCommand(command,channel);
     } catch (InvalidFormatException e) {
         responseManager.addToSend(e.getMessage(), e.getCommand());
         responseManager.send(e.getCommand());
     }finally {

             logger.debug("Команда "+ command.getClass().getName()+" с адресса "+channel.getRemoteAddress()+" выполнена");

     }

}
    public boolean isRegisterCommand(Command command){
        if (command instanceof Register) return true;
        else return false;
    }
    public boolean checkAuthorizationAndGenerateResponse(@NotNull Command command,SocketChannel channel) throws AuthorizationException{
        var login=command.getAuthorization().getLogin();
        var password=command.getAuthorization().getPassword();
        try {
            var result  = AuthorizationManager.checkLoginAndPassword(login,password);
            var loginCorrect = result.getLeft();
            var passwordCorrect = result.getRight();
            responseManager.initResponse(command,Response.builder().loginCorrect(loginCorrect).passwordCorrect(passwordCorrect).channel(channel).build());
            return loginCorrect & passwordCorrect;
        }catch (FailedTransactionException e){
            throw new AuthorizationException("Произошла ошибка авторизации, попробуйте ещё");
        }


    }
//    public boolean checkAuthorization(Command command)  {
//
//    }
    public void executeCommand(Command command,SocketChannel channel) throws InvalidFormatException {
        command.setResponseManager(responseManager);
        command.setLoggerHelper(loggerHelper);
        try {
            if (isRegisterCommand(command)) {
                checkAuthRegisterCommand(command, channel);
            } else {
                checkAuthCommonCommand(command, channel);
            }
            logger.debug("Пользователь "+command.getAuthorization().getLogin()+ " авторизован успешно");
            command.execute();
        }catch (AuthorizationException e){
            logger.debug("Пользователь "+command.getAuthorization().getLogin()+ " не авторизован");
            responseManager.addToSend(e.getMessage(),command);
            responseManager.send(command);
        }catch (NoAccessException e){
            logger.debug("Нет доступа до удаления "+command.getAuthorization().getLogin()+ " не авторизован");
            responseManager.addToSend(e.getMessage(),command);
            responseManager.send(command);
        }
        catch (FailedTransactionException e){
            logger.error("Транзакция команды "+ command.getClass().getName() +" от пользователя "+command.getAuthorization().getLogin()+" завершилась с ошибкой");
            responseManager.addToSend(e.getMessage(),command);
            responseManager.send(command);
        }


    }
    public void checkAuthRegisterCommand(Command command, SocketChannel channel) throws FailedTransactionException {
        var response=Response.builder()
                .channel(channel)
                .loginCorrect(false).passwordCorrect(true)
                .build();
        responseManager.initResponse(command,response);
                var login=command.getAuthorization().getLogin();
                var password=command.getAuthorization().getPassword();
                AuthorizationManager.register(login,password);
                response.setLoginCorrect(true);


    }
@SneakyThrows
        public void checkAuthCommonCommand(Command command,SocketChannel channel) throws AuthorizationException, FailedTransactionException {
            var isAuthorized=checkAuthorizationAndGenerateResponse(command,channel);
            if ( !isAuthorized ){
                logger.debug("Команда "+command.getClass().getName()+" не выполнена, клиент "+responseManager.getResponse(command).getChannel().getRemoteAddress()+" не авторизован");
                if (!responseManager.getResponse(command).isLoginCorrect())
                    throw  new AuthorizationException( "Вы не авторизованы, неверный логин");
                else if (!responseManager.getResponse(command).isPasswordCorrect())
                    throw  new AuthorizationException( "Вы не авторизованы, неверный пароль");

            }

    }



//
//
//



//








}







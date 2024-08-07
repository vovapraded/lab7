package org.controller;

import javafx.application.Platform;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.common.commands.*;
import org.common.commands.authorization.Login;
import org.common.commands.authorization.Register;
import org.common.commands.inner.objects.Authorization;
import org.common.dto.Ticket;
import org.common.network.Response;
import org.controller.connect.to.graphic.ExceptionPublisher;
import org.example.Main;
import org.example.exception.ReceivingException;
import org.example.utility.ValidatorTicket;
import org.hibernate.sql.exec.ExecutionException;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MyController {

    @Getter
    private static final MyController instance = new MyController();
    private final Authorization authorization = new Authorization();
    private final ValidatorTicket validatorTicket= new ValidatorTicket();
    public synchronized   String login(String login,String password) throws Exception {
        authorization.setLogin(login);
        authorization.setPassword(password);
        Login command = new Login();
        var resp = sendCommand(command);
        return  resp.getMessageBySingleString();

    }
    public synchronized String register(String login, String password) throws Exception {
        authorization.setLogin(login);
        authorization.setPassword(password);
        Register command = new Register();
        var resp = sendCommand(command);
        return  resp.getMessageBySingleString();
    }
    public synchronized List<Ticket> show() throws Exception {
        Show command = new Show();
        var resp = sendCommand(command);
        return  resp.getTickets();
    }
    public synchronized ImmutablePair<String, List<Ticket>> remove(Long id) throws Exception {
        RemoveKey command = new RemoveKey();
        command.setStringArg(id.toString());
        var resp = sendCommand(command);

        return  new ImmutablePair<>(resp.getMessageBySingleString(),resp.getTickets());
    }
    public synchronized ImmutablePair<String, List<Ticket>> insert(Ticket ticket) throws Exception {
        validatorTicket.validateTicket(ticket);
        Insert command = new Insert();
        command.setTicketArg(ticket);
        command.setStringArg(ticket.getId().toString());
        var resp = sendCommand(command);


        return  new ImmutablePair<>(resp.getMessageBySingleString(),resp.getTickets());


    }
    public synchronized ImmutablePair<String, List<Ticket>> update(Ticket ticket) throws Exception {
        Update command = new Update();
        command.setTicketArg(ticket);
        command.setStringArg(ticket.getId().toString());
        var resp = sendCommand(command);
        System.out.println("ABOBA");
        return  new ImmutablePair<>(resp.getMessageBySingleString(),resp.getTickets());

    }
    public synchronized Response sendCommand(Command command) throws Exception {
        command.setAuthorization(authorization);

        try {
            Optional<Response> respOpt = null;
            var future = CompletableFuture.supplyAsync(() -> {
                try {
                    return Main.handleCommand(command);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            respOpt = future.get();

            if (respOpt.isPresent()) {
                var resp = respOpt.get();
                System.out.println("Ответ на команду " + command.getClass() + resp.getMessageBySingleString());
                if (resp.getException() != null) {
                    throw resp.getException();
                }
                return resp;
            } else {
                //поменять
                return new Response();
            }


        }catch (Exception  e){
            while (e.getCause()!=null){
                e= (RuntimeException) e.getCause();
            }
            handleException(e);
            return responseResending;
        }
    }

    private void handleException(Exception e) throws Exception {
        if (e instanceof ReceivingException) {
            ExceptionPublisher.notifyListeners(e);
            waitForButtonClick(((ReceivingException) e).getCommand());
        }else {
            throw e;
        }

    }

    @Getter
    private static final BlockingQueue<Object> clickQueue = new ArrayBlockingQueue<>(1);
    private Response responseResending;

    private void waitForButtonClick(Command command) throws Exception {
        try {
            Object o = null;
            while (o==null) {
                o=clickQueue.poll();
            }// Ожидание нажатия кнопки
            responseResending = sendCommand(command);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


//    private S
}

package org.common.commands;

import lombok.SneakyThrows;

import java.io.Serial;
import java.io.Serializable;

/**
 * The command outputs information about the collection
 */
public class Info extends Command implements Serializable {
    @Serial
    private static final long serialVersionUID = "Info".hashCode();


    @SneakyThrows
    @Override
    public void execute(){
        String s = "Дата инициализации "+collection.getCurrentDate()+
                ", Тип коллекции - HashMap, Кол-во элементов "+collection.getCountOfElements();
        responseManager.addToSend(s,this);
        loggerHelper.debug("Команда "+this.getClass().getName()+"от адресса "+responseManager.getResponse(this).getChannel().getRemoteAddress() +" выполнена");
        responseManager.send(this);
    }
    public void sendResponse(){

    }

    @Override
    public void validate(String arg1) {

    }
}

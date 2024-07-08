package org.common.commands;

import java.io.Serial;
import java.io.Serializable;

/**
 * The command outputs information about the collection
 */
public class Info extends Command implements Serializable {
    @Serial
    private static final long serialVersionUID = "Info".hashCode();


    @Override
    public void execute(){
        String s = "Дата инициализации "+collection.getCurrentDate()+
                ", Тип коллекции - HashMap, Кол-во элементов "+collection.getCountOfElements();
        responseManager.addToSend(s,this);
        loggingSuccess();
        responseManager.send(this);
    }
    public void sendResponse(){

    }

    @Override
    public void validate(String arg1) {

    }
}

package org.common.commands;


import org.common.dto.Ticket;
import org.common.utility.InvalidFormatException;
import org.common.utility.*;


import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * Output elements with venue.capacity less than this number
 */
public class FilterLessThanVenue extends Command implements Serializable {
    @Serial
    private static final long serialVersionUID = "FilterLessThanVenue".hashCode();



    @Override
    public void execute() {
        var capacityStr = stringArg;
        //если у каких-то билетов capacity=null то они в любом случае выписываются

            Long capacity= Long.parseLong(capacityStr);
            List<Ticket> filtered = collection.filterLessThanVenue(capacity);
            if (filtered.isEmpty()){
                responseManager.addToSend("Нет таких элементов",this);
            }
            for (Ticket ticket : filtered){
                responseManager.addToSend(ticket.toString(),this);
            }
        loggerHelper.debug("Команда "+this.getClass().getName()+"от адресса "+responseManager.getResponse(this).getAddress() +" выполнена");
        responseManager.send(this);

    }

    @Override
    public void validate(String arg1) {
        this.stringArg = arg1;
        if (!Validator.validate(arg1, TypesOfArgs.Long,false) || Long.parseLong(stringArg)<=0){
            throw new InvalidFormatException("ErrorIdMustBePositive",this);
        }
    }
}
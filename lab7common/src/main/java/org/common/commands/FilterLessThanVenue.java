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
                console.addToSend("Нет таких элементов",getAddress());
            }
            for (Ticket ticket : filtered){
                console.addToSend(ticket.toString(),getAddress());
            }
        console.send(getAddress());

    }

    @Override
    public void validate(String arg1) {
        this.stringArg = arg1;
        if (!Validator.validate(arg1, TypesOfArgs.Long,false) || Long.parseLong(stringArg)<=0){
            throw new InvalidFormatException("Id должен быть числом > 0",getAddress());
        }
    }
}
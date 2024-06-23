package org.example.utility;


import org.common.dto.Ticket;
import org.common.managers.Collection;
import org.common.utility.Console;
import org.common.utility.InvalidFormatException;

/**
 * The class is responsible for creating a ticket from the console
 */

public class ValidatorTicket {

    public void validateTicket(Ticket ticket) throws InvalidFormatException {
        var name = ticket.getName();
            if (name.isEmpty()|| name.contains(" ")|| name.contains("\t")|| name.contains("\n")) {
                throw new InvalidFormatException("ErrorNameFormat");
            }
            if (name.length()>128){
                throw new InvalidFormatException("ErrorNameSize");
            }


        var price = ticket.getPrice();
        if (price==null || price<=0){
            throw new InvalidFormatException("ErrorPriceMustBePositive");
        }

        var discount = ticket.getDiscount();
        if (discount != null && discount <= 0  ) {
            throw new InvalidFormatException("ErrorDiscountMustBePositive");
        }




//        считываем ticketType
        var ticketType = ticket.getTicketType();
        if (ticketType == null){
            throw new InvalidFormatException("ErrorTicketTypeCannotBeNull");
        }
       var x = ticket.getCoordinatesX();
        if (x == null) {
            throw new InvalidFormatException("ErrorXCannotBeNull");

        }
        var y = ticket.getCoordinatesY();
                if (y <= -618) {
                    throw new InvalidFormatException("ErrorYMustBeInTheRange");
                }


        //считываем venueName
        var venueName = ticket.getVenueName();
            if (venueName.isEmpty() || venueName.contains(" ")|| venueName.contains("\t")|| venueName.contains("\n")) {
                throw new InvalidFormatException("ErrorVenueNameFormat");
            }
            if (venueName.length()>128){
                throw new InvalidFormatException("ErrorVenueNameSize");

            }

        //считываем venueCapacity
        var venueCapacity = ticket.getVenueCapacity();
        if (venueCapacity == null || venueCapacity <= 0  ) {
        throw new InvalidFormatException("ErrorCapacityMustBePositive");
    }



        }

    }

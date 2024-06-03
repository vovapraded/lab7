package org.common.managers;

import org.common.dto.*;
import org.common.utility.Console;
import org.common.utility.InvalidFormatException;
import org.common.utility.TypesOfArgs;
import org.common.utility.Validator;


/**
 * The class is responsible for creating a ticket from the console
 */

public class CreateTicket {
    private  final Console console;
    private static final Collection collection=Collection.getInstance();
    public CreateTicket(Console console){
        this.console = console;
    }
    public  Ticket createTicket(Long id) throws InvalidFormatException {
        String name = "";
        while (name.isEmpty() || name.contains(" ")|| name.contains("\t")|| name.contains("\n")|| name.length()>128) {
            console.sendToController("Введите название билета");
            name = console.getInputFromCommand(1, 1);
            if (name.isEmpty()|| name.contains(" ")|| name.contains("\t")|| name.contains("\n")) {
                console.sendToController("Имя не должно быть пустым, не должно содержать пробелов");
            }
            if (name.length()>128){
                console.sendToController("Имя не должно быть больше 128 символов");

            }
        }
        Long price = -1L;
        while (price <= 0) {
            console.sendToController("Введите цену");
            String priceStr = console.getInputFromCommand(1, 1);
            if (!Validator.validate(priceStr, TypesOfArgs.Long, false)) {
                console.sendToController("Цена должна быть числом больше 0");
            } else if (Long.parseLong(priceStr) <= 0) {
                console.sendToController("Цена должна быть числом больше 0");
            } else {
                price = Long.parseLong(priceStr);
            }

        }

        Long discount = -1L;
        while (discount != null && discount <= 0  ) {
            console.sendToController("Введите скидку или пустую строку");
            String discountStr = console.getInputFromCommand(0, 1);
            if (Validator.validate(discountStr, TypesOfArgs.Long, false)) {
                discount = Long.parseLong(discountStr);
            } else if (discountStr.isEmpty()) {
                discount = null;
                break;
            }
            if (discount <= 0) {
                console.sendToController("Скидка, если есть, должна быть числом больше 0");
            }
        }


        Boolean refundable = null;
        console.sendToController("Введите возможность возврата или пустую строку");
        String refaundableStr = console.getInputFromCommand(0, 1);
        while (!Validator.validate(refaundableStr, TypesOfArgs.Boolean, true)) {
            console.sendToController("Возможность возврата, если есть, должна быть \"true\" или \"false\"");
            console.sendToController("Введите возможность возврата или пустую строку");
            refaundableStr = console.getInputFromCommand(0, 1);
        }
        if (!refaundableStr.isEmpty()) {
            refundable = Boolean.parseBoolean(refaundableStr);
        }


//        считываем ticketType

        String ticketTypeStr = "";
        console.sendToController("Введите тип билета");
        for (
                TicketType type : TicketType.values()) {
            console.sendToController(type.name());
        }
        ticketTypeStr = console.getInputFromCommand(1, 1);
        while (!Validator.validate(ticketTypeStr, TypesOfArgs.TicketType, false)) {
            console.sendToController("Вы неверно ввели тип билета");
            console.sendToController("Введите тип билета");
            for (TicketType type : TicketType.values()) {
                console.sendToController(type.name());
            }
            ticketTypeStr = console.getInputFromCommand(1, 1);
        }
        TicketType ticketType = TicketType.valueOf(ticketTypeStr.toUpperCase());
        Double x = null;
        //считываем x
        while (x == null) {
            console.sendToController("Введите координату X, где X число c плавающей точкой");
            String xstr = console.getInputFromCommand(1, 1);
            if (!Validator.validate(xstr, TypesOfArgs.Double, false)) {
                console.sendToController("X должен быть числом с плавающей точкой");
            } else {
                x = Double.parseDouble(xstr);
            }
        }
        Long y = Long.valueOf(-1000);
        String ystr;
        //считываем y
        while (y <= -618) {
            console.sendToController("Введите координату Y, где Y целое число > -618");
            ystr = console.getInputFromCommand(1, 1);
            if (!Validator.validate(ystr, TypesOfArgs.Long, false)) {
                console.sendToController("Y должен быть числом");
            } else {
                y = (long) Long.parseLong(ystr);
                if (y <= -618) {
                    console.sendToController("Y должен быть больше -618");
                }

            }
        }
        //считываем venueName
        String venueName = "";
        while (venueName.isEmpty() || venueName.length()>128 ) {
            console.sendToController("Введите Место встречи");
            venueName = console.getInputFromCommand(1, 1);
            if (venueName.isEmpty()) {
                console.sendToController("Неверный формат ввода, вы не ввели название места встречи");
            }
            if (venueName.length()>128){
                console.sendToController("Имя не должно быть больше 128 символов");

            }
        }
        //считываем venueCapacity
        Long venueCapacity = -1L;
        while (venueCapacity <= 0) {
            console.sendToController("Введите вместимость места встречи или пустую строку");
            String venueCapacityStr = console.getInputFromCommand(0, 1);
            if (venueCapacityStr.isEmpty()) {
                venueCapacity = null;
                break;
            } else if (Validator.validate(venueCapacityStr, TypesOfArgs.Long, false)) {
                venueCapacity = Long.parseLong(venueCapacityStr);
            }
            if (venueCapacity <= 0) {
                console.sendToController("Вместимость должна быть больше нуля");
            }

        }
        //считываем venueType
        VenueType venueType = null;
        while (venueType == null) {
            console.sendToController("Введите тип места встречи из предложенных или пустую строку");
            for (VenueType type : VenueType.values()) {
                console.sendToController(type.name());
            }
            String input = console.getInputFromCommand(0, 1);
            if (Validator.validate(input, TypesOfArgs.VenueType, false)) {
                venueType = VenueType.valueOf(input.toUpperCase());
            } else if (input.isEmpty()) {
                break;
            } else {
                console.sendToController("Вы неверно ввели тип места встречи");
            }
        }
        Venue venue = new Venue(venueType, venueCapacity, venueName);
        Coordinates coordinates = new Coordinates(x, y);
        Ticket ticket = new Ticket(name, coordinates, price, discount, refundable, ticketType, venue);
        ticket.setId(id);
        return ticket;
    }
}

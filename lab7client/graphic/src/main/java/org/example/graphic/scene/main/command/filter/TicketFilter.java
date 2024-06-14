package org.example.graphic.scene.main.command.filter;

import lombok.*;
import org.common.dto.TicketType;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TicketFilter {
    private Long idMax;
    private Long idMin;
    private String partOfName;
    private Long priceMin;
    private Long priceMax;
    private Long discountMin;
    private Long discountMax;
    private LocalDateTime dateMin;
    private LocalDateTime dateMax;
    private String createdBy;


    private List<Boolean> refundable=new ArrayList<>(Arrays.asList(Boolean.TRUE,Boolean.FALSE, null));
    private VenueFilter venueFilter = new VenueFilter();
    private CoordinatesFilter coordinatesFilter = new CoordinatesFilter();
    private List<TicketType> ticketTypes =   new ArrayList<>(Arrays.asList(TicketType.USUAL, TicketType.VIP, TicketType.BUDGETARY));
}

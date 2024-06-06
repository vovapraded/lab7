package org.example.graphic.scene.main.command.filter;

import lombok.*;
import org.common.dto.VenueType;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class VenueFilter {
    private String partOfName;
    private Long capacityMin;
    private Long capacityMax;
    private ArrayList<VenueType> venueTypes = new ArrayList<>(Arrays.asList(VenueType.BAR, VenueType.THEATRE, VenueType.CINEMA, VenueType.STADIUM,null));
}

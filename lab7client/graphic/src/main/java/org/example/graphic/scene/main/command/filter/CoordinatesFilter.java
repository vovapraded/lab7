package org.example.graphic.scene.main.command.filter;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class CoordinatesFilter {
    private Double xMin;
    private Double xMax;
    private Long yMin;
    private Long yMax;
}

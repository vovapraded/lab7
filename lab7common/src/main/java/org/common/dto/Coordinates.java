package org.common.dto;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.io.Serial;
import java.io.Serializable;

/**
 * a class for storing coordinates data
 */
@EqualsAndHashCode
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "coordinates",schema = "s409397")
public class Coordinates implements Serializable {
    @Serial
    private static final long serialVersionUID = "Coordinates".hashCode();
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "coord_seq")
    @SequenceGenerator(name = "coord_seq", sequenceName = "coordinates_id_seq",schema = "s409397", allocationSize = 1)
    private Long id;
    private Double x; //Поле не может быть null
    private long y; //Значение поля должно быть больше -618
    public Coordinates(Double x, long y) {
        this.x = x;
        this.y=y;

    }
    @Override
    public String toString(){
        return x+";"+y+" id:"+id;
    }
}

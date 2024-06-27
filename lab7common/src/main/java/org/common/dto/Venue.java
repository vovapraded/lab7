package org.common.dto;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;


import java.io.Serial;
import java.io.Serializable;

/**
 * a class for storing venue data
 */
@EqualsAndHashCode(callSuper = false)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "venue",schema = "s409397")
public class Venue extends ElementsWithId implements Serializable {
    @Serial
    private static final long serialVersionUID = "Venue".hashCode();
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "venue_seq")
    @SequenceGenerator(name = "venue_seq", sequenceName = "venue_id_seq",schema = "s409397", allocationSize = 1)    Long id;
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Long capacity; //Поле может быть null, Значение поля должно быть больше 0
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Basic(optional=true)
    @Column(name = "venue_type",columnDefinition = "venue_type_")
    private VenueType venueType; //Поле может быть null

    public Venue(VenueType type,Long capacity,String name) {
        this.id = null;
        this.venueType = type;
        this.capacity=capacity;
        this.name = name;
    }

    public Venue(VenueType type,Long capacity,String name,Long id) {
        this.id = id;
        this.venueType = type;
        this.capacity=capacity;
        this.name = name;
    }


    @Override
    public String toString(){
        return  "venueName " + name+
                ", venueType "+ venueType +
                ", venueId " + id+
                ", venueCapacity " + capacity;
    }



    public Long getCapacity() {
        return capacity;
    }


}

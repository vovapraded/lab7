package org.common.dto;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
/**
 * a class for storing ticket data
 */
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@Getter
@Setter
@Data
@NoArgsConstructor
@Entity
@Table(name = "ticket",schema = "s409397")
public class Ticket extends ElementsWithId implements Comparable<Ticket>, Serializable {
    @Serial
    private static final long serialVersionUID = "Ticket".hashCode();
    @Id
    Long id;
    private String name; //Поле не может быть null, Строка не может быть пустой
    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "coordinates_id")
    private Coordinates coordinates; //Поле не может быть null
    @Column(name = "creation_date")
    private LocalDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Long price; //Поле не может быть null, Значение поля должно быть больше 0
    private Long discount; //Поле может быть null, Значение поля должно быть больше 0, Максимальное значение поля: 100
    private Boolean refundable; //Поле может быть null
    @Enumerated(EnumType.STRING)
    @Column(name = "ticket_type")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Basic(optional=false)
    private TicketType ticketType; //Поле не может быть null
    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "venue_id")
    private Venue venue; //Поле не может быть null
    @Column(name = "created_by")
    private  String createdBy; //Поле не может быть null, Строка не может быть пустой

    //конструктор без даты и id

    public Ticket(String name,Coordinates coordinates,Long price,Long discount,Boolean refundable,TicketType type,Venue venue){
        this.name= name;
        this.coordinates=coordinates;
        LocalDateTime currentDate =  LocalDateTime.now();
        this.creationDate=currentDate;
        this.price=price;
        this.discount = discount;
        this.refundable = refundable;
        this.ticketType = type;
        this.venue=venue;

    }
    //конструктор без даты но с id
    public Ticket(Long id, String name,Coordinates coordinates,Long price,Long discount,Boolean refundable,TicketType type,Venue venue){
        this.id = id;
        this.name= name;
        this.coordinates=coordinates;
        LocalDateTime currentDate =  LocalDateTime.now();
        this.creationDate=currentDate;
        this.price=price;
        this.discount = discount;
        this.refundable = refundable;
        this.ticketType = type;
        this.venue=venue;
    }

    //конструктор с датой и id

    public Ticket(Long id, String name, Coordinates coordinates, LocalDateTime creationDate, Long price, Long discount, Boolean refundable, TicketType type, Venue venue){
        this.name= name;
        this.id = id;
        this.coordinates=coordinates;
        this.creationDate = creationDate;
        this.price=price;
        this.discount = discount;
        this.refundable = refundable;
        this.ticketType = type;
        this.venue=venue;
    }





    @Override
    public int compareTo(Ticket other){
        return  Long.compare(price,other.getPrice());

    }


    @Override
    public String toString(){
        return "id "+id+
                ", name "+name+
                ", coordinates "+getCoordinates().toString()+
                ", creationDate "+ creationDate.format(DateTimeFormatter.ISO_DATE_TIME).toString().replace("T"," ")+
                ", price " +price+
                ", discount " +discount+
                ", refundable " +refundable+
                ", ticketType "+ ticketType +
                ", venue " + venue.toString()+
                ", created by " + createdBy;
    }
    public Long getVenueCapacity() {
        return venue != null ? venue.getCapacity() : null;
    }
    public String getVenueName() {
        return venue != null ? venue.getName() : null;
    }
    public VenueType getVenueType() {
        return venue != null ? venue.getVenueType() : null;
    }
    public Double getCoordinatesX() {
        return coordinates != null ? coordinates.getX() : null;
    }
    public Long getCoordinatesY() {
        return coordinates != null ? coordinates.getY() : null;
    }
}


package org.common.dto;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * a class for storing ticket data
 */
@Builder
@AllArgsConstructor
@Getter
@Setter
@Data
@NoArgsConstructor
@Entity
@Table(name = "ticket", schema = "s409397")
public class Ticket extends ElementsWithId implements Comparable<Ticket>, Serializable {
    @Serial
    private static final long serialVersionUID = "Ticket".hashCode();
    @Id
    Long id;
    private String name; //Поле не может быть null, Строка не может быть пустой
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
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
    @Basic(optional = false)
    private TicketType ticketType; //Поле не может быть null
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "venue_id")
    private Venue venue; //Поле не может быть null
    @Column(name = "created_by")
    private String createdBy; //Поле не может быть null, Строка не может быть пустой

    //конструктор без даты и id

    public Ticket(String name, Coordinates coordinates, Long price, Long discount, Boolean refundable, TicketType type, Venue venue) {
        this.name = name;
        this.coordinates = coordinates;
        LocalDateTime currentDate = LocalDateTime.now();
        this.creationDate = currentDate;
        this.price = price;
        this.discount = discount;
        this.refundable = refundable;
        this.ticketType = type;
        this.venue = venue;

    }

    //конструктор без даты но с id
    public Ticket(Long id, String name, Coordinates coordinates, Long price, Long discount, Boolean refundable, TicketType type, Venue venue) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        LocalDateTime currentDate = LocalDateTime.now();
        this.creationDate = currentDate;
        this.price = price;
        this.discount = discount;
        this.refundable = refundable;
        this.ticketType = type;
        this.venue = venue;
    }

    //конструктор с датой и id

    public Ticket(Long id, String name, Coordinates coordinates, LocalDateTime creationDate, Long price, Long discount, Boolean refundable, TicketType type, Venue venue) {
        this.name = name;
        this.id = id;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.price = price;
        this.discount = discount;
        this.refundable = refundable;
        this.ticketType = type;
        this.venue = venue;
    }


    @Override
    public int compareTo(Ticket other) {
        return Long.compare(price, other.getPrice());

    }


    @Override
    public String toString() {
        return "id " + id +
                ", name " + name +
                ", coordinates " + getCoordinates().toString() +
                ", creationDate " + creationDate.format(DateTimeFormatter.ISO_DATE_TIME).toString().replace("T", " ") +
                ", price " + price +
                ", discount " + discount +
                ", refundable " + refundable +
                ", ticketType " + ticketType +
                ", venue " + venue.toString() +
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

    public void setVenueCapacity(Long capacity) {
        venue.setCapacity(capacity);
    }

    public void setVenueName(String name) {
        venue.setName(name);
    }

    public void setVenueType(VenueType venueType) {
        venue.setVenueType(venueType);
    }

    public void setCoordinatesX(Double x) {
        coordinates.setX(x);
    }

    public void setCoordinatesY(Long y) {
        coordinates.setY(y);
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Ticket)) return false;
        final Ticket other = (Ticket) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
        final Object this$coordinates = this.getCoordinates();
        final Object other$coordinates = other.getCoordinates();
        if (this$coordinates == null ? other$coordinates != null : !this$coordinates.equals(other$coordinates))
            return false;
        final Object this$creationDate = this.getCreationDate();
        final Object other$creationDate = other.getCreationDate();
        if (this$creationDate == null ? other$creationDate != null : !this$creationDate.equals(other$creationDate))
            return false;
        final Object this$price = this.getPrice();
        final Object other$price = other.getPrice();
        if (this$price == null ? other$price != null : !this$price.equals(other$price)) return false;
        final Object this$discount = this.getDiscount();
        final Object other$discount = other.getDiscount();
        if (this$discount == null ? other$discount != null : !this$discount.equals(other$discount)) return false;
        final Object this$refundable = this.getRefundable();
        final Object other$refundable = other.getRefundable();
        if (this$refundable == null ? other$refundable != null : !this$refundable.equals(other$refundable))
            return false;
        final Object this$ticketType = this.getTicketType();
        final Object other$ticketType = other.getTicketType();
        if (this$ticketType == null ? other$ticketType != null : !this$ticketType.equals(other$ticketType))
            return false;
        final Object this$venue = this.getVenue();
        final Object other$venue = other.getVenue();
        if (this$venue == null ? other$venue != null : !this$venue.equals(other$venue)) return false;
        final Object this$createdBy = this.getCreatedBy();
        final Object other$createdBy = other.getCreatedBy();
        if (this$createdBy == null ? other$createdBy != null : !this$createdBy.equals(other$createdBy)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Ticket;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        final Object $coordinates = this.getCoordinates();
        result = result * PRIME + ($coordinates == null ? 43 : $coordinates.hashCode());
        final Object $creationDate = this.getCreationDate();
        result = result * PRIME + ($creationDate == null ? 43 : $creationDate.hashCode());
        final Object $price = this.getPrice();
        result = result * PRIME + ($price == null ? 43 : $price.hashCode());
        final Object $discount = this.getDiscount();
        result = result * PRIME + ($discount == null ? 43 : $discount.hashCode());
        final Object $refundable = this.getRefundable();
        result = result * PRIME + ($refundable == null ? 43 : $refundable.hashCode());
        final Object $ticketType = this.getTicketType();
        result = result * PRIME + ($ticketType == null ? 43 : $ticketType.hashCode());
        final Object $venue = this.getVenue();
        result = result * PRIME + ($venue == null ? 43 : $venue.hashCode());
        final Object $createdBy = this.getCreatedBy();
        result = result * PRIME + ($createdBy == null ? 43 : $createdBy.hashCode());
        return result;
    }
}


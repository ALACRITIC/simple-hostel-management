package org.remipassmoilesel.bookme.reservations;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.remipassmoilesel.bookme.customers.Customer;
import org.remipassmoilesel.bookme.sharedresources.SharedResource;

import java.util.Date;
import java.util.Objects;

/**
 * Created by remipassmoilesel on 30/01/17.
 */
@DatabaseTable(tableName = "RESERVATIONS")
public class Reservation {

    public static final String ID_FIELD_NAME = "ID";
    public static final String CUSTOMER_FIELD_NAME = "CUSTOMER";
    public static final String RESERVATION_DATE = "RESERVATIONDATE";
    public static final String SHARED_RESOURCE_FIELD_NAME = "SHARED_RESOURCE";
    public static final String PLACES_FIELD_NAME = "PLACES";
    public static final String DATEBEGIN_FIELD_NAME = "BEGIN";
    public static final String DATEEND_FIELD_NAME = "END";
    public static final String COMMENT_FIELD_NAME = "COMMENT";
    public static final String PAID_FIELD_NAME = "PAID";

    @DatabaseField(generatedId = true, columnName = ID_FIELD_NAME)
    private long id;

    @DatabaseField(foreign = true, columnName = CUSTOMER_FIELD_NAME)
    private Customer customer;

    @DatabaseField(foreign = true, columnName = SHARED_RESOURCE_FIELD_NAME)
    private SharedResource resource;

    @DatabaseField(columnName = PLACES_FIELD_NAME)
    private int places;

    @DatabaseField(columnName = RESERVATION_DATE)
    private Date reservationDate;

    @DatabaseField(columnName = DATEBEGIN_FIELD_NAME)
    private Date begin;

    @DatabaseField(columnName = DATEEND_FIELD_NAME)
    private Date end;

    @DatabaseField(columnName = COMMENT_FIELD_NAME, dataType = DataType.LONG_STRING)
    private String comment;

    @DatabaseField(columnName = PAID_FIELD_NAME)
    private boolean paid;

    public Reservation() {
        // ORMLite needs a no-arg constructor
    }

    public Reservation(Customer customer, SharedResource resource, int places, Date begin, Date end) {
        this(customer, resource, places, begin, end, null);
    }

    public Reservation(Customer customer, SharedResource resource, int places, Date begin, Date end, Date reservationDate) {
        this.customer = customer;
        this.begin = begin;
        this.end = end;
        this.resource = resource;
        this.places = places;

        if (end.getTime() < begin.getTime()) {
            throw new IllegalStateException("Begin date is after end date: begin/ " + begin + " end/ " + end);
        }

        if (reservationDate == null) {
            reservationDate = new Date();
        }
        this.reservationDate = reservationDate;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getBegin() {
        return begin;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Date getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(Date reservationDate) {
        this.reservationDate = reservationDate;
    }

    public SharedResource getResource() {
        return resource;
    }

    public void setResource(SharedResource resource) {
        this.resource = resource;
    }

    public int getPlaces() {
        return places;
    }

    public void setPlaces(int places) {
        this.places = places;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return id == that.id &&
                places == that.places &&
                paid == that.paid &&
                Objects.equals(customer, that.customer) &&
                Objects.equals(resource, that.resource) &&
                Objects.equals(reservationDate, that.reservationDate) &&
                Objects.equals(begin, that.begin) &&
                Objects.equals(end, that.end) &&
                Objects.equals(comment, that.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customer, resource, places, reservationDate, begin, end, comment, paid);
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", customer=" + customer +
                ", resource=" + resource +
                ", places=" + places +
                ", reservationDate=" + reservationDate +
                ", begin=" + begin +
                ", end=" + end +
                ", comment='" + comment + '\'' +
                ", paid=" + paid +
                '}';
    }
}

package org.remipassmoilesel.bookingsystem.reservations;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.remipassmoilesel.bookingsystem.customers.Customer;
import org.remipassmoilesel.bookingsystem.sharedresources.SharedResource;

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
    public static final String DATEBEGIN_FIELD_NAME = "BEGIN";
    public static final String DATEEND_FIELD_NAME = "END";
    public static final String SHARED_RESOURCE = "SHARED_RESOURCE";

    @DatabaseField(generatedId = true, columnName = ID_FIELD_NAME)
    private int id;

    @DatabaseField(foreign = true, columnName = CUSTOMER_FIELD_NAME)
    private Customer customer;

    @DatabaseField(foreign = true, columnName = SHARED_RESOURCE)
    private SharedResource resource;

    @DatabaseField(columnName = RESERVATION_DATE)
    private Date reservationDate;

    @DatabaseField(columnName = DATEBEGIN_FIELD_NAME)
    private Date begin;

    @DatabaseField(columnName = DATEEND_FIELD_NAME)
    private Date end;

    public Reservation() {
        // ORMLite needs a no-arg constructor
    }

    public Reservation(Customer customer, SharedResource resource, Date begin, Date end, Date reservationDate) {
        this.customer = customer;
        this.begin = begin;
        this.end = end;
        this.resource = resource;

        if (end.getTime() < begin.getTime()) {
            throw new IllegalStateException("Departure is after arrival: a/ " + begin + " d/ " + end);
        }

        if (reservationDate == null) {
            reservationDate = new Date();
        }
        this.reservationDate = reservationDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", customer=" + customer +
                ", resource=" + resource +
                ", reservationDate=" + reservationDate +
                ", arrival=" + begin +
                ", departure=" + end +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return id == that.id &&
                Objects.equals(customer, that.customer) &&
                Objects.equals(resource, that.resource) &&
                Objects.equals(reservationDate, that.reservationDate) &&
                Objects.equals(begin, that.begin) &&
                Objects.equals(end, that.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customer, resource, reservationDate, begin, end);
    }

}

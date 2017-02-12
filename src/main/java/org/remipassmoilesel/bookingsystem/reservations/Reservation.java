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
    public static final String DATEARRIVAL_FIELD_NAME = "ARRIVAL";
    public static final String DATEDEPARTURE_FIELD_NAME = "DEPARTURE";
    public static final String SHARED_RESOURCE = "SHARED_RESOURCE";

    @DatabaseField(generatedId = true, columnName = ID_FIELD_NAME)
    private int id;

    @DatabaseField(foreign = true, columnName = CUSTOMER_FIELD_NAME)
    private Customer customer;

    @DatabaseField(foreign = true, columnName = SHARED_RESOURCE)
    private SharedResource resource;

    @DatabaseField(columnName = RESERVATION_DATE)
    private Date reservationDate;

    @DatabaseField(columnName = DATEARRIVAL_FIELD_NAME)
    private Date arrival;

    @DatabaseField(columnName = DATEDEPARTURE_FIELD_NAME)
    private Date departure;

    public Reservation() {
        // ORMLite needs a no-arg constructor
    }

    public Reservation(Customer customer, SharedResource resource, Date arrival, Date departure, Date reservationDate) {
        this.customer = customer;
        this.arrival = arrival;
        this.departure = departure;
        this.resource = resource;

        if (departure.getTime() > arrival.getTime()) {
            throw new IllegalStateException("Departure is after arrival: a/ " + arrival + " d/ " + departure);
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

    public Date getArrival() {
        return arrival;
    }

    public void setArrival(Date arrival) {
        this.arrival = arrival;
    }

    public Date getDeparture() {
        return departure;
    }

    public void setDeparture(Date departure) {
        this.departure = departure;
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
                ", arrival=" + arrival +
                ", departure=" + departure +
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
                Objects.equals(arrival, that.arrival) &&
                Objects.equals(departure, that.departure);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customer, resource, reservationDate, arrival, departure);
    }

}

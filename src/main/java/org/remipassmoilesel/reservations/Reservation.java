package org.remipassmoilesel.reservations;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.remipassmoilesel.customers.Customer;

import java.util.Date;
import java.util.Objects;

/**
 * Created by remipassmoilesel on 30/01/17.
 */
@DatabaseTable(tableName = "CUSTOMERS")
public class Reservation {

    public static final String ID_FIELD_NAME = "id";
    public static final String CUSTOMER_ID_FIELD_NAME = "customerid";
    public static final String RESERVATION_DATE = "reservationdate";
    public static final String DATEARRIVEL_FIELD_NAME = "arrival";
    public static final String DATEDEPARTURE_FIELD_NAME = "departure";

    @DatabaseField(generatedId = true, columnName = ID_FIELD_NAME)
    private int id;

    @DatabaseField(foreign = true)
    private Customer customer;

    @DatabaseField(columnName = RESERVATION_DATE)
    private Date reservationDate;

    @DatabaseField(columnName = DATEARRIVEL_FIELD_NAME)
    private Date arrival;

    @DatabaseField(columnName = DATEDEPARTURE_FIELD_NAME)
    private Date departure;

    public Reservation() {
        // ORMLite needs a no-arg constructor
    }

    public Reservation(Customer customer, Date arrival, Date departure) {
        this.customer = customer;
        this.arrival = arrival;
        this.departure = departure;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return id == that.id &&
                Objects.equals(customer, that.customer) &&
                Objects.equals(reservationDate, that.reservationDate) &&
                Objects.equals(arrival, that.arrival) &&
                Objects.equals(departure, that.departure);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customer, reservationDate, arrival, departure);
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", customer=" + customer +
                ", reservationDate=" + reservationDate +
                ", arrival=" + arrival +
                ", departure=" + departure +
                '}';
    }
}

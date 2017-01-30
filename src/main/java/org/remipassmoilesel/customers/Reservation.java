package org.remipassmoilesel.customers;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;
import java.util.Objects;

/**
 * Created by remipassmoilesel on 30/01/17.
 */
@DatabaseTable(tableName = "CUSTOMERS")
public class Reservation {

    private static final String ID_FIELD_NAME = "id";
    private static final String CUSTOMER_ID_FIELD_NAME = "customerid";
    private static final String DATEARRIVEL_FIELD_NAME = "arrival";
    private static final String DATEDEPARTURE_FIELD_NAME = "departure";

    @DatabaseField(generatedId = true, columnName = ID_FIELD_NAME)
    private int id;


    @DatabaseField(generatedId = true, columnName = CUSTOMER_ID_FIELD_NAME)
    private long customerId;

    @DatabaseField(columnName = DATEARRIVEL_FIELD_NAME)
    private Date arrival;

    @DatabaseField(columnName = DATEDEPARTURE_FIELD_NAME)
    private Date departure;

    public Reservation() {
        // ORMLite needs a no-arg constructor
    }

    public Reservation(Customer customer, Date arrival, Date departure) {
        this.customerId = customer.getId();
        this.arrival = arrival;
        this.departure = departure;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return id == that.id &&
                customerId == that.customerId &&
                Objects.equals(arrival, that.arrival) &&
                Objects.equals(departure, that.departure);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customerId, arrival, departure);
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", arrival=" + arrival +
                ", departure=" + departure +
                '}';
    }
}

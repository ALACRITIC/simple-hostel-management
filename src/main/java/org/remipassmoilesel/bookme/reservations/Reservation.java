package org.remipassmoilesel.bookme.reservations;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.remipassmoilesel.bookme.accommodations.Accommodation;
import org.remipassmoilesel.bookme.customers.Customer;

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
    public static final String ACCOMMODATION_FIELD_NAME = "ACCOMMODATION";
    public static final String PLACES_FIELD_NAME = "PLACES";
    public static final String DATEBEGIN_FIELD_NAME = "BEGIN";
    public static final String DATEEND_FIELD_NAME = "END";
    public static final String COMMENT_FIELD_NAME = "COMMENT";
    public static final String PAID_FIELD_NAME = "PAID";
    public static final String TOTAL_PRICE_FIELD_NAME = "TOTAL_PRICE";

    @DatabaseField(generatedId = true, columnName = ID_FIELD_NAME)
    private long id;

    @DatabaseField(foreign = true, columnName = CUSTOMER_FIELD_NAME)
    private Customer customer;

    @DatabaseField(foreign = true, columnName = ACCOMMODATION_FIELD_NAME)
    private Accommodation accommodation;

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

    @DatabaseField(columnName = TOTAL_PRICE_FIELD_NAME)
    private double totalPrice;

    public Reservation() {
        // ORMLite needs a no-arg constructor
    }

    public Reservation(Customer customer, Accommodation accommodation, int places, Date begin, Date end) {
        this(customer, accommodation, places, begin, end, null);
    }

    public Reservation(Customer customer, Accommodation accommodation, int places, Date begin, Date end, Date reservationDate) {
        this.customer = customer;
        this.begin = begin;
        this.end = end;
        this.accommodation = accommodation;
        this.places = places;

        if (end.getTime() < begin.getTime()) {
            throw new IllegalStateException("Begin date is after end date: begin/ " + begin + " end/ " + end);
        }

        if (reservationDate == null) {
            reservationDate = new Date();
        }
        this.reservationDate = reservationDate;
    }

    @JsonIgnore
    public Duration getDuration() {
        return new Duration(new DateTime(getBegin()), new DateTime(getEnd()));
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

    public Accommodation getAccommodation() {
        return accommodation;
    }

    public void setAccommodation(Accommodation accommodation) {
        this.accommodation = accommodation;
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

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    @JsonIgnore
    public void computeStandardTotalPrice() {
        Duration duration = new Duration(getBegin().getTime(), getEnd().getTime());
        double price = getAccommodation().getPricePerDay() * (duration.getStandardHours() / 18);
        totalPrice = Math.round(price * 100) / 100;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return id == that.id &&
                places == that.places &&
                paid == that.paid &&
                Double.compare(that.totalPrice, totalPrice) == 0 &&
                Objects.equals(customer, that.customer) &&
                Objects.equals(accommodation, that.accommodation) &&
                Objects.equals(reservationDate, that.reservationDate) &&
                Objects.equals(begin, that.begin) &&
                Objects.equals(end, that.end) &&
                Objects.equals(comment, that.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customer, accommodation, places, reservationDate, begin, end, comment, paid, totalPrice);
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", customer=" + customer +
                ", accommodation=" + accommodation +
                ", places=" + places +
                ", reservationDate=" + reservationDate +
                ", begin=" + begin +
                ", end=" + end +
                ", comment='" + comment + '\'' +
                ", paid=" + paid +
                ", totalPrice=" + totalPrice +
                '}';
    }

}

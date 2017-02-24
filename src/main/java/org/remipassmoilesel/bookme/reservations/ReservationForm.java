package org.remipassmoilesel.bookme.reservations;

import org.joda.time.DateTime;
import org.remipassmoilesel.bookme.utils.Utils;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * Created by remipassmoilesel on 30/01/17.
 */
public class ReservationForm {

    @NotNull
    @Size(min = 2, max = 50)
    private String customerFirstname;

    @NotNull
    @Size(min = 2, max = 50)
    private String customerLastname;

    @NotNull
    @Size(min = 2, max = 50)
    @Pattern(regexp = "\\+?[0-9]+")
    private String customerPhonenumber;

    @NotNull
    private Long customerId = -1l;

    @NotNull
    @Min(1)
    private int places = 1;

    @NotNull
    // 24/02/2017 16:00
    @Pattern(regexp = "[0-9]{2}/[0-9]{2}/[0-9]{4} [0-9]{2}:[0-9]{2}")
    private String begin;

    @NotNull
    @Pattern(regexp = "[0-9]{2}/[0-9]{2}/[0-9]{4} [0-9]{2}:[0-9]{2}")
    private String end;

    @NotNull
    private Long sharedResourceId = -1l;

    @NotNull
    private Long reservationId = -1l;

    @Size(max = 2000)
    private String comment;

    @NotNull
    private Boolean paid = false;

    @NotNull
    private Long token;

    public ReservationForm() {
        begin = new DateTime().toString("dd/MM/yyyy") + " 16:00";
        end = new DateTime().plusDays(5).toString("dd/MM/yyyy" + " 10:00");
    }

    /**
     * Load a reservation in form
     *
     * @param reservation
     */
    public void load(Reservation reservation) {

        if (reservation == null) {
            return;
        }

        if (reservation.getCustomer() != null) {
            setCustomerFirstname(reservation.getCustomer().getFirstname());
            setCustomerLastname(reservation.getCustomer().getLastname());
            setCustomerPhonenumber(reservation.getCustomer().getPhonenumber());
        }

        if (reservation.getBegin() != null) {
            setBegin(Utils.dateToString(reservation.getBegin(), "dd/MM/YYYY HH:mm"));
        }

        if (reservation.getEnd() != null) {
            setEnd(Utils.dateToString(reservation.getEnd(), "dd/MM/YYYY HH:mm"));
        }

        if (reservation.getResource() != null) {
            setSharedResourceId(reservation.getResource().getId());
        }

        setReservationId(reservation.getId());
        setCustomerId(reservation.getCustomer().getId());
        setPaid(reservation.isPaid());
    }

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public String getCustomerPhonenumber() {
        return customerPhonenumber;
    }

    public void setCustomerPhonenumber(String customerPhonenumber) {
        this.customerPhonenumber = customerPhonenumber;
    }

    public void setCustomerFirstname(String customerFirstname) {
        this.customerFirstname = customerFirstname;
    }

    public void setCustomerLastname(String customerLastname) {
        this.customerLastname = customerLastname;
    }

    public void setBegin(String begin) {
        this.begin = begin;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getCustomerFirstname() {
        return customerFirstname;
    }

    public String getCustomerLastname() {
        return customerLastname;
    }

    public String getBegin() {
        return begin;
    }

    public String getEnd() {
        return end;
    }

    public Long getToken() {
        return token;
    }

    public void setToken(Long token) {
        this.token = token;
    }

    public long getSharedResourceId() {
        return sharedResourceId;
    }

    public void setSharedResourceId(long sharedResourceId) {
        this.sharedResourceId = sharedResourceId;
    }

    public int getPlaces() {
        return places;
    }

    public void setPlaces(int places) {
        this.places = places;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setReservationId(long reservationId) {
        this.reservationId = reservationId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReservationForm that = (ReservationForm) o;
        return places == that.places &&
                sharedResourceId == that.sharedResourceId &&
                reservationId == that.reservationId &&
                customerId == that.customerId &&
                paid == that.paid &&
                Objects.equals(customerFirstname, that.customerFirstname) &&
                Objects.equals(customerLastname, that.customerLastname) &&
                Objects.equals(customerPhonenumber, that.customerPhonenumber) &&
                Objects.equals(begin, that.begin) &&
                Objects.equals(end, that.end) &&
                Objects.equals(comment, that.comment) &&
                Objects.equals(token, that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerFirstname, customerLastname, customerPhonenumber, places, begin, end, sharedResourceId, reservationId, customerId, comment, paid, token);
    }

    @Override
    public String toString() {
        return "ReservationForm{" +
                "customerFirstname='" + customerFirstname + '\'' +
                ", customerLastname='" + customerLastname + '\'' +
                ", customerPhonenumber='" + customerPhonenumber + '\'' +
                ", places=" + places +
                ", begin='" + begin + '\'' +
                ", end='" + end + '\'' +
                ", sharedResourceId=" + sharedResourceId +
                ", reservationId=" + reservationId +
                ", customerId=" + customerId +
                ", comment='" + comment + '\'' +
                ", paid=" + paid +
                ", token=" + token +
                '}';
    }
}

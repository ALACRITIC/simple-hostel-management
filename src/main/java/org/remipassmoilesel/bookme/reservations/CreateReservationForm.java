package org.remipassmoilesel.bookme.reservations;

import org.joda.time.DateTime;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * Created by remipassmoilesel on 30/01/17.
 */
public class CreateReservationForm {

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
    @Min(1)
    private int places = 1;

    @NotNull
    @Size(min = 10, max = 10)
    private String begin;

    @NotNull
    @Size(min = 10, max = 10)
    private String end;

    @NotNull
    private long sharedResourceId = -1;

    @NotNull
    private long reservationId = -1;

    @NotNull
    private long customerId = -1;

    @Size(max = 2000)
    private String comment;

    @NotNull
    private Long token;

    public CreateReservationForm() {

    }

    /**
     * Load a reservation in form
     *
     * @param res
     */
    public void load(Reservation res) {

        if (res.getCustomer() != null) {
            setCustomerFirstname(res.getCustomer().getFirstname());
            setCustomerLastname(res.getCustomer().getLastname());
            setCustomerPhonenumber(res.getCustomer().getPhonenumber());
        }

        if (res.getBegin() != null) {
            setBegin(new DateTime(res.getBegin()).toString("dd/MM/YYYY"));
        }

        if (res.getEnd() != null) {
            setEnd(new DateTime(res.getEnd()).toString("dd/MM/YYYY"));
        }

        if (res.getResource() != null) {
            setSharedResourceId(res.getResource().getId());
        }

        setReservationId(res.getId());
        setCustomerId(res.getCustomer().getId());
    }

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
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
        CreateReservationForm that = (CreateReservationForm) o;
        return places == that.places &&
                sharedResourceId == that.sharedResourceId &&
                reservationId == that.reservationId &&
                customerId == that.customerId &&
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
        return Objects.hash(customerFirstname, customerLastname, customerPhonenumber, places, begin, end, sharedResourceId, reservationId, customerId, comment, token);
    }

    @Override
    public String toString() {
        return "CreateReservationForm{" +
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
                ", token=" + token +
                '}';
    }
}

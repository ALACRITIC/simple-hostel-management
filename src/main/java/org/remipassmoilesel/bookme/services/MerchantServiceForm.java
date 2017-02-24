package org.remipassmoilesel.bookme.services;

import org.remipassmoilesel.bookme.utils.Utils;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * Created by remipassmoilesel on 30/01/17.
 */
public class MerchantServiceForm {

    @NotNull
    @Min(0)
    private int totalPrice;

    @NotNull
    private long id = -1;

    @NotNull
    @Size(max = 2000)
    private String comment;

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
    @Pattern(regexp = "[0-9]{2}/[0-9]{2}/[0-9]{4} [0-9]{2}:[0-9]{2}")
    private String executionDate;

    @NotNull
    private Long serviceType = -1l;

    @NotNull
    private Boolean paid = false;

    @NotNull
    private Boolean scheduled = false;

    @NotNull
    private Long token;

    public MerchantServiceForm() {

    }

    /**
     * Load a service in form
     *
     * @param bill
     */
    public void load(MerchantService bill) {

        if (bill == null) {
            return;
        }

        setTotalPrice(bill.getTotalPrice());
        setId(bill.getId());

        if (bill.getCustomer() != null) {
            setCustomerId(bill.getCustomer().getId());
            setCustomerFirstname(bill.getCustomer().getFirstname());
            setCustomerLastname(bill.getCustomer().getLastname());
            setCustomerPhonenumber(bill.getCustomer().getPhonenumber());
        }

        if (bill.getExecutionDate() != null) {
            setExecutionDate(Utils.dateToString(bill.getExecutionDate()));
        }

        if (bill.getServiceType() != null) {
            setServiceType(bill.getServiceType().getId());
        }

        setComment(bill.getComment());

    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCustomerFirstname() {
        return customerFirstname;
    }

    public void setCustomerFirstname(String customerFirstname) {
        this.customerFirstname = customerFirstname;
    }

    public String getCustomerLastname() {
        return customerLastname;
    }

    public void setCustomerLastname(String customerLastname) {
        this.customerLastname = customerLastname;
    }

    public String getCustomerPhonenumber() {
        return customerPhonenumber;
    }

    public void setCustomerPhonenumber(String customerPhonenumber) {
        this.customerPhonenumber = customerPhonenumber;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public String getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(String executionDate) {
        this.executionDate = executionDate;
    }


    public long getServiceType() {
        return serviceType;
    }

    public void setServiceType(long serviceType) {
        this.serviceType = serviceType;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public boolean isScheduled() {
        return scheduled;
    }

    public void setScheduled(boolean scheduled) {
        this.scheduled = scheduled;
    }

    public Long getToken() {
        return token;
    }

    public void setToken(Long token) {
        this.token = token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MerchantServiceForm that = (MerchantServiceForm) o;
        return totalPrice == that.totalPrice &&
                id == that.id &&
                customerId == that.customerId &&
                serviceType == that.serviceType &&
                paid == that.paid &&
                scheduled == that.scheduled &&
                Objects.equals(comment, that.comment) &&
                Objects.equals(customerFirstname, that.customerFirstname) &&
                Objects.equals(customerLastname, that.customerLastname) &&
                Objects.equals(customerPhonenumber, that.customerPhonenumber) &&
                Objects.equals(executionDate, that.executionDate) &&
                Objects.equals(token, that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(totalPrice, id, comment, customerFirstname, customerLastname, customerPhonenumber, customerId, executionDate, serviceType, paid, scheduled, token);
    }
}

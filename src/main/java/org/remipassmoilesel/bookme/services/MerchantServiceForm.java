package org.remipassmoilesel.bookme.services;

import org.remipassmoilesel.bookme.utils.Utils;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * Created by remipassmoilesel on 30/01/17.
 */
public class MerchantServiceForm {

    @NotNull
    private int price;

    @NotNull
    private long billId = -1;

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
    private long customerId = -1;

    @NotNull
    private String executionDate;

    @NotNull
    private String purchaseDate;

    @NotNull
    private long serviceId = -1;

    @NotNull
    private boolean paid;

    @NotNull
    private boolean scheduled;

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

        setPrice(bill.getPrice());
        setBillId(bill.getId());

        if (bill.getCustomer() != null) {
            setCustomerId(bill.getCustomer().getId());
            setCustomerFirstname(bill.getCustomer().getFirstname());
            setCustomerLastname(bill.getCustomer().getLastname());
            setCustomerPhonenumber(bill.getCustomer().getPhonenumber());
        }

        if (bill.getExecutionDate() != null) {
            setExecutionDate(Utils.dateToString(bill.getExecutionDate()));
        }

        if (bill.getPurchaseDate() != null) {
            setPurchaseDate(Utils.dateToString(bill.getPurchaseDate()));
        }

        if (bill.getService() != null) {
            setServiceId(bill.getService().getId());
        }

        setComment(bill.getComment());

    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public long getBillId() {
        return billId;
    }

    public void setBillId(long billId) {
        this.billId = billId;
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

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public long getServiceId() {
        return serviceId;
    }

    public void setServiceId(long serviceId) {
        this.serviceId = serviceId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MerchantServiceForm that = (MerchantServiceForm) o;
        return price == that.price &&
                billId == that.billId &&
                customerId == that.customerId &&
                serviceId == that.serviceId &&
                paid == that.paid &&
                scheduled == that.scheduled &&
                Objects.equals(comment, that.comment) &&
                Objects.equals(customerFirstname, that.customerFirstname) &&
                Objects.equals(customerLastname, that.customerLastname) &&
                Objects.equals(customerPhonenumber, that.customerPhonenumber) &&
                Objects.equals(executionDate, that.executionDate) &&
                Objects.equals(purchaseDate, that.purchaseDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price, billId, comment, customerFirstname, customerLastname, customerPhonenumber, customerId, executionDate, purchaseDate, serviceId, paid, scheduled);
    }

    @Override
    public String toString() {
        return "MerchantServiceBillForm{" +
                "price=" + price +
                ", billId=" + billId +
                ", comment='" + comment + '\'' +
                ", customerFirstname='" + customerFirstname + '\'' +
                ", customerLastname='" + customerLastname + '\'' +
                ", customerPhonenumber='" + customerPhonenumber + '\'' +
                ", customerId=" + customerId +
                ", executionDate='" + executionDate + '\'' +
                ", purchaseDate='" + purchaseDate + '\'' +
                ", serviceId=" + serviceId +
                ", paid=" + paid +
                ", scheduled=" + scheduled +
                '}';
    }
}

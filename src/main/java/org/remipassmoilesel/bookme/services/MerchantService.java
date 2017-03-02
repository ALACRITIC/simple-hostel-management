package org.remipassmoilesel.bookme.services;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.remipassmoilesel.bookme.customers.Customer;

import java.util.Date;
import java.util.Objects;

/**
 * Created by remipassmoilesel on 30/01/17.
 */
@DatabaseTable(tableName = "SERVICES")
public class MerchantService {

    public static final String ID_FIELD_NAME = "ID";
    public static final String SERVICE_FIELD_NAME = "SERVICE";
    public static final String PRICE_FIELD_NAME = "PRICE";
    public static final String COMMENT_FIELD_NAME = "COMMENT";
    public static final String PURCHASE_DATE_FIELD_NAME = "PURCHASE_DATE";
    public static final String IS_SCHEDULED_FIELD_NAME = "IS_SCHEDULED";
    public static final String EXECUTION_DATE_FIELD_NAME = "EXECUTION_DATE";
    public static final String CUSTOMER_FIELD_NAME = "CUSTOMER";
    public static final String PAID_FIELD_NAME = "PAID";

    @DatabaseField(generatedId = true, columnName = ID_FIELD_NAME)
    private long id;

    @DatabaseField(columnName = SERVICE_FIELD_NAME, foreign = true)
    private MerchantServiceType serviceType;

    @DatabaseField(columnName = PRICE_FIELD_NAME)
    private double totalPrice;

    @DatabaseField(columnName = COMMENT_FIELD_NAME, dataType = DataType.LONG_STRING)
    private String comment;

    // /!\ GMT time
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    @DatabaseField(columnName = PURCHASE_DATE_FIELD_NAME)
    private Date purchaseDate;

    @DatabaseField(columnName = IS_SCHEDULED_FIELD_NAME)
    private boolean scheduled;

    // /!\ GMT time
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    @DatabaseField(columnName = EXECUTION_DATE_FIELD_NAME)
    private Date executionDate;

    @DatabaseField(foreign = true, columnName = CUSTOMER_FIELD_NAME)
    private Customer customer;

    @DatabaseField(columnName = PAID_FIELD_NAME)
    private boolean paid;

    public MerchantService() {
        // ORMLite needs a no-arg constructor
    }

    public MerchantService(MerchantServiceType service, Customer customer, double totalPrice, String comment, Date purchaseDate, boolean scheduled, Date executionDate) {
        this.serviceType = service;
        this.totalPrice = totalPrice;
        this.comment = comment;
        this.purchaseDate = purchaseDate;
        this.scheduled = scheduled;
        this.executionDate = executionDate;
        this.customer = customer;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public MerchantServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(MerchantServiceType service) {
        this.serviceType = service;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public boolean isScheduled() {
        return scheduled;
    }

    public void setScheduled(boolean scheduled) {
        this.scheduled = scheduled;
    }

    public Date getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(Date executionDate) {
        this.executionDate = executionDate;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
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
        MerchantService that = (MerchantService) o;
        return id == that.id &&
                totalPrice == that.totalPrice &&
                scheduled == that.scheduled &&
                paid == that.paid &&
                Objects.equals(serviceType, that.serviceType) &&
                Objects.equals(comment, that.comment) &&
                Objects.equals(purchaseDate, that.purchaseDate) &&
                Objects.equals(executionDate, that.executionDate) &&
                Objects.equals(customer, that.customer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, serviceType, totalPrice, comment, purchaseDate, scheduled, executionDate, customer, paid);
    }

    @Override
    public String toString() {
        return "MerchantService{" +
                "id=" + id +
                ", serviceType=" + serviceType +
                ", price=" + totalPrice +
                ", comment='" + comment + '\'' +
                ", purchaseDate=" + purchaseDate +
                ", scheduled=" + scheduled +
                ", executionDate=" + executionDate +
                ", customer=" + customer +
                ", paid=" + paid +
                '}';
    }
}

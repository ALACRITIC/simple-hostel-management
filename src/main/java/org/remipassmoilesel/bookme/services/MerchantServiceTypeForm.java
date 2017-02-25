package org.remipassmoilesel.bookme.services;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * Created by remipassmoilesel on 30/01/17.
 */
public class MerchantServiceTypeForm {

    @NotNull
    private double price;

    @NotNull
    @Size(max = 2000)
    private String comment;

    @NotNull
    private String color;

    @NotNull
    private long id = -1;

    @NotNull
    @Size(min = 2, max = 50)
    private String name;

    @NotNull
    private Long token;

    public MerchantServiceTypeForm() {

    }

    /**
     * Load a service in form
     *
     * @param serviceType
     */
    public void load(MerchantServiceType serviceType) {

        if (serviceType == null) {
            return;
        }

        setPrice(serviceType.getPrice());
        setComment(serviceType.getComment());
        setColor(serviceType.getColor());
        setId(serviceType.getId());
        setName(serviceType.getName());

    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        MerchantServiceTypeForm that = (MerchantServiceTypeForm) o;
        return price == that.price &&
                id == that.id &&
                Objects.equals(comment, that.comment) &&
                Objects.equals(color, that.color) &&
                Objects.equals(name, that.name) &&
                Objects.equals(token, that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price, comment, color, id, name, token);
    }

    @Override
    public String toString() {
        return "MerchantServiceTypeForm{" +
                "price=" + price +
                ", comment='" + comment + '\'' +
                ", color='" + color + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", token=" + token +
                '}';
    }
}

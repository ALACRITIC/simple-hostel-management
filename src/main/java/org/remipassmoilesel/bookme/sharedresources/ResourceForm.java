package org.remipassmoilesel.bookme.sharedresources;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * Created by remipassmoilesel on 30/01/17.
 */
public class ResourceForm {

    @NotNull
    @Size(min = 2, max = 50)
    private String name;

    @NotNull
    @Min(1)
    private int places = 1;

    @NotNull
    @Size(max = 1000)
    private String comment;

    @NotNull
    private long id = -1;

    @NotNull
    private Type type;

    @NotNull
    private String color;

    @NotNull
    private double pricePerDay;

    @NotNull
    private Long token;

    public ResourceForm() {

    }

    /**
     * Load a room in form
     *
     * @param res
     */
    public void load(SharedResource res) {

        if (res == null) {
            return;
        }

        setName(res.getName());
        setPlaces(res.getPlaces());
        setComment(res.getComment());
        setId(res.getId());
        setType(res.getType());
        setColor(res.getColor());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getToken() {
        return token;
    }

    public void setToken(Long token) {
        this.token = token;
    }

    public int getPlaces() {
        return places;
    }

    public void setPlaces(int places) {
        this.places = places;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public double getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(double pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResourceForm that = (ResourceForm) o;
        return places == that.places &&
                id == that.id &&
                Double.compare(that.pricePerDay, pricePerDay) == 0 &&
                Objects.equals(name, that.name) &&
                Objects.equals(comment, that.comment) &&
                type == that.type &&
                Objects.equals(color, that.color) &&
                Objects.equals(token, that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, places, comment, id, type, color, pricePerDay, token);
    }

    @Override
    public String toString() {
        return "ResourceForm{" +
                "name='" + name + '\'' +
                ", places=" + places +
                ", comment='" + comment + '\'' +
                ", id=" + id +
                ", type=" + type +
                ", color='" + color + '\'' +
                ", pricePerDay=" + pricePerDay +
                ", token=" + token +
                '}';
    }
}

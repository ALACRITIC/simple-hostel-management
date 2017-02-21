package org.remipassmoilesel.bookme.sharedresources;


import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.remipassmoilesel.bookme.utils.Utils;

import java.awt.*;
import java.util.Objects;

/**
 * Created by remipassmoilesel on 30/01/17.
 */
@DatabaseTable(tableName = "SHARED_RESOURCES")
public class SharedResource {

    public static final String ID_FIELD_NAME = "ID";
    public static final String NAME_FIELD_NAME = "NAME";
    public static final String PLACES_FIELD_NAME = "PLACES";
    public static final String COMMENT_FIELD_NAME = "COMMENT";
    public static final String TYPE_FIELD_NAME = "TYPE";
    public static final String DELETED_FIELD_NAME = "DELETED";
    public static final String COLOR_FIELD_NAME = "COLOR";
    public static final String PRICE_PER_DAY_FIELD_NAME = "PRICE_PER_DAY";

    @DatabaseField(generatedId = true, columnName = ID_FIELD_NAME)
    private long id;

    @DatabaseField(columnName = NAME_FIELD_NAME, unique = true)
    private String name;

    @DatabaseField(columnName = PLACES_FIELD_NAME)
    private int places;

    @DatabaseField(columnName = TYPE_FIELD_NAME)
    private Type type;

    @DatabaseField(columnName = COMMENT_FIELD_NAME, dataType = DataType.LONG_STRING)
    private String comment;

    /**
     * If set to true, this resource hould not appear in availables resources.
     * <p>
     * Resources cannot be deleted, in order to keep database consistency.
     */
    @DatabaseField(columnName = DELETED_FIELD_NAME)
    private boolean deleted;

    @DatabaseField(columnName = COLOR_FIELD_NAME)
    private String color;

    @DatabaseField(columnName = PRICE_PER_DAY_FIELD_NAME)
    private double pricePerDay;

    public SharedResource() {
        // ORMLite needs a no-arg constructor
    }


    public SharedResource(String name, int places, double pricePerDay, String comment, Type type, Color color) {
        this.name = name;
        this.comment = comment;
        this.places = places;
        this.type = type;
        this.deleted = false;
        this.color = Utils.colorToRgbString(color);
        this.pricePerDay = pricePerDay;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getHexadecimalColor() {
        return Utils.colorToHex(color);
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getPlaces() {
        return places;
    }

    public void setPlaces(int places) {
        this.places = places;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
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
        SharedResource resource = (SharedResource) o;
        return id == resource.id &&
                places == resource.places &&
                deleted == resource.deleted &&
                pricePerDay == resource.pricePerDay &&
                Objects.equals(name, resource.name) &&
                type == resource.type &&
                Objects.equals(comment, resource.comment) &&
                Objects.equals(color, resource.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, places, type, comment, deleted, color, pricePerDay);
    }

    @Override
    public String toString() {
        return "SharedResource{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", places=" + places +
                ", type=" + type +
                ", comment='" + comment + '\'' +
                ", deleted=" + deleted +
                ", color='" + color + '\'' +
                ", pricePerDay=" + pricePerDay +
                '}';
    }
}

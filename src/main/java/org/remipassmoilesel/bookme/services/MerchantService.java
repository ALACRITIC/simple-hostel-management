package org.remipassmoilesel.bookme.services;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.remipassmoilesel.bookme.utils.Utils;

import java.awt.*;
import java.util.Objects;

/**
 * Created by remipassmoilesel on 30/01/17.
 */
@DatabaseTable(tableName = "SERVICE_TYPES")
public class MerchantService {

    public static final String ID_FIELD_NAME = "ID";
    public static final String SERVICE_NAME_FIELD_NAME = "NAME";
    public static final String PRICE_FIELD_NAME = "PRICE";
    public static final String COMMENT_FIELD_NAME = "COMMENT";
    public static final String COLOR_FIELD_NAME = "COLOR";

    @DatabaseField(generatedId = true, columnName = ID_FIELD_NAME)
    private long id;

    @DatabaseField(columnName = SERVICE_NAME_FIELD_NAME, unique = true)
    private String name;

    @DatabaseField(columnName = PRICE_FIELD_NAME)
    private int price;

    @DatabaseField(columnName = COMMENT_FIELD_NAME, dataType = DataType.LONG_STRING)
    private String comment;

    @DatabaseField(columnName = COLOR_FIELD_NAME)
    private String color;

    public MerchantService() {
        // ORMLite needs a no-arg constructor
    }

    public MerchantService(String name, int price, String comment, Color color) {
        this.name = name;
        this.price = price;
        this.comment = comment;
        this.color = Utils.colorToRgbString(color);
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MerchantService that = (MerchantService) o;
        return id == that.id &&
                price == that.price &&
                Objects.equals(name, that.name) &&
                Objects.equals(comment, that.comment) &&
                Objects.equals(color, that.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, comment, color);
    }

    @Override
    public String toString() {
        return "MerchantServiceType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", comment='" + comment + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}

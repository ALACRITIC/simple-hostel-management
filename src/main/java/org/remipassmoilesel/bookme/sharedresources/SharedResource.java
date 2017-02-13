package org.remipassmoilesel.bookme.sharedresources;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

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

    @DatabaseField(generatedId = true, columnName = ID_FIELD_NAME)
    private int id;

    @DatabaseField(columnName = NAME_FIELD_NAME)
    private String name;

    @DatabaseField(columnName = PLACES_FIELD_NAME)
    private int places;
    ;

    @DatabaseField(columnName = TYPE_FIELD_NAME)
    private Type type;

    @DatabaseField(columnName = COMMENT_FIELD_NAME)
    private String comment;

    public SharedResource() {
        // ORMLite needs a no-arg constructor
    }

    public SharedResource(String name, int places, String comment, Type type) {
        this.name = name;
        this.comment = comment;
        this.places = places;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    @Override
    public String toString() {
        return "SharedResource{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", places=" + places +
                ", type=" + type +
                ", comment='" + comment + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SharedResource that = (SharedResource) o;
        return id == that.id &&
                places == that.places &&
                Objects.equals(name, that.name) &&
                type == that.type &&
                Objects.equals(comment, that.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, places, type, comment);
    }


}

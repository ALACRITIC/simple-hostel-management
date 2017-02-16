package org.remipassmoilesel.bookme.sharedresources;


import com.j256.ormlite.field.DataType;
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
    public static final String DELETED_FIELD_NAME = "DELETED";

    @DatabaseField(generatedId = true, columnName = ID_FIELD_NAME)
    private long id;

    @DatabaseField(columnName = NAME_FIELD_NAME)
    private String name;

    @DatabaseField(columnName = PLACES_FIELD_NAME)
    private int places;

    @DatabaseField(columnName = TYPE_FIELD_NAME)
    private Type type;

    @DatabaseField(columnName = COMMENT_FIELD_NAME, dataType = DataType.LONG_STRING)
    private String comment;

    @DatabaseField(columnName = DELETED_FIELD_NAME)
    private boolean deleted;

    public SharedResource() {
        // ORMLite needs a no-arg constructor
    }


    public SharedResource(String name, int places, String comment, Type type) {
        this.name = name;
        this.comment = comment;
        this.places = places;
        this.type = type;
        this.deleted = false;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SharedResource resource = (SharedResource) o;
        return id == resource.id &&
                places == resource.places &&
                deleted == resource.deleted &&
                Objects.equals(name, resource.name) &&
                type == resource.type &&
                Objects.equals(comment, resource.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, places, type, comment, deleted);
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
                '}';
    }
}

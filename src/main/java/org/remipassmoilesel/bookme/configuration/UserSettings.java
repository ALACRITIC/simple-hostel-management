package org.remipassmoilesel.bookme.configuration;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Objects;

/**
 * Created by remipassmoilesel on 27/02/17.
 */

@DatabaseTable(tableName = "USER_SETTINGS")
public class UserSettings {

    public static final String HOSTEL_NAME = "HOSTEL_NAME";
    public static final String TRACKING_CODE = "TRACKING_CODE";

    public static final String ID_FIELD_NAME = "ID";
    public static final String KEY_FIELD_NAME = "KEY";
    public static final String VALUE_FIELD_NAME = "VALUE";

    @DatabaseField(generatedId = true, columnName = ID_FIELD_NAME)
    private long id;

    @DatabaseField(columnName = KEY_FIELD_NAME, unique = true)
    private String key;

    @DatabaseField(columnName = VALUE_FIELD_NAME, dataType = DataType.LONG_STRING)
    private String value;

    public UserSettings() {
    }

    public UserSettings(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserSettings that = (UserSettings) o;
        return id == that.id &&
                Objects.equals(key, that.key) &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, key, value);
    }

    @Override
    public String toString() {
        return "UserSettings{" +
                "id=" + id +
                ", key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}

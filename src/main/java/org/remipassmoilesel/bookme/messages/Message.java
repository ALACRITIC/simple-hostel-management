package org.remipassmoilesel.bookme.messages;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;
import java.util.Objects;

/**
 * Created by remipassmoilesel on 30/01/17.
 */
@DatabaseTable(tableName = "MESSAGES")
public class Message {

    public static final String ID_FIELD_NAME = "ID";
    public static final String DATE_FIELD_NAME = "DATE";
    public static final String MESSAGE_FIELD_NAME = "MESSAGE";

    @DatabaseField(generatedId = true, columnName = ID_FIELD_NAME)
    private long id;

    @DatabaseField(columnName = DATE_FIELD_NAME)
    private Date date;

    @DatabaseField(columnName = MESSAGE_FIELD_NAME)
    private String message;

    public Message() {
        // ORMLite needs a no-arg constructor
    }

    public Message(Date date, String message) {
        if (date == null) {
            date = new Date();
        }
        this.date = date;
        this.message = message;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", date=" + date +
                ", message='" + message + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message1 = (Message) o;
        return id == message1.id &&
                Objects.equals(date, message1.date) &&
                Objects.equals(message, message1.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, message);
    }
}

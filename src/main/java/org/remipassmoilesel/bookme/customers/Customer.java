package org.remipassmoilesel.bookme.customers;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;
import java.util.Objects;

/**
 * Created by remipassmoilesel on 30/01/17.
 */
@DatabaseTable(tableName = "CUSTOMERS")
public class Customer {

    public static final String ID_FIELD_NAME = "ID";
    public static final String LASTNAME_FIELD_NAME = "LASTNAME";
    public static final String FIRSTNAME_FIELD_NAME = "FIRSTNAME";
    public static final String PHONENUMBER_FIELD_NAME = "PHONENUMBER";
    public static final String CREATIONDATE_FIELD_NAME = "CREATIONDATE";

    @DatabaseField(generatedId = true, columnName = ID_FIELD_NAME)
    private long id;

    @DatabaseField(columnName = LASTNAME_FIELD_NAME)
    private String lastname;

    @DatabaseField(columnName = FIRSTNAME_FIELD_NAME)
    private String firstname;

    @DatabaseField(columnName = PHONENUMBER_FIELD_NAME)
    private String phonenumber;

    @DatabaseField(columnName = CREATIONDATE_FIELD_NAME)
    private Date creationDate;

    public Customer() {
        // ORMLite needs a no-arg constructor
    }

    public Customer(String firstname, String lastname, String phonenumber) {
        this(firstname, lastname, phonenumber, null);
    }

    public Customer(String firstname, String lastname, String phonenumber, Date creationDate) {
        this.lastname = lastname;
        this.firstname = firstname;
        this.phonenumber = phonenumber;

        if (creationDate == null) {
            creationDate = new Date();
        }
        this.creationDate = creationDate;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return id == customer.id &&
                Objects.equals(lastname, customer.lastname) &&
                Objects.equals(firstname, customer.firstname) &&
                Objects.equals(phonenumber, customer.phonenumber) &&
                Objects.equals(creationDate, customer.creationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, lastname, firstname, phonenumber, creationDate);
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", lastname='" + lastname + '\'' +
                ", firstname='" + firstname + '\'' +
                ", phonenumber='" + phonenumber + '\'' +
                ", creationDate=" + creationDate +
                '}';
    }
}

package org.remipassmoilesel.bookme.customers;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Objects;

/**
 * Created by remipassmoilesel on 30/01/17.
 */
@DatabaseTable(tableName = "CUSTOMERS")
public class Customer {

    private static final String ID_FIELD_NAME = "ID";
    private static final String LASTNAME_FIELD_NAME = "LASTNAME";
    private static final String FIRSTNAME_FIELD_NAME = "FIRSTNAME";
    private static final String PHONENUMBER_FIELD_NAME = "PHONENUMBER";

    @DatabaseField(generatedId = true, columnName = ID_FIELD_NAME)
    private long id;

    @DatabaseField(columnName = LASTNAME_FIELD_NAME)
    private String lastname;

    @DatabaseField(columnName = FIRSTNAME_FIELD_NAME)
    private String firstname;

    @DatabaseField(columnName = PHONENUMBER_FIELD_NAME)
    private String phonenumber;

    public Customer() {
        // ORMLite needs a no-arg constructor
    }

    public Customer(String firstname, String lastname, String phonenumber) {
        this.lastname = lastname;
        this.firstname = firstname;
        this.phonenumber = phonenumber;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return id == customer.id &&
                Objects.equals(lastname, customer.lastname) &&
                Objects.equals(firstname, customer.firstname) &&
                Objects.equals(phonenumber, customer.phonenumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, lastname, firstname, phonenumber);
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + lastname + '\'' +
                ", firstname='" + firstname + '\'' +
                ", phonenumber='" + phonenumber + '\'' +
                '}';
    }
}

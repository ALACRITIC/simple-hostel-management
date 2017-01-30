package org.remipassmoilesel.customers;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Objects;

/**
 * Created by remipassmoilesel on 30/01/17.
 */
@DatabaseTable(tableName = "CUSTOMERS")
public class Customer {

    private static final String ID_FIELD_NAME = "id";
    private static final String NAME_FIELD_NAME = "name";
    private static final String FIRSTNAME_FIELD_NAME = "firstname";
    private static final String PHONENUMBER_FIELD_NAME = "phonenumber";

    @DatabaseField(generatedId = true, columnName = ID_FIELD_NAME)
    private long id;

    @DatabaseField(columnName = NAME_FIELD_NAME)
    private String name;

    @DatabaseField(columnName = FIRSTNAME_FIELD_NAME)
    private String firstname;

    @DatabaseField(columnName = PHONENUMBER_FIELD_NAME)
    private String phonenumber;

    public Customer() {
        // ORMLite needs a no-arg constructor
    }

    public Customer(String firstname, String name, String phonenumber) {
        this.name = name;
        this.firstname = firstname;
        this.phonenumber = phonenumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
                Objects.equals(name, customer.name) &&
                Objects.equals(firstname, customer.firstname) &&
                Objects.equals(phonenumber, customer.phonenumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, firstname, phonenumber);
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", firstname='" + firstname + '\'' +
                ", phonenumber='" + phonenumber + '\'' +
                '}';
    }
}

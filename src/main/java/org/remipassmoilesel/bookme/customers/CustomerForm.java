package org.remipassmoilesel.bookme.customers;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * Created by remipassmoilesel on 30/01/17.
 */
public class CustomerForm {

    @NotNull
    @Size(min = 2, max = 50)
    private String firstname;

    @NotNull
    @Size(min = 2, max = 50)
    private String lastname;

    @NotNull
    @Size(min = 2, max = 50)
    @Pattern(regexp = "\\+?[0-9]+")
    private String phonenumber;

    @NotNull
    private long id = -1;

    @NotNull
    private Long token;

    public CustomerForm() {

    }

    /**
     * Load a customer in form
     *
     * @param customer
     */
    public void load(Customer customer) {

        if (customer == null) {
            return;
        }

        setFirstname(customer.getFirstname());
        setLastname(customer.getLastname());
        setId(customer.getId());
        setPhonenumber(customer.getPhonenumber());

    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getToken() {
        return token;
    }

    public void setToken(Long token) {
        this.token = token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerForm that = (CustomerForm) o;
        return id == that.id &&
                Objects.equals(firstname, that.firstname) &&
                Objects.equals(lastname, that.lastname) &&
                Objects.equals(phonenumber, that.phonenumber) &&
                Objects.equals(token, that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstname, lastname, phonenumber, id, token);
    }

    @Override
    public String toString() {
        return "CustomerForm{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", phonenumber='" + phonenumber + '\'' +
                ", id=" + id +
                ", token=" + token +
                '}';
    }
}

package org.remipassmoilesel.bookme.customers;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Created by remipassmoilesel on 17/02/17.
 */
public class SearchCustomerForm {

    @NotNull
    private String firstname;

    @NotNull
    private String lastname;

    public SearchCustomerForm() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SearchCustomerForm that = (SearchCustomerForm) o;
        return Objects.equals(firstname, that.firstname) &&
                Objects.equals(lastname, that.lastname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstname, lastname);
    }

    @Override
    public String toString() {
        return "SearchCustomerForm{" +
                "firstname='" + firstname + '\'' +
                ", name='" + lastname + '\'' +
                '}';
    }
}

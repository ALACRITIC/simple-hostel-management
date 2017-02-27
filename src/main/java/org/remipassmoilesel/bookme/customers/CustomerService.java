package org.remipassmoilesel.bookme.customers;

import com.j256.ormlite.stmt.QueryBuilder;
import org.remipassmoilesel.bookme.configuration.SpringConfiguration;
import org.remipassmoilesel.bookme.utils.AbstractDaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by remipassmoilesel on 30/01/17.
 */
@Service
public class CustomerService extends AbstractDaoService<Customer> {

    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    public CustomerService(SpringConfiguration configuration) {
        super(Customer.class, configuration);
    }

    /**
     * Create a customer
     *
     * @param firstname
     * @param lastname
     * @param phonenumber
     * @return
     * @throws IOException
     */
    public Customer create(String firstname, String lastname, String phonenumber) throws IOException {
        return create(new Customer(firstname, lastname, phonenumber));
    }

    /**
     * Get customers ordered by creation date desc
     *
     * @param limit
     * @param offset
     * @return
     * @throws IOException
     */
    public List<Customer> getLasts(long limit, long offset) throws IOException {
        try {
            QueryBuilder<Customer, String> builder = dao.queryBuilder();
            builder.orderBy(Customer.CREATIONDATE_FIELD_NAME, true);
            builder.limit(limit);
            if (offset > 0) {
                builder.offset(offset);
            }

            return builder.query();
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    public Customer getByPhonenumber(String phonenumber) throws IOException {

        try {
            QueryBuilder queryBuilder = dao.queryBuilder();
            queryBuilder.where().eq(Customer.PHONENUMBER_FIELD_NAME, phonenumber);

            List<Customer> result = queryBuilder.query();
            if (result.size() < 1) {
                return null;
            } else {
                return result.get(0);
            }
        } catch (SQLException e) {
            throw new IOException(e);
        }

    }

    public List<Customer> search(String firstname, String lastname, String phonenumber, long limit, long offset) throws IOException {

        if ((firstname == null || firstname.isEmpty())
                && (lastname == null || lastname.isEmpty())
                && (phonenumber == null || phonenumber.isEmpty())) {
            throw new NullPointerException("Name or first name must be not null and not empty: n/" + lastname + ", fn/" + firstname);
        }

        String rawWhere = "";

        // prepare arguments
        // to lowercase, and replace all special chars by wildcards
        lastname = lastname.trim().toLowerCase().replaceAll("[^a-z0-9]", "_");
        firstname = firstname.trim().toLowerCase().replaceAll("[^a-z0-9]", "_");
        phonenumber = phonenumber.trim().toLowerCase().replaceAll("[^a-z0-9]", "_");

        // check first name
        if (firstname.matches("^_*$") == false && firstname.isEmpty() == false) {
            rawWhere += "LOWER(" + Customer.FIRSTNAME_FIELD_NAME + ") LIKE '%" + firstname + "%'";
        }

        // check last name
        if (lastname.matches("^_*$") == false && lastname.isEmpty() == false) {
            if (rawWhere.isEmpty() == false) {
                rawWhere += " AND ";
            }
            rawWhere += "LOWER(" + Customer.LASTNAME_FIELD_NAME + ") LIKE '%" + lastname + "%'";
        }

        // check phone number
        if (phonenumber.matches("^_*$") == false && phonenumber.isEmpty() == false) {
            if (rawWhere.isEmpty() == false) {
                rawWhere += " AND ";
            }
            rawWhere += "LOWER(" + Customer.PHONENUMBER_FIELD_NAME + ") LIKE '%" + phonenumber + "%'";
        }

        // do not query if arguments are not significant
        if (rawWhere.isEmpty()) {
            throw new IOException("Terms of research are not significant");
        }

        try {

            QueryBuilder builder = dao.queryBuilder();
            builder.where().raw(rawWhere);
            builder.limit(limit);
            builder.offset(offset);

            return builder.query();

        } catch (Exception e) {
            throw new IOException(e);
        }

    }

}

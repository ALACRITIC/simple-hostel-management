package org.remipassmoilesel.bookme.customers;

import com.j256.ormlite.stmt.QueryBuilder;
import org.remipassmoilesel.bookme.configuration.CustomConfiguration;
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
public class CustomerService extends AbstractDaoService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    public CustomerService(CustomConfiguration configuration) {
        super(Customer.class, configuration);
    }

    /**
     * Get a customer by ID
     *
     * @param id
     * @return
     * @throws IOException
     */
    public Customer getById(Long id) throws IOException {
        try {
            return (Customer) dao.queryForId(String.valueOf(id));
        } catch (SQLException e) {
            throw new IOException(e);
        }
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
    public Customer createCustomer(String firstname, String lastname, String phonenumber) throws IOException {
        return createCustomer(new Customer(firstname, lastname, phonenumber));
    }

    /**
     * Create a customer
     *
     * @param customer
     * @return
     * @throws IOException
     */
    public Customer createCustomer(Customer customer) throws IOException {

        try {
            dao.create(customer);
            return customer;
        } catch (SQLException e) {
            throw new IOException(e);
        }

    }

    /**
     * Refresh information of a customer object from database
     *
     * @param customer
     * @throws IOException
     */
    public void refresh(Customer customer) throws IOException {
        try {
            dao.refresh(customer);
        } catch (SQLException e) {
            throw new IOException(e);
        }

    }

    public List<Customer> getAll() throws IOException {
        try {
            return dao.queryForAll();
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

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


}

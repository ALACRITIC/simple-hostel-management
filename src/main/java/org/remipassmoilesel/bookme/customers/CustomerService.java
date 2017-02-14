package org.remipassmoilesel.bookme.customers;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.table.TableUtils;
import org.remipassmoilesel.bookme.MainConfiguration;
import org.remipassmoilesel.bookme.utils.DatabaseUtils;
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
public class CustomerService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);
    private Dao<Customer, String> customerDao;
    private JdbcPooledConnectionSource connection;

    public CustomerService() {

        try {
            connection = DatabaseUtils.getH2OrmliteConnectionPool(MainConfiguration.DATABASE_PATH);

            TableUtils.createTableIfNotExists(connection, Customer.class);

            // instantiate the dao
            customerDao = DaoManager.createDao(connection, Customer.class);

        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Close connection on finalize
     *
     * @throws Throwable
     */
    @Override
    protected void finalize() throws Throwable {
        super.finalize();

        if (connection != null) {
            connection.close();
        }
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
            return customerDao.queryForId(String.valueOf(id));
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
            customerDao.create(customer);
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
            customerDao.refresh(customer);
        } catch (SQLException e) {
            throw new IOException(e);
        }

    }

    public List<Customer> getAll() throws IOException {
        try {
            return customerDao.queryForAll();
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    public List<Customer> getLasts(long limit, long offset) throws IOException {
        try {
            QueryBuilder<Customer, String> builder = customerDao.queryBuilder();
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

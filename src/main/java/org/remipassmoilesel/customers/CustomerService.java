package org.remipassmoilesel.customers;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.table.TableUtils;
import org.remipassmoilesel.MainConfiguration;
import org.remipassmoilesel.utils.DatabaseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by remipassmoilesel on 30/01/17.
 */
@Service
public class CustomerService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);
    private Dao<Customer, String> customerDao;
    private JdbcPooledConnectionSource connection;

    public CustomerService() {

        System.out.println("CustomerService");
        System.out.println("CustomerService");
        System.out.println("CustomerService");
        System.out.println("CustomerService");
        System.out.println("CustomerService");
        System.out.println("CustomerService");
        System.out.println("CustomerService");
        System.out.println("CustomerService");
        System.out.println("CustomerService");

        try {
            connection = DatabaseUtils.getH2OrmliteConnectionPool(MainConfiguration.DATABASE_PATH);

            TableUtils.createTableIfNotExists(connection, Customer.class);

            // instantiate the dao
            customerDao = DaoManager.createDao(connection, Customer.class);

        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();

        if (connection != null) {
            connection.close();
        }
    }

    public Customer getById(Long id) throws IOException {
        try {
            return customerDao.queryForId(String.valueOf(id));
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    public Customer createClient(String firstname, String lastname, String phonenumber) throws IOException {

        try {
            Customer customer = new Customer(firstname, lastname, phonenumber);
            customerDao.create(customer);
            return customer;
        } catch (SQLException e) {
            throw new IOException(e);
        }

    }
}

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

import java.sql.SQLException;

/**
 * Created by remipassmoilesel on 30/01/17.
 */
@Service
public class CustomerService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);
    private Dao<Customer, String> customerDao;
    private JdbcPooledConnectionSource connection;

    public CustomerService() throws SQLException {

        connection = DatabaseUtils.getH2OrmliteConnectionPool(MainConfiguration.DATABASE_PATH);

        // instantiate the dao
        customerDao = DaoManager.createDao(connection, Customer.class);

        TableUtils.createTableIfNotExists(connection, Customer.class);

    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();

        if (connection != null) {
            try {
                connection.close();
            } catch (Exception e) {
                logger.error("Error while closing connection source", e);
            }
        }
    }

    public Customer getById(Long id) {
        try {
            return customerDao.queryForId(String.valueOf(id));
        } catch (SQLException e) {
            logger.error("Error while retrieving: " + id, e);
            return null;
        }
    }

}

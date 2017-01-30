package org.remipassmoilesel.reservations;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import org.remipassmoilesel.customers.Configuration;
import org.remipassmoilesel.utils.DatabaseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

/**
 * Created by remipassmoilesel on 30/01/17.
 */

@Service
public class ReservationService {

    private static final Logger logger = LoggerFactory.getLogger(ReservationService.class);
    private Dao<Reservation, String> reservationDao;
    private JdbcPooledConnectionSource connection;

    public ReservationService() throws SQLException {

        connection = DatabaseUtils.getH2OrmliteConnectionPool(Configuration.DATABASE_PATH);

        // instantiate the dao
        reservationDao = DaoManager.createDao(connection, Reservation.class);

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

    public Reservation getById(Long id) {
        try {
            return reservationDao.queryForId(String.valueOf(id));
        } catch (SQLException e) {
            logger.error("Error while retrieving: " + id, e);
            return null;
        }
    }

    public void getAll() {

        System.out.println("Get all !");

        /*
        // instantiate the dao
        Dao<Account, String> accountDao =
                DaoManager.createDao(connectionSource, Account.class);

        // if you need to create the 'accounts' table make this call
        TableUtils.createTableIfNotExists(connectionSource, Account.class);

        // create an instance of Account
        Account account = new Account();
        account.setName("Jim Coakley");

        // persist the account object to the database
        accountDao.create(account);

        account.setPassword("azerty");
        accountDao.update(account);

        Account account3 = new Account("heyhey", "hoho");
        accountDao.create(account3);
        accountDao.delete(account);


        // dao are iterators
        for (Account a : accountDao) {
            if (account.getName().equals("Bob Smith")) {
                // you can't return, break, or throw from here
//                return a;
            }
        }


        // retrieve the account from the database by its id field (name)
        List<Account> account2 = accountDao.queryForAll();
        System.out.println("Accounts: " + account2);

        // close the connection source
        connectionSource.close();

        */

    }

}

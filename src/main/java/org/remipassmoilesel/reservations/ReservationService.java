package org.remipassmoilesel.reservations;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.table.TableUtils;
import org.remipassmoilesel.MainConfiguration;
import org.remipassmoilesel.customers.Customer;
import org.remipassmoilesel.utils.DatabaseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Created by remipassmoilesel on 30/01/17.
 */

@Service
public class ReservationService {

    private static final Logger logger = LoggerFactory.getLogger(ReservationService.class);
    private Dao<Reservation, String> reservationDao;
    private JdbcPooledConnectionSource connection;

    public ReservationService() {

        System.out.println("ReservationService");
        System.out.println("ReservationService");
        System.out.println("ReservationService");
        System.out.println("ReservationService");
        System.out.println("ReservationService");
        System.out.println("ReservationService");
        System.out.println("ReservationService");
        System.out.println("ReservationService");
        System.out.println("ReservationService");
        System.out.println("ReservationService");

        try {

            connection = DatabaseUtils.getH2OrmliteConnectionPool(MainConfiguration.DATABASE_PATH);

            TableUtils.createTableIfNotExists(connection, Reservation.class);

            // instantiate the dao
            reservationDao = DaoManager.createDao(connection, Reservation.class);

        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }

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

    public List<Reservation> getLasts(int i) throws SQLException {

        QueryBuilder<Reservation, String> statement = reservationDao.queryBuilder().orderBy(Reservation.RESERVATION_DATE, false);
        List<Reservation> results = statement.query();

        return results;
    }

    public Reservation createReservation(Customer customer, Date departure, Date arrival) throws IOException {
        try {
            Reservation res = new Reservation(customer, departure, arrival, null);
            reservationDao.create(res);
            return res;
        } catch (SQLException e) {
            throw new IOException(e);
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

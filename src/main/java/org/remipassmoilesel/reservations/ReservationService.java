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

    public List<Reservation> getLasts(long limit, long offset) throws SQLException {

        QueryBuilder<Reservation, String> statement = reservationDao.queryBuilder().orderBy(Reservation.RESERVATION_DATE, false).limit(limit).offset(offset);
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

}

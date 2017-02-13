package org.remipassmoilesel.bookingsystem.reservations;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.table.TableUtils;
import org.remipassmoilesel.bookingsystem.MainConfiguration;
import org.remipassmoilesel.bookingsystem.customers.Customer;
import org.remipassmoilesel.bookingsystem.customers.CustomerService;
import org.remipassmoilesel.bookingsystem.sharedresources.SharedResource;
import org.remipassmoilesel.bookingsystem.sharedresources.SharedResourceService;
import org.remipassmoilesel.bookingsystem.sharedresources.Type;
import org.remipassmoilesel.bookingsystem.utils.DatabaseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by remipassmoilesel on 30/01/17.
 */

@Service
public class ReservationService {

    private static final Logger logger = LoggerFactory.getLogger(ReservationService.class);

    @Autowired
    private CustomerService customerService;

    @Autowired
    private SharedResourceService sharedResourceService;

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

    /**
     * Close connection on finalization
     *
     * @throws Throwable
     */
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

    /**
     * Get a reservation by its id
     *
     * @param id
     * @return
     */
    public Reservation getById(Long id) {
        try {
            return reservationDao.queryForId(String.valueOf(id));
        } catch (SQLException e) {
            logger.error("Error while retrieving: " + id, e);
            return null;
        }
    }

    /**
     * Get last reservations stored
     *
     * @param limit
     * @param offset
     * @return
     * @throws IOException
     */
    public List<Reservation> getLasts(long limit, long offset) throws IOException {

        try {
            QueryBuilder<Reservation, String> statement = reservationDao.queryBuilder()
                    .orderBy(Reservation.RESERVATION_DATE, false).limit(limit).offset(offset);
            List<Reservation> results = statement.query();

            for (Reservation r : results) {
                customerService.refresh(r.getCustomer());
                sharedResourceService.refresh(r.getResource());
            }

            return results;
        } catch (Exception e) {
            throw new IOException(e);
        }

    }

    /**
     * Create a new reservation
     *
     * @param customer
     * @param departure
     * @param arrival
     * @return
     * @throws IOException
     */
    public Reservation createReservation(Customer customer, SharedResource resource, Date departure, Date arrival) throws IOException {
        try {
            Reservation res = new Reservation(customer, resource, departure, arrival, null);
            reservationDao.create(res);
            return res;
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    /**
     * Return a list of reservation wbetween specified
     *
     * @param beginDate
     * @param endDate
     * @param orderAscending
     * @return
     * @throws IOException
     */
    public List<Reservation> getByInterval(Date beginDate, Date endDate, boolean orderAscending) throws IOException {

        try {
            QueryBuilder<Reservation, String> queryBuilder = reservationDao.queryBuilder();
            queryBuilder.orderBy(Reservation.DATEBEGIN_FIELD_NAME, orderAscending);
            Where<Reservation, String> where = queryBuilder.where();

            where.between(Reservation.DATEBEGIN_FIELD_NAME, beginDate, endDate)
                    .or().between(Reservation.DATEEND_FIELD_NAME, beginDate, endDate);

            List<Reservation> results = queryBuilder.query();

            for (Reservation r : results) {
                customerService.refresh(r.getCustomer());
                sharedResourceService.refresh(r.getResource());
            }

            return results;
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    /**
     * Return a list of shared resources available
     *
     * @param type
     * @param begin
     * @param end
     * @return
     * @throws Exception
     */
    public List<SharedResource> getAvailableResources(Type type, Date begin, Date end) throws Exception {

        ArrayList<SharedResource> result = new ArrayList<>();
        for (SharedResource res : sharedResourceService.getAll(type)) {

            QueryBuilder<Reservation, String> builder = reservationDao.queryBuilder();

            Where<Reservation, String> where = builder.where();
            where.and(
                    where.eq(Reservation.SHARED_RESOURCE, res.getId()),
                    where.or(
                            // case 1: reservation begin or end in interval
                            where.between(Reservation.DATEBEGIN_FIELD_NAME, begin, end)
                                    .or().between(Reservation.DATEEND_FIELD_NAME, begin, end),

                            // case 2: interval is in reservation
                            where.lt(Reservation.DATEBEGIN_FIELD_NAME, begin)
                                    .and().gt(Reservation.DATEEND_FIELD_NAME, end)
                    )
            );

            int reservationNumber = builder.query().size();

            if (reservationNumber < res.getPlaces()) {
                result.add(res);
            }

        }

        return result;
    }

}

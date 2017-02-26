package org.remipassmoilesel.bookme.reservations;

import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import org.joda.time.DateTime;
import org.remipassmoilesel.bookme.accommodations.Accommodation;
import org.remipassmoilesel.bookme.accommodations.AccommodationService;
import org.remipassmoilesel.bookme.accommodations.Type;
import org.remipassmoilesel.bookme.configuration.CustomConfiguration;
import org.remipassmoilesel.bookme.customers.Customer;
import org.remipassmoilesel.bookme.customers.CustomerService;
import org.remipassmoilesel.bookme.utils.AbstractDaoService;
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
public class ReservationService extends AbstractDaoService<Reservation> {

    private static final Logger logger = LoggerFactory.getLogger(ReservationService.class);

    @Autowired
    private CustomerService customerService;

    @Autowired
    private AccommodationService accommodationService;

    public ReservationService(CustomConfiguration configuration) {
        super(Reservation.class, configuration);
    }

    /**
     * Get last reservations stored
     *
     * @param limit
     * @param offset
     * @return
     * @throws IOException
     */
    public List<Reservation> getLastReservations(long limit, long offset) throws IOException {

        try {
            QueryBuilder<Reservation, String> statement = dao.queryBuilder()
                    .orderBy(Reservation.RESERVATION_DATE, false).limit(limit).offset(offset);
            List<Reservation> results = statement.query();

            for (Reservation r : results) {
                refresh(r);
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
     * @param begin
     * @param end
     * @return
     * @throws IOException
     */
    public Reservation create(Customer customer, Accommodation accommodation, int places, Date begin, Date end) throws IOException {
        return create(new Reservation(customer, accommodation, places, begin, end, null));
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
        return getByInterval(beginDate, endDate, orderAscending, -1l);
    }

    public List<Reservation> getByInterval(Date beginDate, Date endDate, boolean orderAscending, Long accommodationId) throws IOException {

        try {
            QueryBuilder<Reservation, String> queryBuilder = dao.queryBuilder();
            queryBuilder.orderBy(Reservation.DATEBEGIN_FIELD_NAME, orderAscending);
            Where<Reservation, String> where = queryBuilder.where();

            if (accommodationId != -1) {
                where.and(
                        where.eq(Reservation.ACCOMMODATION_FIELD_NAME, accommodationId),
                        where.between(Reservation.DATEBEGIN_FIELD_NAME, beginDate, endDate)
                                .or().between(Reservation.DATEEND_FIELD_NAME, beginDate, endDate)
                );
            } else {
                where.between(Reservation.DATEBEGIN_FIELD_NAME, beginDate, endDate)
                        .or().between(Reservation.DATEEND_FIELD_NAME, beginDate, endDate);
            }

            List<Reservation> results = queryBuilder.query();

            for (Reservation r : results) {
                refresh(r);
            }

            return results;
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    /**
     * Return a list of shared resources available for specified period and specified places number
     *
     * @param type
     * @param begin
     * @param end
     * @return
     * @throws Exception
     */
    public List<Accommodation> getAvailableAccommodations(Type type, Date begin, Date end, int places) throws IOException {

        try {
            ArrayList<Accommodation> freeRessources = new ArrayList<>();
            for (Accommodation accommodation : accommodationService.getAll(type)) {

                QueryBuilder<Reservation, String> builder = dao.queryBuilder();

                Where<Reservation, String> where = builder.where();
                where.and(
                        where.eq(Reservation.ACCOMMODATION_FIELD_NAME, accommodation.getId()),
                        where.or(
                                // case 1: reservation begin or end in specified interval (SI)
                                where.or(
                                        where.between(Reservation.DATEBEGIN_FIELD_NAME, begin, end),
                                        where.between(Reservation.DATEEND_FIELD_NAME, begin, end)
                                ),

                                // case 2: reservation equal SI
                                // case 3: reservation contains SI
                                where.and(
                                        where.le(Reservation.DATEBEGIN_FIELD_NAME, begin),
                                        where.ge(Reservation.DATEEND_FIELD_NAME, end)
                                )
                        )
                );

                List<Reservation> reservations = builder.query();

                // no reservations, resource is free
                if (reservations.size() < 1 && places <= accommodation.getPlaces()) {
                    freeRessources.add(accommodation);
                }

                // some reservations, count places
                // TODO: create a raw statement for sum() instead of this ?
                else {
                    int reservedPlaces = 0;
                    for (Reservation reservation : reservations) {
                        reservedPlaces += reservation.getPlaces();
                    }

                    if (accommodation.getPlaces() - reservedPlaces >= places) {
                        freeRessources.add(accommodation);
                    }
                }

            }

            return freeRessources;
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    /**
     * Get future checkouts
     *
     * @param limit
     * @param offset
     * @return
     * @throws IOException
     */
    public List<Reservation> getNextCheckouts(long limit, long offset) throws IOException {

        try {
            QueryBuilder<Reservation, String> builder = dao.queryBuilder();

            DateTime now = new DateTime();
            DateTime start = new DateTime(now.getYear(), now.getMonthOfYear(), now.getDayOfMonth(), 0, 0).minusHours(2);
            DateTime future = start.plusYears(1);

            Where<Reservation, String> where = builder.where();
            where.between(Reservation.DATEEND_FIELD_NAME, start.toDate(), future.toDate());

            builder.orderBy(Reservation.DATEEND_FIELD_NAME, true);
            builder.limit(limit);
            if (offset > 0) {
                builder.offset(offset);
            }

            List<Reservation> results = builder.query();
            refresh(results);

            return results;
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    public List<Reservation> getByAccommodationId(Long accommodationId, long limit, long offset) throws IOException {

        try {
            QueryBuilder queryBuilder = dao.queryBuilder();
            queryBuilder.where().eq(Reservation.ACCOMMODATION_FIELD_NAME, accommodationId);
            queryBuilder.orderBy(Reservation.DATEBEGIN_FIELD_NAME, true);
            queryBuilder.limit(limit);
            queryBuilder.offset(offset);

            List results = queryBuilder.query();
            refresh(results);

            return results;
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    public List<Reservation> getByCustomerId(Long customerId, Boolean paid, long limit, long offset) throws IOException {
        try {
            QueryBuilder queryBuilder = dao.queryBuilder();
            Where where = queryBuilder.where();

            // search for paid or non-paid only
            if (paid != null) {
                where.and(
                        where.eq(Reservation.CUSTOMER_FIELD_NAME, customerId),
                        where.eq(Reservation.PAID_FIELD_NAME, paid)
                );
            }

            // search by customer id
            else {
                where.eq(Reservation.CUSTOMER_FIELD_NAME, customerId);
            }

            queryBuilder.orderBy(Reservation.DATEBEGIN_FIELD_NAME, true);
            queryBuilder.limit(limit);
            queryBuilder.offset(offset);

            List results = queryBuilder.query();
            refresh(results);

            return results;
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    public void refresh(Reservation res) throws IOException {
        super.refresh(res);

        if (res == null) {
            logger.error("Cannot refresh null object: " + res, new NullPointerException("Cannot refresh null object: " + res));
            return;
        }

        customerService.refresh(res.getCustomer());
        accommodationService.refresh(res.getAccommodation());
    }

    @Override
    public Reservation getById(Long id) throws IOException {
        Reservation res = super.getById(id);
        refresh(res);
        return res;
    }

    @Override
    public List<Reservation> getAll() throws IOException {
        List<Reservation> result = super.getAll();
        refresh(result);
        return result;
    }


}

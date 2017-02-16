package org.remipassmoilesel.bookme.reservations;

import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import org.joda.time.DateTime;
import org.remipassmoilesel.bookme.configuration.CustomConfiguration;
import org.remipassmoilesel.bookme.customers.Customer;
import org.remipassmoilesel.bookme.customers.CustomerService;
import org.remipassmoilesel.bookme.sharedresources.SharedResource;
import org.remipassmoilesel.bookme.sharedresources.SharedResourceService;
import org.remipassmoilesel.bookme.sharedresources.Type;
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
    private CustomConfiguration configuration;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private SharedResourceService sharedResourceService;

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
    public Reservation create(Customer customer, SharedResource resource, int places, Date begin, Date end) throws IOException {
        return create(new Reservation(customer, resource, places, begin, end, null));
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
            QueryBuilder<Reservation, String> queryBuilder = dao.queryBuilder();
            queryBuilder.orderBy(Reservation.DATEBEGIN_FIELD_NAME, orderAscending);
            Where<Reservation, String> where = queryBuilder.where();

            where.between(Reservation.DATEBEGIN_FIELD_NAME, beginDate, endDate)
                    .or().between(Reservation.DATEEND_FIELD_NAME, beginDate, endDate);

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
    public List<SharedResource> getAvailableResources(Type type, Date begin, Date end, int places) throws IOException {

        try {
            ArrayList<SharedResource> freeRessources = new ArrayList<>();
            for (SharedResource resource : sharedResourceService.getAll(type)) {

                QueryBuilder<Reservation, String> builder = dao.queryBuilder();

                Where<Reservation, String> where = builder.where();
                where.and(
                        where.eq(Reservation.SHARED_RESOURCE_FIELD_NAME, resource.getId()),
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
                if (reservations.size() < 1 && places <= resource.getPlaces()) {
                    freeRessources.add(resource);
                }

                // some reservations, count places
                // TODO: create a raw statement for sum() instead of this ?
                else {
                    int reservedPlaces = 0;
                    for (Reservation reservation : reservations) {
                        reservedPlaces += reservation.getPlaces();
                    }

                    if (resource.getPlaces() - reservedPlaces >= places) {
                        freeRessources.add(resource);
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

    public List<Reservation> getByResourceId(Long resourceId, long limit, long offset) throws IOException {

        try {
            QueryBuilder queryBuilder = dao.queryBuilder();
            queryBuilder.where().eq(Reservation.SHARED_RESOURCE_FIELD_NAME, resourceId);
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
        customerService.refresh(res.getCustomer());
        sharedResourceService.refresh(res.getResource());
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

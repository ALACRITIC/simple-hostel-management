package org.remipassmoilesel.bookme.services;

import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import org.remipassmoilesel.bookme.configuration.SpringConfiguration;
import org.remipassmoilesel.bookme.customers.CustomerService;
import org.remipassmoilesel.bookme.utils.AbstractDaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Created by remipassmoilesel on 19/02/17.
 */
@Service
public class MerchantServiceService extends AbstractDaoService<MerchantService> {

    private final Logger logger = LoggerFactory.getLogger(MerchantServiceService.class);

    @Autowired
    private CustomerService customerService;

    @Autowired
    private MerchantServiceTypesService merchantServiceTypesService;

    public MerchantServiceService(SpringConfiguration configuration) {
        super(MerchantService.class, configuration);
    }

    @Override
    public MerchantService getById(Long id) throws IOException {
        MerchantService result = super.getById(id);
        refresh(result);
        return result;
    }

    @Override
    public void refresh(MerchantService service) throws IOException {
        super.refresh(service);

        if (service == null) {
            logger.error("Cannot refresh null object: " + service, new NullPointerException("Cannot refresh null object: " + service));
            return;
        }

        customerService.refresh(service.getCustomer());
        merchantServiceTypesService.refresh(service.getServiceType());

    }

    @Override
    public List<MerchantService> getAll(Long limit, Long offset) throws IOException {
        List<MerchantService> result = super.getAll(limit, offset);
        refresh(result);
        return result;
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
    public List<MerchantService> getScheduledServicesByInterval(Date beginDate, Date endDate, boolean orderAscending) throws IOException {

        if (beginDate.getTime() >= endDate.getTime()) {
            throw new IllegalArgumentException("Begin date must be lesser than " +
                    "end date: b/" + beginDate + " e/" + endDate);
        }

        try {

            QueryBuilder<MerchantService, String> queryBuilder = dao.queryBuilder();
            queryBuilder.orderBy(MerchantService.EXECUTION_DATE_FIELD_NAME, orderAscending);
            Where<MerchantService, String> where = queryBuilder.where();

            where.and(
                    where.eq(MerchantService.IS_SCHEDULED_FIELD_NAME, true),
                    where.between(MerchantService.EXECUTION_DATE_FIELD_NAME, beginDate, endDate)
                    /*,
                    where.or(

                            where.eq(MerchantService.EXECUTION_DATE_FIELD_NAME, beginDate),
                            where.eq(MerchantService.EXECUTION_DATE_FIELD_NAME, endDate)
                    )*/
            );

            List<MerchantService> results = queryBuilder.query();

            refresh(results);

            return results;
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    public List<MerchantService> getByCustomerId(Long customerId, Boolean paid, boolean orderAscending) throws IOException {

        try {

            QueryBuilder<MerchantService, String> queryBuilder = dao.queryBuilder();
            queryBuilder.orderBy(MerchantService.PURCHASE_DATE_FIELD_NAME, orderAscending);
            Where<MerchantService, String> where = queryBuilder.where();

            // search for paid or non-paid only
            if (paid != null) {
                where.and(
                        where.eq(MerchantService.CUSTOMER_FIELD_NAME, customerId),
                        where.eq(MerchantService.PAID_FIELD_NAME, paid)
                );
            }

            // search by customer id
            else {
                where.eq(MerchantService.CUSTOMER_FIELD_NAME, customerId);
            }

            List<MerchantService> results = queryBuilder.query();
            refresh(results);

            return results;
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    public List<MerchantService> getByInterval(Date begin, Date end, boolean orderAscending) throws IOException {

        try {
            QueryBuilder<MerchantService, String> queryBuilder = dao.queryBuilder();
            queryBuilder.orderBy(MerchantService.PURCHASE_DATE_FIELD_NAME, orderAscending);
            Where<MerchantService, String> where = queryBuilder.where();
            where.between(MerchantService.PURCHASE_DATE_FIELD_NAME, begin, end);

            List<MerchantService> results = queryBuilder.query();

            for (MerchantService r : results) {
                refresh(r);
            }

            return results;
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    public List<MerchantService> getLastScheduledServices(long limit, long offset) throws IOException {

        try {
            QueryBuilder<MerchantService, String> statement = dao.queryBuilder();

            statement.orderBy(MerchantService.EXECUTION_DATE_FIELD_NAME, false)
                    .limit(limit).offset(offset);
            statement.where().eq(MerchantService.IS_SCHEDULED_FIELD_NAME, true);

            List<MerchantService> results = statement.query();
            refresh(results);

            return results;
        } catch (Exception e) {
            throw new IOException(e);
        }
    }
}

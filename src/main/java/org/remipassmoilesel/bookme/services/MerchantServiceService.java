package org.remipassmoilesel.bookme.services;

import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import org.remipassmoilesel.bookme.configuration.CustomConfiguration;
import org.remipassmoilesel.bookme.customers.CustomerService;
import org.remipassmoilesel.bookme.utils.AbstractDaoService;
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

    @Autowired
    private CustomerService customerService;

    @Autowired
    private MerchantServiceTypesService merchantServiceTypesService;

    public MerchantServiceService(CustomConfiguration configuration) {
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
        customerService.refresh(service.getCustomer());
        merchantServiceTypesService.refresh(service.getServiceType());
    }

    @Override
    public List<MerchantService> getAll() throws IOException {
        List<MerchantService> result = super.getAll();
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
    public List<MerchantService> getScheduledServiceByInterval(Date beginDate, Date endDate, boolean orderAscending) throws IOException {

        try {
            QueryBuilder<MerchantService, String> queryBuilder = dao.queryBuilder();
            queryBuilder.orderBy(MerchantService.EXECUTION_DATE_FIELD_NAME, orderAscending);
            Where<MerchantService, String> where = queryBuilder.where();

            where.between(MerchantService.EXECUTION_DATE_FIELD_NAME, beginDate, endDate)
                    .or().between(MerchantService.EXECUTION_DATE_FIELD_NAME, beginDate, endDate);

            List<MerchantService> results = queryBuilder.query();

            refresh(results);

            return results;
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

}

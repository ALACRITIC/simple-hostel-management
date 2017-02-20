package org.remipassmoilesel.bookme.services;

import org.remipassmoilesel.bookme.configuration.CustomConfiguration;
import org.remipassmoilesel.bookme.customers.CustomerService;
import org.remipassmoilesel.bookme.utils.AbstractDaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
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

}

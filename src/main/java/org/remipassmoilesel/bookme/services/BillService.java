package org.remipassmoilesel.bookme.services;

import org.remipassmoilesel.bookme.configuration.CustomConfiguration;
import org.remipassmoilesel.bookme.utils.AbstractDaoService;
import org.springframework.stereotype.Service;

/**
 * Created by remipassmoilesel on 19/02/17.
 */
@Service
public class BillService extends AbstractDaoService<MerchantServiceBill> {


    public BillService(CustomConfiguration configuration) {
        super(MerchantServiceBill.class, configuration);
    }


}

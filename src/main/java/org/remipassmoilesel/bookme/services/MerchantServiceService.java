package org.remipassmoilesel.bookme.services;

import org.remipassmoilesel.bookme.configuration.CustomConfiguration;
import org.remipassmoilesel.bookme.utils.AbstractDaoService;
import org.springframework.stereotype.Service;

/**
 * Created by remipassmoilesel on 19/02/17.
 */
@Service
public class MerchantServiceService extends AbstractDaoService<MerchantService> {

    public MerchantServiceService(CustomConfiguration configuration) {
        super(MerchantService.class, configuration);
    }

}

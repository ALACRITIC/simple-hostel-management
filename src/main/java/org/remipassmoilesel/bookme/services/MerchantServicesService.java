package org.remipassmoilesel.bookme.services;

import org.remipassmoilesel.bookme.configuration.CustomConfiguration;
import org.remipassmoilesel.bookme.utils.AbstractDaoService;
import org.remipassmoilesel.bookme.utils.colors.DefaultColors;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by remipassmoilesel on 19/02/17.
 */
@Service
public class MerchantServicesService extends AbstractDaoService<MerchantService> {

    public MerchantServicesService(CustomConfiguration configuration) throws SQLException {
        super(MerchantService.class, configuration);

        // create resources if resource table is empty
        if (dao.queryForAll().size() < 1) {

            List<String> names = Arrays.asList(
                    "Pressing",
                    "Sauna",
                    "Meal",
                    "Drink");

            Color darkslategray = DefaultColors.get("darkslategray").getColor();
            Color darkviolet = DefaultColors.get("darkviolet").getColor();
            for (int i = 1; i < names.size(); i++) {
                Color color = i % 2 == 0 ? darkslategray : darkviolet;
                dao.create(new MerchantService(names.get(i), i + 3, "", color));
            }

        }

    }

}

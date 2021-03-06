package org.remipassmoilesel.bookme.services;

import com.j256.ormlite.stmt.QueryBuilder;
import org.remipassmoilesel.bookme.accommodations.Accommodation;
import org.remipassmoilesel.bookme.configuration.SpringConfiguration;
import org.remipassmoilesel.bookme.utils.AbstractDaoService;
import org.remipassmoilesel.bookme.utils.colors.DefaultColors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by remipassmoilesel on 19/02/17.
 */
@Service
public class MerchantServiceTypesService extends AbstractDaoService<MerchantServiceType> {

    private static final Logger logger = LoggerFactory.getLogger(MerchantServiceTypesService.class);

    public MerchantServiceTypesService(SpringConfiguration configuration) throws SQLException {
        super(MerchantServiceType.class, configuration);

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
                dao.create(new MerchantServiceType(names.get(i), i + 3, "", color));
            }

        }

    }

    public List<MerchantServiceType> getAll() throws IOException {
        return getAll(-1l, -1l, false);
    }

    @Override
    public List<MerchantServiceType> getAll(Long limit, Long offset) throws IOException {
        return getAll(limit, offset, false);
    }

    public List<MerchantServiceType> getAll(Long limit, Long offset, boolean withDeletedEntities) throws IOException {

        // return all with deleted
        if (withDeletedEntities) {
            return super.getAll(limit, offset);
        }

        // return all but deleted
        else {
            try {
                QueryBuilder builder = dao.queryBuilder();
                if (limit != -1l) {
                    builder.limit(limit);
                }
                if (offset != -1l) {
                    builder.offset(offset);
                }
                builder.where().eq(MerchantServiceType.DELETED_FIELD_NAME, false);
                return builder.query();
            } catch (SQLException e) {
                throw new IOException(e);
            }
        }
    }

    /**
     * Service are not deleted, just marked as delete
     *
     * @param res
     * @throws IOException
     */
    public void markAsDeleted(Accommodation res) throws IOException {
        res.setDeleted(true);
        markAsDeleted(res.getId());
    }

    /**
     * Services are not deleted, just marked as delete
     *
     * @param id
     * @throws IOException
     */
    public void markAsDeleted(Long id) throws IOException {
        try {
            MerchantServiceType res = (MerchantServiceType) dao.queryForId(id);

            if (res == null) {
                logger.warn("No accommodation found: " + id);
            }

            res.setDeleted(true);
            dao.update(res);

        } catch (SQLException e) {
            throw new IOException(e);
        }
    }


    public MerchantServiceType getByName(String name) throws IOException {

        try {
            QueryBuilder queryBuilder = dao.queryBuilder();
            queryBuilder.where().eq(MerchantServiceType.SERVICE_NAME_FIELD_NAME, name);

            List<MerchantServiceType> result = queryBuilder.query();
            if (result.size() < 1) {
                return null;
            } else {
                return result.get(0);
            }
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

}

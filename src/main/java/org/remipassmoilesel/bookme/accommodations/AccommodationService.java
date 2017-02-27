package org.remipassmoilesel.bookme.accommodations;

import com.j256.ormlite.stmt.QueryBuilder;
import org.remipassmoilesel.bookme.configuration.SpringConfiguration;
import org.remipassmoilesel.bookme.utils.AbstractDaoService;
import org.remipassmoilesel.bookme.utils.colors.DefaultColors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by remipassmoilesel on 11/02/17.
 */
@Service
public class AccommodationService extends AbstractDaoService<Accommodation> {

    private static final Logger logger = LoggerFactory.getLogger(AccommodationService.class);

    public AccommodationService(SpringConfiguration configuration) throws SQLException {
        super(Accommodation.class, configuration);

        // create resources if resource table is empty
        if (dao.queryForAll().size() < 1) {

            Color cadetblue = DefaultColors.get("cadetblue").getColor();
            Color blueviolet = DefaultColors.get("blueviolet").getColor();
            for (int i = 1; i < 4; i++) {
                dao.create(new Accommodation("Room " + i, 2, 2.5d, "", Type.ROOM, cadetblue));
            }

            for (int i = 1; i < 4; i++) {
                dao.create(new Accommodation("Bed " + i, 1, 2.5d, "", Type.BED, blueviolet));
            }

        }
    }

    public Accommodation createAccommodation(String roomName, int places, double pricePerDay, String roomComment, Type type, Color color) throws IOException {
        return create(new Accommodation(roomName, places, pricePerDay, roomComment, type, color));
    }

    /**
     * This method do not return deleted resources
     *
     * @param type
     * @return
     * @throws IOException
     */
    public List<Accommodation> getAll(Type type) throws IOException {

        if (type == null) {
            throw new NullPointerException("Type is null");
        }

        try {
            QueryBuilder builder = dao.queryBuilder();
            builder.where().eq(Accommodation.TYPE_FIELD_NAME, type);
            builder.where().eq(Accommodation.DELETED_FIELD_NAME, false);
            return builder.query();
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    public List<Accommodation> getAll() throws IOException {
        return getAll(-1l, -1l, false);
    }

    @Override
    public List<Accommodation> getAll(Long limit, Long offset) throws IOException {
        return getAll(limit, offset, false);
    }

    public List<Accommodation> getAll(Long limit, Long offset, boolean withDeletedEntities) throws IOException {

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
                builder.where().eq(Accommodation.DELETED_FIELD_NAME, false);
                return builder.query();
            } catch (SQLException e) {
                throw new IOException(e);
            }
        }
    }

    /**
     * Resources are not deleted, just marked as delete
     *
     * @param res
     * @throws IOException
     */
    public void markAsDeleted(Accommodation res) throws IOException {
        res.setDeleted(true);
        markAsDeleted(res.getId());
    }

    /**
     * Resources are not deleted, just marked as delete
     *
     * @param id
     * @throws IOException
     */
    public void markAsDeleted(Long id) throws IOException {
        try {
            Accommodation res = (Accommodation) dao.queryForId(id);

            if (res == null) {
                logger.warn("No accommodation found: " + id);
            }

            res.setDeleted(true);
            dao.update(res);

        } catch (SQLException e) {
            throw new IOException(e);
        }
    }


}

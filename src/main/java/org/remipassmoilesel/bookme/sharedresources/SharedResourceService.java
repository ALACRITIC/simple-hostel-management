package org.remipassmoilesel.bookme.sharedresources;

import com.j256.ormlite.stmt.QueryBuilder;
import org.remipassmoilesel.bookme.configuration.CustomConfiguration;
import org.remipassmoilesel.bookme.utils.AbstractDaoService;
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
public class SharedResourceService extends AbstractDaoService<SharedResource> {

    private static final Logger logger = LoggerFactory.getLogger(SharedResourceService.class);

    public SharedResourceService(CustomConfiguration configuration) throws SQLException {
        super(SharedResource.class, configuration);

        // create resources if resource table is empty
        if (dao.queryForAll().size() < 1) {

            for (int i = 1; i < 4; i++) {
                dao.create(new SharedResource("Room " + i, 2, "", Type.ROOM, Color.blue));
            }

            for (int i = 1; i < 4; i++) {
                dao.create(new SharedResource("Bed " + i, 1, "", Type.BED, Color.green));
            }

        }
    }

    public SharedResource createResource(String roomName, int places, String roomComment, Type type, Color color) throws IOException {
        return create(new SharedResource(roomName, places, roomComment, type, color));
    }

    /**
     * This method do not return deleted resources
     *
     * @param type
     * @return
     * @throws IOException
     */
    public List<SharedResource> getAll(Type type) throws IOException {

        if (type == null) {
            throw new NullPointerException("Type is null");
        }

        try {
            QueryBuilder builder = dao.queryBuilder();
            builder.where().eq(SharedResource.TYPE_FIELD_NAME, type);
            builder.where().eq(SharedResource.DELETED_FIELD_NAME, false);
            return builder.query();
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    @Override
    public List<SharedResource> getAll() throws IOException {
        return getAll(false);
    }

    public List<SharedResource> getAll(boolean withDeletedEntities) throws IOException {

        // return all with deleted
        if (withDeletedEntities) {
            return super.getAll();
        }

        // return all but deleted
        else {
            try {
                QueryBuilder builder = dao.queryBuilder();
                builder.where().eq(SharedResource.DELETED_FIELD_NAME, false);
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
    public void markAsDeleted(SharedResource res) throws IOException {
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
            SharedResource res = (SharedResource) dao.queryForId(id);

            if (res == null) {
                logger.warn("No resource found: " + id);
            }

            res.setDeleted(true);
            dao.update(res);

        } catch (SQLException e) {
            throw new IOException(e);
        }
    }


}

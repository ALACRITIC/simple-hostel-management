package org.remipassmoilesel.bookme.sharedresources;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.table.TableUtils;
import org.remipassmoilesel.bookme.MainConfiguration;
import org.remipassmoilesel.bookme.utils.DatabaseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by remipassmoilesel on 11/02/17.
 */
@Service
public class SharedResourceService {

    private static final Logger logger = LoggerFactory.getLogger(SharedResourceService.class);

    private Dao<SharedResource, String> resourceDao;
    private JdbcPooledConnectionSource connection;

    public SharedResourceService() {

        try {

            connection = DatabaseUtils.getH2OrmliteConnectionPool(MainConfiguration.DATABASE_PATH);

            TableUtils.createTableIfNotExists(connection, SharedResource.class);

            // instantiate the dao
            resourceDao = DaoManager.createDao(connection, SharedResource.class);

        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }

    }

    /**
     * Close connection on finalization
     *
     * @throws Throwable
     */
    @Override
    protected void finalize() throws Throwable {
        super.finalize();

        if (connection != null) {
            try {
                connection.close();
            } catch (Exception e) {
                logger.error("Error while closing connection source", e);
            }
        }
    }

    /**
     * Get a reservation by its id
     *
     * @param id
     * @return
     */
    public SharedResource getById(Long id) {
        try {
            return resourceDao.queryForId(String.valueOf(id));
        } catch (SQLException e) {
            logger.error("Error while retrieving: " + id, e);
            return null;
        }
    }

    /**
     * Create a new reservation
     *
     * @return
     * @throws IOException
     */
    public SharedResource createResource(SharedResource room) throws IOException {
        try {
            resourceDao.create(room);
            return room;
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    public List<SharedResource> getAll(Type type) throws IOException {
        try {
            if (type != null) {
                return resourceDao.queryForEq(SharedResource.TYPE_FIELD_NAME, type);
            } else {
                return resourceDao.queryForAll();
            }
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    public SharedResource createRoom(String roomName, int places, String roomComment, Type type) throws IOException {
        return createResource(new SharedResource(roomName, places, roomComment, type));
    }

    /**
     * Refresh information of a customer object from database
     *
     * @param resource
     * @throws IOException
     */
    public void refresh(SharedResource resource) throws IOException {
        try {
            resourceDao.refresh(resource);
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

}

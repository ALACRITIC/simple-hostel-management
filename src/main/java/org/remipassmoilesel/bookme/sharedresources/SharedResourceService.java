package org.remipassmoilesel.bookme.sharedresources;

import org.remipassmoilesel.bookme.utils.AbstractDaoService;
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
public class SharedResourceService extends AbstractDaoService {

    private static final Logger logger = LoggerFactory.getLogger(SharedResourceService.class);

    public SharedResourceService() {
        super(SharedResource.class);
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
            return (SharedResource) dao.queryForId(String.valueOf(id));
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
            dao.create(room);
            return room;
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    public List<SharedResource> getAll(Type type) throws IOException {
        try {
            if (type != null) {
                return dao.queryForEq(SharedResource.TYPE_FIELD_NAME, type);
            } else {
                return dao.queryForAll();
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
            dao.refresh(resource);
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

}

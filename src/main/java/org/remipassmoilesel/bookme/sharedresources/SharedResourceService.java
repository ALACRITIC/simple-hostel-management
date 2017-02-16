package org.remipassmoilesel.bookme.sharedresources;

import org.remipassmoilesel.bookme.configuration.CustomConfiguration;
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
public class SharedResourceService extends AbstractDaoService<SharedResource> {

    private static final Logger logger = LoggerFactory.getLogger(SharedResourceService.class);

    public SharedResourceService(CustomConfiguration configuration) {
        super(SharedResource.class, configuration);
    }

    public SharedResource createRoom(String roomName, int places, String roomComment, Type type) throws IOException {
        return create(new SharedResource(roomName, places, roomComment, type));
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

}

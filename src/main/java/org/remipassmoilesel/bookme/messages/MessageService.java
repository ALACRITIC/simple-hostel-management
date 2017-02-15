package org.remipassmoilesel.bookme.messages;

import com.j256.ormlite.stmt.QueryBuilder;
import org.remipassmoilesel.bookme.configuration.CustomConfiguration;
import org.remipassmoilesel.bookme.utils.AbstractDaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by remipassmoilesel on 30/01/17.
 */
@Service
public class MessageService extends AbstractDaoService {

    private static final Logger logger = LoggerFactory.getLogger(MessageService.class);

    public MessageService(CustomConfiguration configuration) {
        super(Message.class, configuration);
    }

    /**
     * Get a message by its id
     *
     * @param id
     * @return
     */
    public Message getById(Long id) throws IOException {
        try {
            return (Message) dao.queryForId(String.valueOf(id));
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    /**
     * Get last messages stored
     *
     * @param limit
     * @param offset
     * @return
     * @throws IOException
     */
    public List<Message> getLasts(long limit, long offset) throws IOException {

        try {
            QueryBuilder<Object, String> statement = dao.queryBuilder()
                    .orderBy(Message.DATE_FIELD_NAME, false).limit(limit).offset(offset);
            List<Message> results = (List<Message>)(List<?>) statement.query();

            return results;
        } catch (Exception e) {
            throw new IOException(e);
        }

    }

    public List<Message> getAll() throws IOException {

        try {
            return dao.queryForAll();
        } catch (Exception e) {
            throw new IOException(e);
        }

    }

    /**
     * Create a new message
     *
     * @param message
     * @return
     * @throws IOException
     */
    public Message createMessage(Message message) throws IOException {
        try {
            dao.create(message);
            return message;
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    /**
     * Create a new message
     *
     * @param message
     * @return
     * @throws IOException
     */
    public Message createMessage(String message) throws IOException {
        return createMessage(new Message(null, message));
    }

}

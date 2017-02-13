package org.remipassmoilesel.bookme.messages;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.stmt.QueryBuilder;
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
 * Created by remipassmoilesel on 30/01/17.
 */
@Service
public class MessageService {

    private static final Logger logger = LoggerFactory.getLogger(MessageService.class);

    private Dao<Message, String> messageDao;
    private JdbcPooledConnectionSource connection;

    public MessageService() {

        try {

            connection = DatabaseUtils.getH2OrmliteConnectionPool(MainConfiguration.DATABASE_PATH);

            TableUtils.createTableIfNotExists(connection, Message.class);

            // instantiate the dao
            messageDao = DaoManager.createDao(connection, Message.class);

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
     * Get a message by its id
     *
     * @param id
     * @return
     */
    public Message getById(Long id) throws IOException {
        try {
            return messageDao.queryForId(String.valueOf(id));
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
            QueryBuilder<Message, String> statement = messageDao.queryBuilder()
                    .orderBy(Message.DATE_FIELD_NAME, false).limit(limit).offset(offset);
            List<Message> results = statement.query();

            return results;
        } catch (Exception e) {
            throw new IOException(e);
        }

    }

    public List<Message> getAll() throws IOException {

        try {
            return messageDao.queryForAll();
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
            messageDao.create(message);
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

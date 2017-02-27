package org.remipassmoilesel.bookme.messages;

import com.j256.ormlite.stmt.QueryBuilder;
import org.remipassmoilesel.bookme.configuration.SpringConfiguration;
import org.remipassmoilesel.bookme.utils.AbstractDaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * Created by remipassmoilesel on 30/01/17.
 */
@Service
public class MessageService extends AbstractDaoService<Message> {

    private static final Logger logger = LoggerFactory.getLogger(MessageService.class);

    public MessageService(SpringConfiguration configuration) {
        super(Message.class, configuration);
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
            List<Message> results = (List<Message>) (List<?>) statement.query();

            return results;
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
    public Message create(String message) throws IOException {
        return create(new Message(null, message));
    }

}

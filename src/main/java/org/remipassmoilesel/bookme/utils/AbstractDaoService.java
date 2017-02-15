package org.remipassmoilesel.bookme.utils;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.table.TableUtils;
import org.remipassmoilesel.bookme.configuration.CustomConfiguration;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by remipassmoilesel on 14/02/17.
 */
public abstract class AbstractDaoService {

    private final Class clazz;
    protected Dao dao;
    protected JdbcPooledConnectionSource connection;

    public AbstractDaoService(Class clazz, CustomConfiguration configuration) {

        try {
            this.clazz = clazz;

            // TODO: let user create it's own database
            connection = DatabaseUtils.getH2OrmliteConnectionPool(configuration.getDatabasePath(),
                    // please do not laugh :)
                    CustomConfiguration.DB_LOGIN, CustomConfiguration.DB_PASSWORD);

            TableUtils.createTableIfNotExists(connection, clazz);

            // instantiate the dao
            dao = DaoManager.createDao(connection, clazz);

        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        dispose();
    }

    @PreDestroy
    public void dispose() throws IOException {
        if (connection != null) {
            connection.close();
        }
    }

    public void clearAllRows() throws IOException {
        try {
            TableUtils.clearTable(connection, clazz);
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

}

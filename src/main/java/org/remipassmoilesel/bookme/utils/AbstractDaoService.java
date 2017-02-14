package org.remipassmoilesel.bookme.utils;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.table.TableUtils;
import org.remipassmoilesel.bookme.CustomConfiguration;

import java.sql.SQLException;

/**
 * Created by remipassmoilesel on 14/02/17.
 */
public abstract class AbstractDaoService {

    protected Dao dao;
    protected JdbcPooledConnectionSource connection;

    public AbstractDaoService(Class clazz, CustomConfiguration configuration) {

        try {

            connection = DatabaseUtils.getH2OrmliteConnectionPool(configuration.getDatabasePath());

            TableUtils.createTableIfNotExists(connection, clazz);

            // instantiate the dao
            dao = DaoManager.createDao(connection, clazz);

        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}

package org.remipassmoilesel.bookme.utils;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.table.TableUtils;
import org.remipassmoilesel.bookme.configuration.CustomConfiguration;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by remipassmoilesel on 14/02/17.
 */
public abstract class AbstractDaoService<T> {

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

    /**
     * Clear all entities in this repository
     *
     * @throws IOException
     */
    public void clearAllEntities() throws IOException {
        try {
            TableUtils.clearTable(connection, clazz);
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    /**
     * Delete entity corresponding to specified id
     *
     * @param reservationId
     * @throws IOException
     */
    public void deleteById(Long reservationId) throws IOException {
        try {
            dao.deleteById(reservationId);
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    /**
     * Return object corresponding to specified id or null
     *
     * @param id
     * @return
     * @throws IOException
     */
    public T getById(Long id) throws IOException {
        try {
            return (T) dao.queryForId(id);
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    /**
     * Create an entity
     *
     * @param toCreate
     * @return
     * @throws IOException
     */
    public T create(T toCreate) throws IOException {
        try {
            dao.create(toCreate);
            return toCreate;
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    /**
     * Refresh information of a customer object from database
     *
     * @param obj
     * @throws IOException
     */
    public void refresh(T obj) throws IOException {
        try {
            dao.refresh(obj);
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    /**
     * Return a list of all entities
     *
     * @return
     * @throws IOException
     */
    public List<T> getAll() throws IOException {
        try {
            return dao.queryForAll();
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

}

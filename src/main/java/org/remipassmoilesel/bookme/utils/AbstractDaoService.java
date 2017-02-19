package org.remipassmoilesel.bookme.utils;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.table.TableUtils;
import org.remipassmoilesel.bookme.configuration.CustomConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by remipassmoilesel on 14/02/17.
 */
public abstract class AbstractDaoService<T> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractDaoService.class);

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
     * @param objectId
     * @throws IOException
     */
    public void deleteById(Long objectId) throws IOException {
        try {
            dao.deleteById(objectId);
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

        if (obj == null) {
            logger.error("Cannot refresh null object: " + obj, new NullPointerException("Cannot refresh null object: " + obj));
            return;
        }

        try {
            dao.refresh(obj);
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    /**
     * Refresh information of a customer object from database
     *
     * @param listObj
     * @throws IOException
     */
    public void refresh(List<T> listObj) throws IOException {

        if (listObj == null) {
            throw new NullPointerException("Cannot refresh null object");
        }

        for (T o : listObj) {
            refresh(o);
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

    /**
     * Update specified entity in database
     *
     * @param obj
     * @return
     * @throws IOException
     */
    public int update(T obj) throws IOException {
        try {
            return dao.update(obj);
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

}

package org.remipassmoilesel.utils;

import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import org.h2.jdbcx.JdbcConnectionPool;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by remipassmoilesel on 30/01/17.
 */
public class DatabaseUtils {

    private static HashMap<String, JdbcConnectionPool> currentH2Pool;

    public static JdbcPooledConnectionSource getH2OrmliteConnectionPool(Path database) throws SQLException {

        String databaseUrl = getJdbcUrlForH2(database);

        JdbcPooledConnectionSource connectionSource = new JdbcPooledConnectionSource(databaseUrl);
        connectionSource.setMaxConnectionAgeMillis(Long.MAX_VALUE);
        connectionSource.setCheckConnectionsEveryMillis(-1);
        connectionSource.setTestBeforeGet(false);

        connectionSource.initialize();

        return connectionSource;
    }

    public static String getJdbcUrlForH2(Path databasePath) {
        // TODO: close database on VM exit ?
        // return "jdbc:h2:file:" + databasePath.toAbsolutePath().toString() + ";DB_CLOSE_ON_EXIT=FALSE";
        return "jdbc:h2:file:" + databasePath.toAbsolutePath().toString();
    }


    public static Connection getH2Connection(Path databasePath) throws SQLException {
        if (currentH2Pool == null) {
            currentH2Pool = new HashMap<>();
        }

        String key = databasePath.toAbsolutePath().toString();
        if (currentH2Pool.get(key) == null) {
            currentH2Pool.put(key, JdbcConnectionPool.create(getJdbcUrlForH2(databasePath), "", ""));
        }

        return currentH2Pool.get(key).getConnection();
    }
}

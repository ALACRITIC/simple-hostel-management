package org.remipassmoilesel.bookme.utils;

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

    public static JdbcPooledConnectionSource getH2OrmliteConnectionPool(Path database, String user, String psswd) throws SQLException {

        String databaseUrl = getH2JdbcUrlFor(database);

        JdbcPooledConnectionSource connectionSource = new JdbcPooledConnectionSource(databaseUrl);
        connectionSource.setMaxConnectionAgeMillis(Long.MAX_VALUE);
        connectionSource.setCheckConnectionsEveryMillis(-1);
        connectionSource.setTestBeforeGet(false);

        if(user != null && psswd != null){
            connectionSource.setUsername(user);
            connectionSource.setPassword(psswd);
        }

        connectionSource.initialize();

        return connectionSource;
    }

    public static String getH2JdbcUrlFor(Path databasePath) {
        // DB_CLOSE_ON_EXIT=FALSE recommended by https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-sql.html
        return "jdbc:h2:file:" + databasePath.toAbsolutePath().toString() + ";DB_CLOSE_ON_EXIT=FALSE";
    }

    public static Connection getH2Connection(Path databasePath) throws SQLException {

        if (currentH2Pool == null) {
            currentH2Pool = new HashMap<>();
        }

        String key = databasePath.toAbsolutePath().toString();
        if (currentH2Pool.get(key) == null) {
            currentH2Pool.put(key, JdbcConnectionPool.create(getH2JdbcUrlFor(databasePath), "", ""));
        }

        return currentH2Pool.get(key).getConnection();
    }
}

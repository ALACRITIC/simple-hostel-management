package org.remipassmoilesel.bookme.credentials;

import com.j256.ormlite.stmt.QueryBuilder;
import org.remipassmoilesel.bookme.configuration.CustomConfiguration;
import org.remipassmoilesel.bookme.utils.AbstractDaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by remipassmoilesel on 15/02/17.
 */
@Service
public class UserService extends AbstractDaoService<User> {

    private static final String HASH_SALT = "$2a$10$ceil.owlq1EG0IP8f0QZeO";

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(CustomConfiguration configuration) throws Exception {
        super(User.class, configuration);

        // create default user if needed
        if (dao.queryForAll().size() < 1) {
            create(CustomConfiguration.DEFAULT_USER, CustomConfiguration.DEFAULT_PASSWORD, Role.ADMIN);
        }

    }

    /**
     * Create a user with specified CLEAR password.
     *
     * @param username
     * @param clearPassword
     * @param role
     * @return
     * @throws IOException
     */
    public User create(String username, String clearPassword, Role role) throws IOException {

        username = username.trim().toLowerCase();

        String hashedPassword = BCrypt.hashpw(clearPassword, HASH_SALT);
        User cred = new User(username, hashedPassword, role);
        try {
            dao.create(cred);
            return cred;
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    /**
     * Return a user with specified username or null if nothing is found
     *
     * @param username
     * @return
     * @throws IOException
     */
    public User getByUsername(String username) throws IOException {

        try {

            username = username.trim().toLowerCase();

            QueryBuilder builder = dao.queryBuilder();
            builder.where().eq(User.USERNAME_FIELD_NAME, username);
            return (User) builder.queryForFirst();
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    /**
     * Return a credential object if username and password are valid and existing, or null if nothing is found
     *
     * @param username
     * @param password
     * @return
     * @throws IOException
     */
    public User testCredentials(String username, String password) throws IOException {

        User cred = getByUsername(username);

        // credentials are valid
        if (cred != null && testPassword(cred, password) == true) {
            return cred;
        }

        // credentials are invalid
        else {
            return null;
        }

    }

    /**
     * Return true if specified password is valid
     *
     * @param cred
     * @param clearPassword
     * @return
     */
    public boolean testPassword(User cred, String clearPassword) {

        if (clearPassword == null) {
            throw new NullPointerException("Clear password is null");
        }

        if (cred == null) {
            throw new NullPointerException("Credentials are null");
        }

        String testPassword = BCrypt.hashpw(clearPassword, HASH_SALT);
        return testPassword.equals(cred.getPassword());
    }

}

package org.remipassmoilesel.bookme.security;

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
public class CredentialService extends AbstractDaoService {

    private static final String HASH_SALT = "$2a$10$ceil.owlq1EG0IP8f0QZeO";

    private static final Logger logger = LoggerFactory.getLogger(CredentialService.class);

    public CredentialService(CustomConfiguration configuration) throws Exception {
        super(Credential.class, configuration);

        // create default user if needed
        if (dao.queryForAll().size() < 1) {
            createUser(CustomConfiguration.DEFAULT_USER, CustomConfiguration.DEFAULT_PASSWORD, Credential.ADMIN_ROLE);
        }

    }

    private Credential createUser(String username, String clearPassword, String role) throws IOException {
        String hashedPassword = BCrypt.hashpw(clearPassword, HASH_SALT);
        Credential cred = new Credential(username, hashedPassword, role);
        try {
            dao.create(cred);
            return cred;
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    public Credential getByUsername(String username) throws IOException {

        try {
            QueryBuilder builder = dao.queryBuilder();
            builder.where().eq(Credential.USERNAME_FIELD_NAME, username);
            return (Credential) builder.queryForFirst();
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    public Credential testCredentials(String username, String password) throws IOException {

        Credential cred = getByUsername(username);

        // credentials are valid
        if (cred != null && testPassword(cred, password) == true) {
            return cred;
        }

        // credentials are invalid
        else {
            return null;
        }

    }

    public boolean testPassword(Credential cred, String clearPassword) {

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

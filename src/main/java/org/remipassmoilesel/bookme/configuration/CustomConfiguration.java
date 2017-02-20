package org.remipassmoilesel.bookme.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;
import java.awt.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * Created by remipassmoilesel on 12/12/16.
 */
@Configuration
public class CustomConfiguration {

    public static final String TEST_PROFILE = "TEST_PROFILE";
    public static final String DEV_PROFILE = "DEV_PROFILE";

    public static final Path PROD_DATABASE_DIRECTORY = Paths.get("database");
    public static final Path PROD_DATABASE_PATH = PROD_DATABASE_DIRECTORY.resolve("db.h2");

    public static final Path TEST_DATABASE_DIRECTORY = Paths.get("database-test");
    public static final Path TEST_DATABASE_PATH = TEST_DATABASE_DIRECTORY.resolve("db.h2");

    // TODO: use user credentials
    // please do not laugh :)
    public static final String DB_LOGIN = "i03KvGVpQIwja-nxr5gq7I1oiOErdbCS";
    public static final String DB_PASSWORD = "hcNEfW0zcDoY0yuM50aCgXRbY-rRSxiX";

    public static final String DEFAULT_USER = "admin";
    public static final String DEFAULT_PASSWORD = "admin";

    public static final Path TEMP_DIRECTORY = Paths.get("./tmp");


    @Resource
    private Environment environment;

    /**
     * Return database path. Database path can change if we are in tests or production environment
     *
     * @return
     */
    public Path getDatabasePath() {

        List<String> activeProfiles = Arrays.asList(environment.getActiveProfiles());
        if (activeProfiles.contains(TEST_PROFILE)) {
            return TEST_DATABASE_PATH;
        } else {
            return PROD_DATABASE_PATH;
        }

    }

    /**
     * Return true if browser should be start on launch
     *
     * @return
     */
    public boolean isLaunchBrowserOnStart() {
        return isDevProfileEnabled() == false && GraphicsEnvironment.isHeadless() == false;
    }

    /**
     * Return true if dev profile is enable
     *
     * @return
     */
    public boolean isDevProfileEnabled() {
        List<String> activeProfiles = Arrays.asList(environment.getActiveProfiles());
        return activeProfiles.contains(DEV_PROFILE);
    }

}

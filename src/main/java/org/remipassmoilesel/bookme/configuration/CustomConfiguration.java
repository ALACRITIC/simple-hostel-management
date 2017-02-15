package org.remipassmoilesel.bookme.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import javax.annotation.Resource;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

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

    @Resource
    private Environment environment;

    public Path getDatabasePath() {

        List<String> activeProfiles = Arrays.asList(environment.getActiveProfiles());
        if (activeProfiles.contains(TEST_PROFILE)) {
            return TEST_DATABASE_PATH;
        } else {
            return PROD_DATABASE_PATH;
        }

    }

    public boolean isLaunchBrowserOnStart() {
        return isDevProfileEnabled() == false;
    }

    public boolean isDevProfileEnabled() {
        List<String> activeProfiles = Arrays.asList(environment.getActiveProfiles());
        return activeProfiles.contains(DEV_PROFILE);
    }

}

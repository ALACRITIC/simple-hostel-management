package org.remipassmoilesel;

import org.apache.commons.io.FileUtils;
import org.remipassmoilesel.utils.UpdateFilesListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Locale;

@Configuration
@EnableAutoConfiguration
@ComponentScan
@ServletComponentScan
public class Application extends WebMvcConfigurerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {

        // drop database if asked in configuration, for debug purposes
        if (MainConfiguration.DROP_DATABASE_ON_LAUNCH == true) {
            try {
                FileUtils.deleteDirectory(MainConfiguration.DATABASE_PATH.toFile());
                logger.error("Database have been dropped");
            } catch (IOException e) {
                logger.error("Error while reseting database");
            }
        }

        // standalone server for development
        SpringApplication app = new SpringApplication(Application.class);

        // listen application to update files
        UpdateFilesListener updater = new UpdateFilesListener();
        updater.addPeer(Paths.get("src/main/resources"), Paths.get("target/classes"));

        // first file update
        app.addListeners(updater);
        updater.update();

        //TODO
        // wait 2 minutes and launch user browser

        // run server
        app.run(args);

    }

    /**
     * Locale settings
     */

    /**
     * Set up the localized message source
     *
     * @return
     */
    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:/locales/messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    /**
     * Set up a locale cookie system
     *
     * @return
     */
    @Bean
    public CookieLocaleResolver localeResolver() {
        CookieLocaleResolver localeResolver = new CookieLocaleResolver();
        localeResolver.setDefaultLocale(Locale.ENGLISH);
        localeResolver.setCookieName("my-locale-cookie");
        localeResolver.setCookieMaxAge(3600);
        return localeResolver;
    }

    /**
     * Set up locale configuration by url parameter
     *
     * @return
     */
    @Bean
    public LocaleChangeInterceptor localeInterceptor() {
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        interceptor.setParamName("lang");
        return interceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeInterceptor());
    }


}
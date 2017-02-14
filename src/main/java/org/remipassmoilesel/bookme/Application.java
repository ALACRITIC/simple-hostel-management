package org.remipassmoilesel.bookme;

import org.remipassmoilesel.bookme.utils.UpdateFilesListener;
import org.remipassmoilesel.bookme.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Locale;

/**
 * Use option -Djava.awt.headless=false if you want to add a system tray icon
 */
@SpringBootApplication
public class Application extends WebMvcConfigurerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    private static SpringApplication mainApp;

    public static void main(String[] args) throws Exception {

        // standalone server for development
        mainApp = new SpringApplication(Application.class);

        if (Arrays.asList(args).contains(CustomConfiguration.DEV_PROFILE)) {
            mainApp.setAdditionalProfiles(CustomConfiguration.DEV_PROFILE);
        }

        // listen application to update files
        UpdateFilesListener updater = new UpdateFilesListener();
        updater.addPeer(Paths.get("src/main/resources"), Paths.get("target/classes"));

        // first file update
        mainApp.addListeners(updater);

        // open browser on launch if needed
        mainApp.addListeners((event) -> {
            if (event instanceof ApplicationReadyEvent) {
                CustomConfiguration config = ((ApplicationReadyEvent) event).getApplicationContext().getBean(CustomConfiguration.class);
                if (config.isLaunchBrowserOnStart() == true) {
                    showMainPage();
                }
            }
        });

        // run server
        mainApp.run(args);

    }

    private static void showMainPage() {
        URI uri = null;
        try {
            uri = new URI(Utils.getBaseUrlForEmbeddedTomcat());
            Desktop.getDesktop().browse(uri);
        } catch (Exception e) {
            logger.error("Unable to launch browser: ", e);

            String message = "Unable to launch your web browser. ";
            if (uri != null) {
                message += "Please visit manually: " + uri;
            }
            JOptionPane.showMessageDialog(null,
                    message,
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
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
        localeResolver.setCookieName("bookme-locale-cookie");
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
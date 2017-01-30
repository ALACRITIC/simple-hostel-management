package org.remipassmoilesel;

import org.apache.commons.io.FileUtils;
import org.remipassmoilesel.utils.UpdateFilesListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Paths;

@Configuration
@EnableAutoConfiguration
@ComponentScan
@ServletComponentScan
public class Application {

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

}
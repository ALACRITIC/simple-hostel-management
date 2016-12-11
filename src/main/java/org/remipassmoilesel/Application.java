package org.remipassmoilesel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Paths;

@Configuration
@EnableAutoConfiguration
@ComponentScan
@ServletComponentScan
public class Application {

    public static void main(String[] args) {

        // standalone server for development
        SpringApplication app = new SpringApplication(Application.class);

        // listen application to update files
        UpdateFilesListener updater = new UpdateFilesListener();
        updater.addPeer(Paths.get("src/main/resources"), Paths.get("target/classes"));

        // first file update
        app.addListeners(updater);
        updater.update();

        // run server
        app.run(args);

    }

}
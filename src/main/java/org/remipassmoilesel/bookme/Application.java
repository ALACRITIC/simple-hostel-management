package org.remipassmoilesel.bookme;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.remipassmoilesel.bookme.configuration.SpringConfiguration;
import org.remipassmoilesel.bookme.utils.UpdateFilesListener;
import org.remipassmoilesel.bookme.utils.Utils;
import org.remipassmoilesel.bookme.utils.thread.ThreadManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;

/**
 * Use option -Djava.awt.headless=false if you want to add a system tray icon
 */
@SpringBootApplication
public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    private static SpringApplication mainApp;

    public static void main(String[] args) throws Exception {

        // standalone server for development
        mainApp = new SpringApplication(Application.class);

        // ask for updates
        ThreadManager.runLater(() -> {
            askForUpdate();
        });

        logger.warn("Specified arguments: " + Arrays.asList(args));

        if (Arrays.asList(args).contains(SpringConfiguration.DEV_PROFILE)) {
            mainApp.setAdditionalProfiles(SpringConfiguration.DEV_PROFILE);
        }

        // listen application to update files
        UpdateFilesListener updater = new UpdateFilesListener();
        updater.addPeer(Paths.get("src/main/resources"), Paths.get("target/classes"));

        // first file update
        mainApp.addListeners(updater);

        // open browser on launch if needed
        mainApp.addListeners((event) -> {
            if (event instanceof ApplicationReadyEvent) {
                SpringConfiguration config = ((ApplicationReadyEvent) event).getApplicationContext().getBean(SpringConfiguration.class);
                if (config == null) {
                    logger.error("Configuration not ready");
                    return;
                }
                if (config.isLaunchBrowserOnStart() == true) {
                    showMainPage();
                }
            }
        });

        // run server
        try {
            mainApp.run(args);
        } catch (Exception e) {
            logger.error("Error while launching application: ", e);

            // if not headless, show a dialog
            if (GraphicsEnvironment.isHeadless() == false) {

                String message = "<html><p>Unable to launch application: ";
                if (e.getMessage() != null && e.getMessage().length() > 50) {
                    message += e.getMessage().substring(0, 50) + " ...";
                } else {
                    message += e.getMessage();
                }

                message += "</p>";
                message += "<p>First of all, check if software is not already launched (you will see an icon in your task bar)</p>";
                message += "<p>Then try restarting the program. If this error persists, try restarting your computer and restart the program.</p></html>";

                try {
                    JOptionPane.showMessageDialog(null,
                            message,
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                } catch (Exception e2) {
                    logger.error("Unable to show graphical error: ", e2);
                }
            }
        }
    }

    private static void askForUpdate() {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(SpringConfiguration.UPDATE_SERVER_ADDRESS);
        try (CloseableHttpResponse response1 = httpclient.execute(httpGet)) {

            //System.out.println(Arrays.asList(response1.getAllHeaders()));
            HttpEntity entity = response1.getEntity();

            try (BufferedReader bis = new BufferedReader(new InputStreamReader(entity.getContent()))) {
                String line = null;
                String content = "";
                while ((line = bis.readLine()) != null) {
                    content += line;
                }

                logger.warn("Update message: " + content);
            }
            EntityUtils.consume(entity);
        } catch (IOException e) {
            logger.error("Unable to reach update server", e);
        }
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

}
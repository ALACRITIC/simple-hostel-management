package org.remipassmoilesel.bookme.systemtray;

import org.remipassmoilesel.bookme.Application;
import org.remipassmoilesel.bookme.Mappings;
import org.remipassmoilesel.bookme.utils.ApplicationContextProvider;
import org.remipassmoilesel.bookme.utils.Utils;
import org.remipassmoilesel.bookme.utils.thread.ThreadManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

/**
 * Created by remipassmoilesel on 13/02/17.
 */
@Component
@Scope("singleton")
public class SystemTrayComponent {

    private static final Logger logger = LoggerFactory.getLogger(SystemTrayComponent.class);

    private ArrayList<TrayItem> trayItems;
    private TrayIcon trayIcon;

    public SystemTrayComponent() {

        // links to create, from top to bottom
        trayItems = new ArrayList<>();

        trayItems.add(new TrayItem("Dashboard", Mappings.APPLICATION_ROOT));
        trayItems.add(new TrayItem("Calendar", Mappings.RESERVATIONS_CALENDAR));
        trayItems.add(new TrayItem("Add a reservation", Mappings.RESERVATIONS_FORM));
        trayItems.add(new TrayItem("Add a message", Mappings.MESSAGES_FORM));

        try {
            createTrayMenu();
        } catch (Exception e) {
            logger.error("Unable to create system tray icon", e);
        }

    }

    private void createTrayMenu() throws AWTException {

        // Check if the SystemTray is supported
        if (GraphicsEnvironment.isHeadless() == true) {
            throw new AWTException("JVM is headless, abort add system tray icon. Use: '-Djava.awt.headless=false' if possible to prevent errors");
        }

        if (SystemTray.isSupported() == false) {
            throw new AWTException("System tray is not supproted, abort");
        }

        // Check if menu already exist and if the SystemTray is supported
        if (trayIcon != null) {
            throw new AWTException("Menu already exist: " + trayIcon);
        }

        if (SystemTray.isSupported() == false) {
            throw new AWTException("System tray is not supported");
        }

        // create icon and pop up menu
        final PopupMenu popup = new PopupMenu();
        BufferedImage icon = null;
        try {
            icon = ImageIO.read(Application.class.getResourceAsStream("/static/assets/img/logo_tray.png"));
        } catch (IOException e) {
            logger.error("Error while reading system tray icon");
        }
        trayIcon = new TrayIcon(icon);
        trayIcon.setImageAutoSize(true);
        final SystemTray tray = SystemTray.getSystemTray();

        // exit on demand
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.addActionListener((ev) -> {
            exitSpringApplication();
        });

        // add header
        MenuItem header = new MenuItem("* Book-me ! *");
        header.setFont(new Font(Font.DIALOG, Font.BOLD, 12));
        popup.add(header);

        // Add components to pop-up menu
        for (TrayItem item : trayItems) {
            MenuItem it = new MenuItem(item.getName());
            it.addActionListener((ev) -> {
                openItem(item);
            });

            popup.add(it);
        }

        popup.addSeparator();
        popup.add(exitItem);

        trayIcon.setToolTip("Bookme - Right click to open menu");

        // add menu to system tray
        trayIcon.setPopupMenu(popup);

        tray.add(trayIcon);
    }

    private void openItem(TrayItem item) {

        URI url = null;
        try {
            url = new URI(Utils.getBaseUrlForEmbeddedTomcat() + item.getMapping());
            Desktop.getDesktop().browse(url);
        } catch (Exception e) {
            logger.error("Unable to launch browser: ", e);

            String message = "Unable to launch your web browser. ";

            if (url != null) {
                message += "Please visit manually: " + url;
            }
            JOptionPane.showMessageDialog(null,
                    message,
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Close gracefully spring on demand
     */
    private void exitSpringApplication() {
        ThreadManager.runLater(() -> {

            ApplicationContext appCtx = new ApplicationContextProvider().getApplicationContext();
            int code = SpringApplication.exit(appCtx);

            System.exit(code);

        });
    }

    /**
     * This method will be called when Spring app is destroyed
     */
    @PreDestroy
    public void removeSystemTray() {
        if (trayIcon != null) {
            final SystemTray tray = SystemTray.getSystemTray();
            tray.remove(trayIcon);
        }
    }


    @Override
    protected void finalize() throws Throwable {
        super.finalize();

        removeSystemTray();
    }
}

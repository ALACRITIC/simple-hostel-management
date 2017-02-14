package org.remipassmoilesel.bookme;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by remipassmoilesel on 12/12/16.
 */
public class MainConfiguration {

    public static final boolean DROP_DATABASE_ON_LAUNCH = false;

    public static final boolean ADD_ICON_IN_SYSTEM_TRAY = true;

    public static final Path DATABASE_DIRECTORY = Paths.get("database");
    public static final Path DATABASE_PATH = DATABASE_DIRECTORY.resolve("db.h2");


    public static boolean OPEN_BROWSER_ON_LAUNCH = true;
}

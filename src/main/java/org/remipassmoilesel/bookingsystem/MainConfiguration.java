package org.remipassmoilesel.bookingsystem;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by remipassmoilesel on 12/12/16.
 */
public class MainConfiguration {

    public static final boolean DROP_DATABASE_ON_LAUNCH = true;

    public static final Path DATABASE_DIRECTORY = Paths.get("database");
    public static final Path DATABASE_PATH = DATABASE_DIRECTORY.resolve("db.h2");


}

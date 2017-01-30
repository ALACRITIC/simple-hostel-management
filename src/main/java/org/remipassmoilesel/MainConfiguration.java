package org.remipassmoilesel;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by remipassmoilesel on 12/12/16.
 */
public class MainConfiguration {

    public static final boolean DROP_DATABASE_ON_LAUNCH = false;
    public static final Path DATABASE_PATH= Paths.get("database/db.h2");


}

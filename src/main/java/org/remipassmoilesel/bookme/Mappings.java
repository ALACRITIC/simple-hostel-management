package org.remipassmoilesel.bookme;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * Created by remipassmoilesel on 12/12/16.
 */
public class Mappings {

    private static final Logger logger = LoggerFactory.getLogger(Mappings.class);

    public static final String ROOT = "/";
    public static final String METRICS = "/metrics";

    public static final String APPLICATION_ROOT = "/app";

    public static final String RESERVATION_ROOT = APPLICATION_ROOT + "/reservation";
    public static final String RESERVATION_LASTS = RESERVATION_ROOT + "/lasts";
    public static final String RESERVATION_CALENDAR = RESERVATION_ROOT + "/calendar";
    public static final String RESERVATION_FORM = RESERVATION_ROOT + "/form";
    public static final String RESERVATION_JSON = RESERVATION_ROOT + "/json";
    public static final String RESERVATION_JSON_GET = RESERVATION_JSON + "/json";
    public static final String RESERVATION_ROOMS_AVAILABLE_JSON_GET = RESERVATION_JSON + "/availablerooms";

    public static final String ROOMS_ROOT = APPLICATION_ROOT + "/room";
    public static final String ROOMS_SHOW = ROOMS_ROOT + "/show";
    public static final String ROOMS_FORM = ROOMS_ROOT + "/form";
    public static final String ROOMS_JSON = ROOMS_ROOT + "/json";
    public static final String ROOMS_JSON_GET = ROOMS_JSON + "/all";


    public static final String POPULATE_TABLES = APPLICATION_ROOT + "/populate-tables";

    public static MappingMap getMap() {

        MappingMap result = new MappingMap();

        for (Field f : Mappings.class.getDeclaredFields()) {
            try {
                Object val = f.get(null);
                if (val instanceof String) {
                    result.put(f.getName(), (String) val);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Unable to access field: " + f);
            }
        }

        return result;
    }

    /**
     * Special class used to show errors
     */
    public static class MappingMap extends HashMap<String, String> {
        @Override
        public String get(Object key) {
            String res = super.get(key);
            if (res == null) {
                logger.error("Key do not exist: " + key, new Exception("Key do not exist: " + key));
            }
            return res;
        }
    }

    public static void includeMappings(Model model) {
        model.addAttribute("mappings", Mappings.getMap());
    }

}
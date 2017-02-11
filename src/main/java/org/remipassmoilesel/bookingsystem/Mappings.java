package org.remipassmoilesel.bookingsystem;

import org.springframework.ui.Model;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * Created by remipassmoilesel on 12/12/16.
 */
public class Mappings {

    public static final String ROOT = "/";
    public static final String METRICS = "/metrics";

    public static final String APPLICATION_INDEX = "/app";

    public static final String RESERVATION_ROOT = APPLICATION_INDEX + "/reservation";
    public static final String RESERVATION_LASTS = RESERVATION_ROOT + "/lasts";
    public static final String RESERVATION_CALENDAR = RESERVATION_ROOT + "/calendar";
    public static final String RESERVATION_FORM = RESERVATION_ROOT + "/form";
    public static final String RESERVATION_JSON = RESERVATION_ROOT + "/json";
    public static final String RESERVATION_JSON_GET = RESERVATION_JSON + "/json";

    public static final String ROOMS_ROOT = APPLICATION_INDEX + "/rooms";
    public static final String ROOMS_SHOW = ROOMS_ROOT + "/show";
    public static final String ROOMS_ADD = ROOMS_ROOT + "/add";
    public static final String ROOMS_JSON = ROOMS_ROOT + "/json";
    public static final String ROOMS_JSON_GET = ROOMS_JSON + "/get";

    public static HashMap<String, Object> getMap() {

        HashMap<String, Object> result = new HashMap<>();

        for (Field f : Mappings.class.getDeclaredFields()) {
            try {
                result.put(f.getName(), f.get(null));
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Unable to access field: " + f);
            }
        }

        return result;
    }

    public static void includeMappings(Model model) {
        model.addAttribute("mappings", Mappings.getMap());
    }

}

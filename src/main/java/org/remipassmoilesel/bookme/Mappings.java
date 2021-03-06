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
    public static final String MODEL_ARGUMENT_NAME = "mappings" ;

    public static final String ROOT = "/";
    public static final String METRICS = "/metrics";

    public static final String APPLICATION_ROOT = "/app";

    public static final String MAIN_MENU = APPLICATION_ROOT;
    public static final String DASHBOARD = APPLICATION_ROOT + "/dashboard";

    // no ending slash here
    public static final String BOWER_COMPONENTS_DIR = "/bower_components";
    public static final String ASSETS_DIR = "/assets";

    public static final String LOGIN_URL = APPLICATION_ROOT + "/login";
    public static final String LOGOUT_URL = APPLICATION_ROOT + "/logout";
    public static final String LOGOUT_SUCCESS_URL = APPLICATION_ROOT + "/login?logout";

    public static final String RESERVATIONS_ROOT = APPLICATION_ROOT + "/reservations";
    public static final String RESERVATIONS_SHOW_LATEST = RESERVATIONS_ROOT + "/latest";
    public static final String RESERVATIONS_NEXT_CHECKOUTS = RESERVATIONS_ROOT + "/nextcheckouts";
    public static final String RESERVATIONS_CALENDAR = RESERVATIONS_ROOT + "/calendar";
    public static final String RESERVATIONS_FORM = RESERVATIONS_ROOT + "/form";
    public static final String RESERVATIONS_DELETE = RESERVATIONS_ROOT + "/delete";
    public static final String RESERVATIONS_JSON = RESERVATIONS_ROOT + "/json";
    public static final String RESERVATIONS_JSON_GET = RESERVATIONS_JSON + "/get";
    public static final String RESERVATIONS_JSON_GET_LATEST = RESERVATIONS_JSON + "/latest";
    public static final String RESERVATIONS_JSON_SEARCH = RESERVATIONS_JSON + "/search";
    public static final String RESERVATIONS_ACCOMMODATIONS_AVAILABLE_JSON_GET = RESERVATIONS_JSON + "/free-accommodations";

    public static final String RESERVATIONS_BY_CUSTOMER = RESERVATIONS_ROOT + "/bycustomer";
    public static final String RESERVATIONS_BY_ACCOMMODATION = RESERVATIONS_ROOT + "/byaccommodation";

    public static final String ACCOMMODATIONS_ROOT = APPLICATION_ROOT + "/accommodations";
    public static final String ACCOMMODATIONS_SHOW_ALL = ACCOMMODATIONS_ROOT + "/show/all";
    public static final String ACCOMMODATIONS_JSON = ACCOMMODATIONS_ROOT + "/json";
    public static final String ACCOMMODATIONS_JSON_GET = ACCOMMODATIONS_JSON + "/all";
    public static final String ACCOMMODATIONS_FORM = ACCOMMODATIONS_ROOT + "/form";
    public static final String ACCOMMODATIONS_DELETE = ACCOMMODATIONS_ROOT + "/delete";
    public static final String ACCOMMODATIONS_CALENDAR = ACCOMMODATIONS_ROOT + "/calendar";

    public static final String MESSAGES_ROOT = APPLICATION_ROOT + "/messages";
    public static final String MESSAGES_FORM = MESSAGES_ROOT + "/form";
    public static final String MESSAGES_SHOW = MESSAGES_ROOT + "/show";
    public static final String MESSAGES_SHOW_BY_ID = MESSAGES_ROOT + "/byid";
    public static final String MESSAGES_SHOW_LATEST = MESSAGES_SHOW + "/latest";
    public static final String MESSAGES_JSON_GET_LATEST = APPLICATION_ROOT + "/json/latest";

    public static final String CUSTOMERS_ROOT = APPLICATION_ROOT + "/customers";
    public static final String CUSTOMERS_SHOW_LATEST = CUSTOMERS_ROOT + "/show/latest";
    public static final String CUSTOMERS_SEARCH = CUSTOMERS_ROOT + "/search";
    public static final String CUSTOMERS_FORM = CUSTOMERS_ROOT + "/form";
    public static final String CUSTOMERS_BILL_FORM = CUSTOMERS_ROOT + "/bill/form";
    public static final String CUSTOMERS_BILL_PRINT = CUSTOMERS_ROOT + "/bill/print";
    public static final String CUSTOMERS_JSON_ROOT = CUSTOMERS_ROOT + "/json";
    public static final String CUSTOMERS_JSON_SEARCH = CUSTOMERS_JSON_ROOT + "/search";
    public static final String CUSTOMERS_JSON_GET_LATEST = CUSTOMERS_JSON_ROOT + "/latest";

    public static final String SERVICES_ROOT = APPLICATION_ROOT + "/services";
    public static final String SERVICE_TYPES_ROOT = SERVICES_ROOT + "/types";

    public static final String SERVICE_TYPES_FORM = SERVICE_TYPES_ROOT + "/form";
    public static final String SERVICE_TYPES_DELETE = SERVICE_TYPES_ROOT + "/delete";
    public static final String SERVICE_TYPES_SHOW_ALL = SERVICE_TYPES_ROOT + "/show/all";

    public static final String SERVICES_DELETE = SERVICES_ROOT + "/delete";
    public static final String SERVICES_BY_CUSTOMER = SERVICES_ROOT + "/bycustomer";
    public static final String SERVICES_FORM = SERVICES_ROOT + "/form";
    public static final String SERVICES_SHOW_LATEST = SERVICES_ROOT + "/show/lastest";
    public static final String SERVICES_CALENDAR = SERVICES_ROOT + "/calendar";
    public static final String SERVICES_JSON_GET = SERVICES_ROOT + "/json/get";
    public static final String SERVICES_JSON_SEARCH = SERVICES_ROOT + "/json/search";
    public static final String SERVICES_BY_TYPE = SERVICES_ROOT + "/bytype";

    public static final String ADMINISTRATION_ROOT = APPLICATION_ROOT + "/admin";
    public static final String ADMINISTRATION_EXPORT_RESERVATIONS_CSV = ADMINISTRATION_ROOT + "/export/csv/reservations";
    public static final String ADMINISTRATION_EXPORT_SERVICES_CSV = ADMINISTRATION_ROOT + "/export/csv/services";

    public static final String POPULATE_TABLES = APPLICATION_ROOT + "/populate-tables";
    public static final String MAIN_TEMPLATE = APPLICATION_ROOT + "/main-template";

    public static final String TEST_ROOT = APPLICATION_ROOT + "/test/";

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
        model.addAttribute(Mappings.MODEL_ARGUMENT_NAME, Mappings.getMap());
    }


}

package org.remipassmoilesel.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by remipassmoilesel on 31/01/17.
 */
public class Utils {

    private static DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

    public static String dateToString(Date d) {
        return formatter.format(d);
    }

    public static Date stringToDate(String str) throws ParseException {
        return formatter.parse(str);
    }

}

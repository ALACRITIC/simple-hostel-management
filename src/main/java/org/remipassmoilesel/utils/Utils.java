package org.remipassmoilesel.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by remipassmoilesel on 31/01/17.
 */
public class Utils {

    private static DateFormat formatterDDMMYYYY = new SimpleDateFormat("dd/MM/yyyy");
    private static DateFormat formatterYYYYMMDD = new SimpleDateFormat("yyyy-MM-dd");

    public static String dateToString(Date d) {
        return formatterDDMMYYYY.format(d);
    }

    public static Date stringToDate(String str) throws Exception {
        try {
            return formatterDDMMYYYY.parse(str);
        } catch (Exception e) {
            try {
                return formatterYYYYMMDD.parse(str);
            } catch (Exception e2) {
                throw new Exception(e2);
            }
        }
    }

}

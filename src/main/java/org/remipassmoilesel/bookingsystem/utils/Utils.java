package org.remipassmoilesel.bookingsystem.utils;

import org.remipassmoilesel.bookingsystem.Mappings;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

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

    /**
     * Print attributes of a HTTP request for debug purposes
     *
     * @param httpRequest
     */
    public static void printAttributes(HttpServletRequest httpRequest) {

        System.out.println();
        System.out.println("Attributes of: " + httpRequest);
        Enumeration<String> names = httpRequest.getAttributeNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            Object value = httpRequest.getAttribute(name);
            System.out.println("\t # Name: " + name);
            System.out.println("\t   Value: " + value);
        }
    }

}

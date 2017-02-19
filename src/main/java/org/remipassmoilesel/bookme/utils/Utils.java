package org.remipassmoilesel.bookme.utils;

import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.embedded.EmbeddedWebApplicationContext;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainer;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * Created by remipassmoilesel on 31/01/17.
 */
public class Utils {

    private static final Logger logger = LoggerFactory.getLogger(Utils.class);

    private static DateFormat formatterDDMMYYYY = new SimpleDateFormat("dd/MM/yyyy");
    private static DateFormat formatterYYYYMMDD = new SimpleDateFormat("yyyy-MM-dd");

    public static String dateToString(Date d) {
        return formatterDDMMYYYY.format(d);
    }

    private static Random rand = new Random();

    private static final String STRING_SEPARATOR = ",";

    /**
     * Parse two type of dates: YYYYMMDD or DDMMYYYY
     *
     * @param str
     * @return
     * @throws Exception
     */
    public static Date stringToDate(String str) throws Exception {
        try {
            return formatterDDMMYYYY.parse(str);
        } catch (Exception e) {
            logger.debug("Error while parsing date: ", e);
        }
        try {
            return formatterYYYYMMDD.parse(str);
        } catch (Exception e2) {
            throw new Exception("Invalid date: " + str);
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

    /**
     * /!\ Work only if application run on embedded Tomcat server
     *
     * @return
     * @throws Exception
     */
    public static String getBaseUrlForEmbeddedTomcat() throws Exception {

        // get embedded tomcat
        EmbeddedWebApplicationContext appContext = (EmbeddedWebApplicationContext) new ApplicationContextProvider().getApplicationContext();
        Tomcat tomcat = ((TomcatEmbeddedServletContainer) appContext.getEmbeddedServletContainer()).getTomcat();
        Connector connector = tomcat.getConnector();

        // compose address
        String scheme = connector.getScheme();
        String hostName = tomcat.getHost().getName();
        int port = connector.getPort();
        String contextPath = appContext.getServletContext().getContextPath();

        return scheme + "://" + hostName + ":" + port + contextPath;
    }

    /**
     * Return a random int
     *
     * @param min
     * @param max
     * @return
     */
    public static int randInt(int min, int max) {
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }

    /**
     * Return a random value from list
     *
     * @return
     */
    public static Object randValueFrom(List list) {
        int randIndex = randInt(0, list.size() - 1);
        return list.get(randIndex);
    }

    private static final String LOREM_IPSUM = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. "
            + "Donec a diam lectus. Sed sit amet ipsum mauris. Maecenas congue ligula ac "
            + "quam viverra nec consectetur ante hendrerit. Donec et mollis dolor. "
            + "Praesent et diam eget libero egestas mattis sit amet vitae augue. Nam "
            + "tincidunt congue enim, ut porta lorem lacinia consectetur. Donec ut libero "
            + "sed arcu vehicula ultricies a non tortor. Lorem ipsum dolor sit amet, "
            + "consectetur adipiscing elit. Aenean ut gravida lorem. Ut turpis felis, "
            + "pulvinar a semper sed, adipiscing id dolor. Pellentesque auctor nisi id "
            + "magna consequat sagittis. Curabitur dapibus enim sit amet elit pharetra "
            + "tincidunt feugiat nisl imperdiet. Ut convallis libero in urna ultrices "
            + "accumsan. Donec sed odio eros. Donec viverra mi quis quam pulvinar at "
            + "malesuada arcu rhoncus. Cum sociis natoque penatibus et magnis dis parturient "
            + "montes, nascetur ridiculus mus. In rutrum accumsan ultricies. Mauris "
            + "vitae nisi at sem facilisis semper ac in est.";

    public static String generateLoremIpsum(int length) {

        String rslt = LOREM_IPSUM;

        while (rslt.length() < length) {
            rslt += LOREM_IPSUM;
        }

        return rslt.substring(0, length);
    }


    public static String colorToRgbString(Color c) {

        if (c == null) {
            return "null";
        }

        return String.join(STRING_SEPARATOR, Arrays.asList(
                String.valueOf(c.getRed()),
                String.valueOf(c.getGreen()),
                String.valueOf(c.getBlue())));

    }

    public static Color rgbStringToColor(String s) {

        if (s.equalsIgnoreCase("null")) {
            return null;
        }

        Integer[] vals = stringToIntArray(s);

        if (vals.length != 3) {
            throw new IllegalArgumentException("Invalid string: " + s);
        }

        return new Color(Integer.valueOf(vals[0]), Integer.valueOf(vals[1]), Integer.valueOf(vals[2]));
    }

    public static Integer[] stringToIntArray(String str) {

        String[] parts = str.toLowerCase().trim().split(STRING_SEPARATOR);
        ArrayList<Integer> result = new ArrayList<>();

        for (int i = 0; i < parts.length; i++) {
            try {
                result.add(Integer.valueOf(parts[i].trim()));
            } catch (Exception e) {
                logger.error("Error while parsing string ", e);
            }
        }

        return result.toArray(new Integer[result.size()]);
    }

    public static String colorToHex(Color color) {
        return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
    }

    public static String colorToHex(String rgbStr) {
        Color color = Utils.rgbStringToColor(rgbStr);
        return colorToHex(color);
    }

    public static Color hexToColor(String hexStr) {
        return Color.decode(hexStr);
    }
}

package org.remipassmoilesel.bookme.utils;

import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.springframework.boot.context.embedded.EmbeddedWebApplicationContext;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainer;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Random;

/**
 * Created by remipassmoilesel on 31/01/17.
 */
public class Utils {

    private static DateFormat formatterDDMMYYYY = new SimpleDateFormat("dd/MM/yyyy");
    private static DateFormat formatterYYYYMMDD = new SimpleDateFormat("yyyy-MM-dd");

    public static String dateToString(Date d) {
        return formatterDDMMYYYY.format(d);
    }

    private static Random rand = new Random();

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

}

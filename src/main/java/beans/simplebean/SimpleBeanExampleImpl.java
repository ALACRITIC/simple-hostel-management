package beans.simplebean;


import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;

/**
 * Created by remipassmoilesel on 09/12/16.
 */
public class SimpleBeanExampleImpl implements SimpleBeanExample {

    private static final Logger logger = Logger.getLogger(SimpleBeanExampleImpl.class.toString());

    @PostConstruct
    public void init() {
        System.out.println("Called after bean construction");
    }

    @Override
    public void doService() {
        System.out.println("Time: " + System.currentTimeMillis());
        System.out.println(this.toString());
    }

}

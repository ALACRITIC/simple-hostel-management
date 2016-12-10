package org.remipassmoilesel.beans.simplebean;


import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;

/**
 * Created by remipassmoilesel on 09/12/16.
 */
public class SimpleBeanExampleImpl implements SimpleBeanExample {

    private static final Logger logger = Logger.getLogger(SimpleBeanExampleImpl.class.toString());

    @PostConstruct
    public void postConstruct() {
        System.out.println("postConstruct() method called !");
    }

    @Override
    public void init() {
        System.out.println("init() method called");
    }

    @Override
    public void cleanup() {
        System.out.println("cleanup() method called");
    }

    @Override
    public void doService() {
        System.out.println("Time: " + System.currentTimeMillis());
        System.out.println(this.toString());
    }

}

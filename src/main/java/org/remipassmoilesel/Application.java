package org.remipassmoilesel;

import org.remipassmoilesel.plainexamples.simplebean.SimpleBeanExample;
import org.remipassmoilesel.plainexamples.simplebean.SimpleBeanExampleImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class Application {

    public static void main(String[] args) {
        // standalone server for development
        SpringApplication.run(Application.class, args);
    }

    /**
     * Explicit factory of bean
     * @return
     */
    @Bean(initMethod = "init", destroyMethod = "cleanup")
    SimpleBeanExample simpleBean() {
        return new SimpleBeanExampleImpl();
    }

}
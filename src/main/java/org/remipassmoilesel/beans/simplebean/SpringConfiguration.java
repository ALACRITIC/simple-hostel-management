package org.remipassmoilesel.beans.simplebean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfiguration {

    @Bean
    public SimpleBeanExample singletonService() {
        return new SimpleBeanExampleImpl();
    }

}

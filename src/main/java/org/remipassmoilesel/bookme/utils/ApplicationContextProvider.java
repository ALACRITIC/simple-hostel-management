package org.remipassmoilesel.bookme.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Simple utility that can be used to access application context
 * <p>
 * Usage: ApplicationContext appCtx = new ApplicationContextProvider().getApplicationContext();
 * <p>
 * See: https://spring.io/understanding/application-context
 */
@Component
public class ApplicationContextProvider implements ApplicationContextAware {

    private static ApplicationContext context;

    public ApplicationContext getApplicationContext() {
        return context;
    }

    @Override
    public void setApplicationContext(ApplicationContext ctx) {
        context = ctx;
    }

}

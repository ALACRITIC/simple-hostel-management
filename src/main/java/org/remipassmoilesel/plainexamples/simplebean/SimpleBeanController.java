package org.remipassmoilesel.plainexamples.simplebean;

import org.remipassmoilesel.Application;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SimpleBeanController {

    @RequestMapping("/simplebean")
    @ResponseBody
    public String simpleBean(Model model) {

        // explicit instantiation of bean
        ApplicationContext ctx = new AnnotationConfigApplicationContext(Application.class);
        SimpleBeanExample bean = ctx.getBean(SimpleBeanExample.class);

        return bean.doService();
    }

}
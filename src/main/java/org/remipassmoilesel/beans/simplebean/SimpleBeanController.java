package org.remipassmoilesel.beans.simplebean;

import org.remipassmoilesel.Application;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SimpleBeanController {

    @RequestMapping(value = "/greeting", method = RequestMethod.GET)
    public String greeting(@RequestParam(value = "name", required = false, defaultValue = "World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }

    @RequestMapping("/simplebean")
    public void simpleBean(Model model) {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(Application.class);
        SimpleBeanExample bean = ctx.getBean(SimpleBeanExample.class);
        bean.doService();
    }

}
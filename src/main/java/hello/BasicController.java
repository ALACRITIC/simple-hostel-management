package hello;

import beans.simplebean.SimpleBeanExample;
import beans.simplebean.SpringConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BasicController {

    @RequestMapping("/main")
    public void main(Model model) {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfiguration.class);
        SimpleBeanExample bean = ctx.getBean(SimpleBeanExample.class);
        bean.doService();
    }

    @RequestMapping("/greeting")
    public String greeting(@RequestParam(value = "name", required = false, defaultValue = "World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }

}
package org.remipassmoilesel.utils;

import org.remipassmoilesel.Mappings;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by remipassmoilesel on 12/12/16.
 */
@Controller
@RequestMapping(Mappings.METRICS)
public class Metrics {

    private static String[] metricsMapping = new String[]{
            "/management/metrics",
            "/management/health",
            "/management/info",
            "/management/trace",
            "/management/configprops",
            "/management/beans",
            "/management/env",
            "/management/heapdump",
            "/management/autoconfig"};

    @RequestMapping(method = RequestMethod.GET)
    public String metrics(Model model) {
        model.addAttribute("metricsMapping", metricsMapping);
        return "metrics";
    }

}

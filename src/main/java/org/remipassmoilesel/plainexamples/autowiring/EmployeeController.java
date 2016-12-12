package org.remipassmoilesel.plainexamples.autowiring;

import org.remipassmoilesel.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class EmployeeController {

    @Autowired
    private EmployeeService employsvc;

    @RequestMapping(value = Mappings.EMPLOYEE)
    @ResponseBody
    public String employee() {
        Employee employee = employsvc.affectTask("Pierre", "Go make french fries !");
        return "Task affected: <br/>" + employee.toString();
    }

}
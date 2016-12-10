package org.remipassmoilesel.beans.autowiring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class EmployeeController {

    @Autowired
    private EmployeeService employsvc;

    @RequestMapping(value = "/employee")
    public void employee() {
        Employee employee = employsvc.affectTask("Pierre", "Go make french fries !");
        System.out.println(employee);
    }

}
package org.remipassmoilesel.beans.autowiring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeDAO dao;

    public Employee affectTask(String employee, String task) {
        Employee empl = dao.get(employee);
        empl.addTask(task);
        return empl;
    }
}
package org.remipassmoilesel.beans.autowiring;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * Created by remipassmoilesel on 09/12/16.
 */
@Repository
public class EmployeeDAO {

    private final ArrayList<Employee> employees;

    public EmployeeDAO() {
        employees = new ArrayList<Employee>();
        employees.add(new Employee("Jean"));
        employees.add(new Employee("Pierre"));
        employees.add(new Employee("Paul"));
    }

    public Employee get(String name) {
        for (Employee e : employees) {
            if (e.getName().equalsIgnoreCase(name)) {
                return e;
            }
        }
        return null;
    }

    public ArrayList<Employee> getAll() {
        return new ArrayList<>(employees);
    }

}

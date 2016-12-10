package org.remipassmoilesel.beans.autowiring;

import java.util.ArrayList;

public class Employee {

    private final ArrayList<String> tasks;
    private String name;

    public Employee(String name) {
        this.name = name;
        this.tasks = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addTask(String taskId) {
        tasks.add(taskId);
    }

    @Override
    public String toString() {
        return "Employee{" +
                "tasks=" + tasks +
                ", name='" + name + '\'' +
                '}';
    }
}
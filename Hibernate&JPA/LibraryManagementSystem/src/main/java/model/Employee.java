package model;

import jakarta.persistence.Entity;

@Entity
public class Employee extends Person {

    private String department;

    private double salary;

    public Employee() {
    }

    public Employee(String name, String email, String department, double salary) {
        super(name, email);
        this.department = department;
        this.salary = salary;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }
}

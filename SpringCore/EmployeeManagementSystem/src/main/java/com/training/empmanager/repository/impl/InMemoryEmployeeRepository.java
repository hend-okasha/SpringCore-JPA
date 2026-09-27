package com.training.empmanager.repository.impl;

import com.training.empmanager.model.Employee;
import com.training.empmanager.repository.EmployeeRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;


@Repository
@Profile("dev")
public class InMemoryEmployeeRepository implements EmployeeRepository {

    private List<Employee> employeeList = new ArrayList<>();

    @Override
    public void save(Employee employee) {
        employeeList.removeIf(
                emp -> emp.getId() == employee.getId()
        );
        employeeList.add(employee);
    }

    @Override
    public Employee findById(int id) {
        return employeeList.stream().filter(emp -> emp.getId() == id )
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Employee> findAll() {
        return employeeList;
    }
}

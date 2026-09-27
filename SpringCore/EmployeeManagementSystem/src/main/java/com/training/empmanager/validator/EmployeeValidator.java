package com.training.empmanager.validator;

import com.training.empmanager.model.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeValidator {

    public void validate(Employee employee){

        if(employee == null) {
              throw new InvalidEmployeeException("Employee cannot be null");
          }

        if (employee.getName() == null || employee.getName().isBlank()){
            throw new InvalidEmployeeException("Employee name cannot be blank");
        }

        if (employee.getSalary() < 0 ){
            throw new InvalidEmployeeException("Employee salary cannot be negative ");
        }
    }
}

package com.training.empmanager.service.impl;

import com.training.empmanager.audit.AuditLogger;
import com.training.empmanager.model.Employee;
import com.training.empmanager.notify.NotificationManager;
import com.training.empmanager.notify.Notifier;
import com.training.empmanager.repository.EmployeeRepository;
import com.training.empmanager.service.EmployeeService;
import com.training.empmanager.validator.EmployeeValidator;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.xml.validation.Validator;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {


    private final EmployeeRepository employeeRepository;
    private final NotificationManager notificationManager;
    private final ObjectProvider<AuditLogger> auditLoggerProvider;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository,
                               NotificationManager notificationManager,
                               ObjectProvider<AuditLogger> auditLoggerProvider) {
        this.employeeRepository = employeeRepository;
        this.notificationManager = notificationManager;
        this.auditLoggerProvider = auditLoggerProvider;
    }

    @Autowired
    private EmployeeValidator validator;

    @Value("${company.name}")
    private String companyName;

    @Value("${company.currency}")
    private String currency;

    @Value("${notification.retry-count}")
    private int retryCount;

    @Value("${raise.max-percentage}")
    private double maxRaisePercentage;

    @Override
    public void addEmployee(Employee employee) {
        AuditLogger logger= auditLoggerProvider.getObject();

        validator.validate(employee);
        employeeRepository.save(employee);

        System.out.println("Employee add : " + employee.getName());
        notificationManager.notifyAll("New employee added:" + employee.getName());
        logger.log("Employee ADDED - id=" + employee.getId() + ", name=" + employee.getName());
    }

    @Override
    public Employee getEmployeeById(int id) {

        Employee employee = employeeRepository.findById(id);
        validator.validate(employee);

        return employee;
    }

    @Override
    public List<Employee> getAllEmployees() {
        List<Employee> employeeList= employeeRepository.findAll();
        return employeeList;
    }

    @Override
    public void giveRaise(int id, double percentage) {
        AuditLogger logger = auditLoggerProvider.getObject();
        Employee employee = employeeRepository.findById(id);
        validator.validate(employee);

        if (percentage > maxRaisePercentage){
            System.out.println("Raise REJECTED for Employee :" + employee.getName());
            logger.log("Raise REJECTED - id=" + id + ", requested=" + percentage + "%");
            return;
        }
        double oldSalary = employee.getSalary();
        double newSalary = oldSalary + (oldSalary * percentage / 100.0);
        employee.setSalary(newSalary);
        employeeRepository.save(employee);

        System.out.println("Raise SUCCESS for Employee :" + employee.getName());
        notificationManager.notifyAll("Employee " + employee.getName() + " received a " +
                        percentage + "% raise.");
        logger.log("Raise SUCCESS - id=" + id + ", requested=" + percentage + "%" + "SALARY = " + newSalary);

    }

    @Override
    public void printConfiguration() {
        System.out.println("--- Externalized Configuration ---");
        System.out.println("Company Name: " + companyName);
        System.out.println("Currency: " + currency);
        System.out.println("Notification Retry Count: " + retryCount);
        System.out.println("Maximum Raise Percentage: " + maxRaisePercentage + "%");
    }
}

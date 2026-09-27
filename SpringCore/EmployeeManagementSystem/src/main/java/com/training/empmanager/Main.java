package com.training.empmanager;


import com.training.empmanager.audit.AuditLogger;
import com.training.empmanager.config.AppConfig;
import com.training.empmanager.model.Employee;
import com.training.empmanager.service.EmployeeService;
import com.training.empmanager.service.impl.EmployeeServiceImpl;
import com.training.empmanager.validator.InvalidEmployeeException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        System.out.println(
                "=========================================="
        );

        System.out.println(
                " Employee Management System"
        );

        System.out.println(
                "=========================================="
        );

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

        context.getEnvironment().setActiveProfiles("dev");
        context.register(AppConfig.class);
        context.refresh();

        EmployeeService service = context.getBean(EmployeeService.class);
        EmployeeServiceImpl serviceImpl = context.getBean(EmployeeServiceImpl.class);

        Employee employee1 =
                new Employee(
                        1,
                        "Ahmed Ali",
                        "IT",
                        15000
                );

        try {
            service.addEmployee(employee1);
        } catch (InvalidEmployeeException e) {
            System.out.println("Validation Error: " + e.getMessage());
        }



        Employee invalidEmployee =
                new Employee(
                        2,
                        "",
                        "HR",
                        -5000
                );

        try {
            service.addEmployee(invalidEmployee);
        } catch (InvalidEmployeeException e) {

            System.out.println("Invalid employee handled gracefully: " + e.getMessage());
        }


        try {

            service.giveRaise(1, 10);

        } catch (InvalidEmployeeException e) {

            System.out.println("Raise Error: " + e.getMessage());
        }


        try {
            service.giveRaise(1, 30);
        } catch (InvalidEmployeeException e) {

            System.out.println("Raise rejected: " + e.getMessage());
        }


        System.out.println(
                "\n--- Prototype Scope Demo ---"
        );

        AuditLogger logger1 = context.getBean(AuditLogger.class);
        AuditLogger logger2 = context.getBean(AuditLogger.class);
        AuditLogger logger3 = context.getBean(AuditLogger.class);


        System.out.println("Logger 1 ID: " + System.identityHashCode(logger1));
        System.out.println("Logger 2 ID: " + System.identityHashCode(logger2));
        System.out.println("Logger 3 ID: " + System.identityHashCode(logger3));

        System.out.println("logger1 == logger2: " + (logger1 == logger2));
        System.out.println("logger2 == logger3: " + (logger2 == logger3));


        System.out.println("\n--- All Employees ---");
        service.getAllEmployees().forEach(System.out::println);


        serviceImpl.printConfiguration();


        System.out.println(
                "\n--- Closing Spring Context ---"
        );

        context.close();

        System.out.println(
                "=========================================="
        );

        System.out.println(
                "Application Finished"
        );

        System.out.println(
                "=========================================="
        );
    }
}
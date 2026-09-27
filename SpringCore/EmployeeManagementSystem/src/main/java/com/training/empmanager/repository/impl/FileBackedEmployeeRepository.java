package com.training.empmanager.repository.impl;

import com.training.empmanager.model.Employee;
import com.training.empmanager.repository.EmployeeRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
@Repository
@Profile("prod")
public class FileBackedEmployeeRepository implements EmployeeRepository {
    private static final String FILE_PATH = "employees.csv";

    public FileBackedEmployeeRepository() {

        try {
            if (!Files.exists(Paths.get(FILE_PATH))) {
                Files.createFile(Paths.get(FILE_PATH));
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize employees file", e);
        }
    }

    @Override
    public void save(Employee employee) {
        List<Employee> all = readAll();
        all.removeIf(
                emp -> emp.getId() == employee.getId()
        );
        all.add(employee);
        writeAll(all);
    }

    @Override
    public Employee findById(int id) {
        return readAll().stream()
                .filter(e -> e.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Employee> findAll() {
        return readAll();
    }


    private List<Employee> readAll() {
        List<Employee> employees = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0]);
                String name = parts[1];
                String department = parts[2];
                double salary = Double.parseDouble(parts[3]);
                employees.add(new Employee(id, name, department, salary));
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading employees file", e);
        }
        return employees;
    }

    private void writeAll(List<Employee> employees) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Employee e : employees) {
                writer.write(e.getId() + "," + e.getName() + "," +
                        e.getDepartment() + "," + e.getSalary());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error writing employees file", e);
        }
    }
}

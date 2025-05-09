package com.firma.firmazaposleni1.service;

import com.firma.firmazaposleni1.model.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {

    List<Employee> getAllEmployees();

    Employee createEmployee(Long companyId, String name, String position);

    Optional<Employee> getEmployeeById(Long id);

    Optional<Employee> updateEmployee(Long id, Employee employee);

    boolean deleteEmployee(Long id);

    List<Employee> getEmployeesByCompanyId(Long companyId);


}

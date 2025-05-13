package com.firma.firmazaposleni1.controller;
import com.firma.firmazaposleni1.model.Employee;
import com.firma.firmazaposleni1.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;



@RestController  //ova klasa odgovara na HTTP zahteve i vraca JSON kao rezultat
@RequestMapping("/employees") //sve putanje u ovoj klasi krecu sa /employees
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;


    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        if (employee.getCompany() == null || employee.getCompany().getId() == null) {
            return ResponseEntity.badRequest().build();
        }
        Employee savedEmployee = employeeService.createEmployee(
                employee.getCompany().getId(),
                employee.getName(),
                employee.getPosition()
        );
        return ResponseEntity.status(201).body(savedEmployee);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeByID(@PathVariable Long id) {
        return employeeService.getEmployeeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @Operation(summary = "Get all employees", description = "Retrieve a list of all employees")
    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee updatedEmployee) {
        return employeeService.updateEmployee(id, updatedEmployee)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        boolean deleted = employeeService.deleteEmployee(id);
        if (deleted) {
            return ResponseEntity.noContent().build(); // 204
        } else {
            return ResponseEntity.notFound().build();  // 404
        }
    }


    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<Employee>> getEmployeesByCompanyId(@PathVariable Long companyId) {
        List<Employee> employees = employeeService.getEmployeesByCompanyId(companyId);
        return ResponseEntity.ok(employees);
    }
}


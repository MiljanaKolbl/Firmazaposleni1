package com.firma.firmazaposleni1.controller;
import com.firma.firmazaposleni1.dto.request.EmployeeRequest;
import com.firma.firmazaposleni1.dto.response.EmployeeResponse;
import com.firma.firmazaposleni1.mapper.EmployeeMapper;
import com.firma.firmazaposleni1.model.Employee;
import com.firma.firmazaposleni1.service.EmployeeService;
//import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;


@RestController  //ova klasa odgovara na HTTP zahteve i vraca JSON kao rezultat
@RequestMapping("/employees") //sve putanje u ovoj klasi krecu sa /employees
public class EmployeeController {
    private final EmployeeService employeeService;
    private final EmployeeMapper employeeMapper;


    @Autowired
    public EmployeeController(EmployeeService employeeService, EmployeeMapper employeeMapper) {
        this.employeeService = employeeService;
        this.employeeMapper = employeeMapper;
    }

    @PostMapping
    public ResponseEntity<EmployeeResponse> createEmployee(@RequestBody EmployeeRequest employeeRequest) {
        if (employeeRequest.companyId() == null) {
            return ResponseEntity.badRequest().build();
        }
        Employee savedEmployee = employeeService.createEmployee(
                employeeRequest.companyId(),
                employeeRequest.name(),
                employeeRequest.position()
        );
        EmployeeResponse employeeResponse = employeeMapper.toResponse(savedEmployee);

        return ResponseEntity.status(201).body(employeeResponse); // 201 Created
    }


    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse> getEmployeeByID(@PathVariable Long id) {
        return employeeService.getEmployeeById(id)
                .map(employee -> ResponseEntity.ok(employeeMapper.toResponse(employee)))
                .orElse(ResponseEntity.notFound().build());
    }


    @GetMapping
    public ResponseEntity<List<EmployeeResponse>> getAllEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        List<EmployeeResponse> employeeResponses = employees.stream()
                .map(employeeMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(employeeResponses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponse> updateEmployee(@PathVariable Long id, @RequestBody EmployeeRequest updatedEmployeeRequest) {
        return employeeService.updateEmployee(id, updatedEmployeeRequest)
                .map(employeeMapper::toResponse)
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
    public ResponseEntity<List<EmployeeResponse>> getEmployeesByCompanyId(@PathVariable Long companyId) {
        List<Employee> employees = employeeService.getEmployeesByCompanyId(companyId);
        List<EmployeeResponse> employeeResponses = employees.stream()
                .map(employeeMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(employeeResponses);
    }
}


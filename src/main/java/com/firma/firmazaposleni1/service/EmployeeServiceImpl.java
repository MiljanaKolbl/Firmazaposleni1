package com.firma.firmazaposleni1.service;

import com.firma.firmazaposleni1.dto.request.EmployeeRequest;
import com.firma.firmazaposleni1.model.Company;
import com.firma.firmazaposleni1.model.Employee;
import com.firma.firmazaposleni1.repository.CompanyRepository;
import com.firma.firmazaposleni1.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final CompanyRepository companyRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, CompanyRepository companyRepository) {
        this.employeeRepository = employeeRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();    //ugradjena metoda iz JPARepository
    }


    @Override
    public Employee createEmployee(Long companyId, String name, String position) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));
        Employee employee = new Employee();
        employee.setName(name);
        employee.setPosition(position);
        employee.setCompany(company);

        return employeeRepository.save(employee);
    }
    @Transactional(readOnly = true)
    @Override
    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    public Optional<Employee> updateEmployee(Long id, EmployeeRequest request) {
        return employeeRepository.findById(id).map(employee -> {
            employee.setName(request.name());
            employee.setPosition(request.position());

            Company company = companyRepository.findById(request.companyId())
                    .orElseThrow(() -> new RuntimeException("Company not found"));

            employee.setCompany(company);

            return employeeRepository.save(employee);
        });
    }


    @Override
    public boolean deleteEmployee(Long id) {
        if (employeeRepository.existsById(id)) {  //proverava da li zaposleni postoji pre nego sto ga obrise
            employeeRepository.deleteById(id);
            return true;  //vracaz true ako je obrisan, false ako ne postoji
        }
        return false;
    }

    @Override
    public List<Employee> getEmployeesByCompanyId(Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));
        return employeeRepository.findByCompany(company);
    }

}



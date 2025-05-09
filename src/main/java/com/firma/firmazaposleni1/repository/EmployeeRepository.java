package com.firma.firmazaposleni1.repository;

import com.firma.firmazaposleni1.model.Employee;
import com.firma.firmazaposleni1.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByCompany(Company company);
}

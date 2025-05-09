package com.firma.firmazaposleni1;

import com.firma.firmazaposleni1.model.Company;
import com.firma.firmazaposleni1.model.Employee;
import com.firma.firmazaposleni1.repository.CompanyRepository;
import com.firma.firmazaposleni1.repository.EmployeeRepository;
import com.firma.firmazaposleni1.service.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.verification.VerificationMode;
import org.springframework.data.repository.CrudRepository;
import static org.mockito.Mockito.verify;

import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private CompanyRepository companyRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;
    private Company testCompany;

    @BeforeEach
    public void setUp() {
        testCompany = new Company();
        testCompany.setName("Firma Test");
        testCompany.setAddress("Adresa Test");
        testCompany.setId(1L);  // Postavljamo ID firme na 1

        employee = new Employee();
        employee.setName("Marko Marković");
        employee.setPosition("Programer");
        employee.setCompany(testCompany);
    }

    @Test
    public void testCreateEmployee() {
        when(companyRepository.findById(anyLong())).thenReturn(Optional.of(testCompany));
        when(employeeRepository.save(any(Employee.class))).thenAnswer(invocation -> {
            Employee employeeToSave = invocation.getArgument(0);
            employeeToSave.setId(1L);  // Simulacija dodeljivanja ID-ja nakon snimanja
            return employeeToSave;
        });

        Employee createdEmployee = employeeService.createEmployee(1L, "Marko Marković", "Programer");

        assertNotNull(createdEmployee);
        assertEquals("Marko Marković", createdEmployee.getName());
        assertEquals("Programer", createdEmployee.getPosition());
        assertEquals(1L, createdEmployee.getCompany().getId());
    }


    @Test
    void testGetEmployeeById() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        Optional<Employee> foundEmployee = employeeService.getEmployeeById(1L);

        assertTrue(foundEmployee.isPresent());
        assertEquals("Marko Marković", foundEmployee.get().getName());
    }

    @Test
    void testUpdateEmployee() {
        // Postojeći zaposleni u bazi
        Employee existingEmployee = new Employee();
        existingEmployee.setId(1L);
        existingEmployee.setName("Marko Marković");
        existingEmployee.setPosition("Programer");
        existingEmployee.setCompany(testCompany);

        // Novi podaci koje korisnik šalje za ažuriranje
        Employee updatedData = new Employee();
        updatedData.setName("Marko Izmenjen");
        updatedData.setPosition("Senior Programer");

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(existingEmployee));
        when(employeeRepository.save(any(Employee.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Optional<Employee> updatedEmployee = employeeService.updateEmployee(1L, updatedData);

        assertTrue(updatedEmployee.isPresent());
        assertEquals("Marko Izmenjen", updatedEmployee.get().getName());
        assertEquals("Senior Programer", updatedEmployee.get().getPosition());
    }


    @Test
    void testDeleteEmployee() {
        when(employeeRepository.existsById(1L)).thenReturn(true);

        boolean isDeleted = employeeService.deleteEmployee(1L);

        assertTrue(isDeleted);
        verify(employeeRepository, times(1)).deleteById(1L);  // verify da je pozvana deleteById() metoda
    }

}

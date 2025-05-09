
package com.firma.firmazaposleni1;

import com.firma.firmazaposleni1.model.Company;
import com.firma.firmazaposleni1.model.Employee;
import com.firma.firmazaposleni1.repository.CompanyRepository;
import com.firma.firmazaposleni1.repository.EmployeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.firma.firmazaposleni1.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;




@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class Firmazaposleni1ApplicationTests {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    private Company testCompany;


    @BeforeEach
    public void init() {
        employeeRepository.deleteAll();
        companyRepository.deleteAll();

        testCompany = new Company();
        testCompany.setName("Firma Test");
        testCompany.setAddress("Adresa Test");
        testCompany = companyRepository.save(testCompany);
    }

    @Test
    public void testCreateEmployee() {
        Long companyId = testCompany.getId();
        String name = "Marko Marković";
        String position = "Programer";

        Employee createdEmployee = employeeService.createEmployee(companyId, name, position);

        assertNotNull(createdEmployee.getId()); // sada stvarno mora da ima ID iz baze
        assertEquals(name, createdEmployee.getName());
        assertEquals(position, createdEmployee.getPosition());
        assertEquals(testCompany.getId(), createdEmployee.getCompany().getId());
    }

    @Test
    public void testGetEmployees() throws Exception {
        Employee emp1 = new Employee();
        emp1.setName("Ana Anić");
        emp1.setPosition("Menadžer");
        emp1.setCompany(testCompany);
        employeeRepository.save(emp1);

        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Ana Anić"));
    }

    @Test
    public void testUpdateEmployee() throws Exception {
        Employee employee = new Employee();
        employee.setName("Marko Marković");
        employee.setPosition("Analitičar");
        employee.setCompany(testCompany);
        employee = employeeRepository.save(employee);

        employee.setName("Marko Izmenjen");

        mockMvc.perform(put("/employees/" + employee.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Marko Izmenjen"));
    }

    @Test
    public void testDeleteEmployee() throws Exception {
        Employee employee = new Employee();
        employee.setName("Nikola Nikolić");
        employee.setPosition("HR");
        employee.setCompany(testCompany);
        employee = employeeRepository.save(employee);

        mockMvc.perform(delete("/employees/" + employee.getId()))
                .andExpect(status().isNoContent());
    }
}

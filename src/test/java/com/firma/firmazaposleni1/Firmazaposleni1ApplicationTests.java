
package com.firma.firmazaposleni1;

import com.firma.firmazaposleni1.dto.request.EmployeeRequest;
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

        assertNotNull(testCompany.getId(), "Test company ID should not be null");
    }

    @Test
    public void testCreateEmployee() throws Exception {
        Long companyId = testCompany.getId();
        String name = "Marko Marković";
        String position = "Programer";


        EmployeeRequest employeeRequest = new EmployeeRequest(name, position, companyId);

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.position").value(position))
                .andExpect(jsonPath("$.companyId").value(companyId))
                .andExpect(jsonPath("$.id").exists());
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
                .andExpect(jsonPath("$[0].name").value("Ana Anić"))
                .andExpect(jsonPath("$[0].position").value("Menadžer"))
                .andExpect(jsonPath("$[0].companyId").value(testCompany.getId()));
    }

    @Test
    public void testUpdateEmployee() throws Exception {
        Employee employee = new Employee();
        employee.setName("Marko Marković");
        employee.setPosition("Analitičar");
        employee.setCompany(testCompany);
        employee = employeeRepository.save(employee);


        EmployeeRequest updateRequest = new EmployeeRequest("Marko Izmenjen", "Analitičar",testCompany.getId());


        mockMvc.perform(put("/employees/" + employee.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Marko Izmenjen"))
                .andExpect(jsonPath("$.position").value("Analitičar"))
                .andExpect(jsonPath("$.companyId").value(testCompany.getId()));
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


        assertEquals(0, employeeRepository.count());
    }

    @Test
    public void testGetEmployeesByCompanyId() throws Exception {
        Employee emp1 = new Employee();
        emp1.setName("Ana Anić");
        emp1.setPosition("Menadžer");
        emp1.setCompany(testCompany);
        employeeRepository.save(emp1);

        Employee emp2 = new Employee();
        emp2.setName("Jovan Jovanović");
        emp2.setPosition("Programer");
        emp2.setCompany(testCompany);
        employeeRepository.save(emp2);

        mockMvc.perform(get("/employees/company/" + testCompany.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Ana Anić"))
                .andExpect(jsonPath("$[1].name").value("Jovan Jovanović"))
                .andExpect(jsonPath("$[0].companyId").value(testCompany.getId()))
                .andExpect(jsonPath("$[1].companyId").value(testCompany.getId()));
    }

    @Test
    public void testCreateEmployeeWithoutCompanyId() throws Exception {
        EmployeeRequest employeeRequest = new EmployeeRequest("Marko Markovic", "Programer", null);

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeRequest)))
                .andExpect(status().isBadRequest());
    }



}

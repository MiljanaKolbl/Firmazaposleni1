package com.firma.firmazaposleni1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.firma.firmazaposleni1.model.Company;
import com.firma.firmazaposleni1.repository.CompanyRepository;
import com.firma.firmazaposleni1.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class Firmazaposleni1ApplicationTests2 {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CompanyRepository companyRepository;

    private Company testCompany;

    @BeforeEach
    public void setUp() {
        employeeRepository.deleteAll();
        companyRepository.deleteAll();

        testCompany = new Company();
        testCompany.setName("Firma Test");
        testCompany.setAddress("Adresa Test");
        testCompany = companyRepository.saveAndFlush(testCompany);
    }

    @Test
    public void testCreateCompany() throws Exception {
        Company company = new Company(null, "Nova Firma", "Beograd");

        mockMvc.perform(post("/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(company)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Nova Firma"))
                .andExpect(jsonPath("$.address").value("Beograd"));
    }

    @Test
    public void testGetCompanyById() throws Exception {
        Company company = new Company(null, "Test Firma", "Novi Sad");

        String response = mockMvc.perform(post("/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(company)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Company created = objectMapper.readValue(response, Company.class);

        mockMvc.perform(get("/companies/" + created.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Firma"))
                .andExpect(jsonPath("$.address").value("Novi Sad"));
    }

    @Test
    public void testUpdateCompany() throws Exception {
        Company company = new Company(null, "Stara Firma", "Zemun");

        String response = mockMvc.perform(post("/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(company)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Company created = objectMapper.readValue(response, Company.class);
        created.setName("Izmenjena Firma");
        created.setAddress("Novi Beograd");

        mockMvc.perform(put("/companies/" + created.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(created)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Izmenjena Firma"))
                .andExpect(jsonPath("$.address").value("Novi Beograd"));
    }

    @Test
    public void testDeleteCompany() throws Exception {
        Company company = new Company(null, "Za brisanje", "Negotin");

        String response = mockMvc.perform(post("/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(company)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Company created = objectMapper.readValue(response, Company.class);

        mockMvc.perform(delete("/companies/" + created.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/companies/" + created.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetAllCompanies() throws Exception {
        Company c1 = new Company(null, "Firma1", "Adresa1");
        Company c2 = new Company(null, "Firma2", "Adresa2");

        mockMvc.perform(post("/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(c1)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(c2)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/companies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(greaterThanOrEqualTo(2)));
    }
}

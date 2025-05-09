package com.firma.firmazaposleni1;

import com.firma.firmazaposleni1.model.Company;
import com.firma.firmazaposleni1.repository.CompanyRepository;
import com.firma.firmazaposleni1.service.CompanyServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CompanyServiceImplTest {

    @Mock
    private CompanyRepository companyRepository;

    @InjectMocks
    private CompanyServiceImpl companyService;

    private Company company;

    @BeforeEach
    void setUp() {
        company = new Company();
        company.setName("Firma Test");
        company.setAddress("Test Adresa");
        company.setId(1L);
    }

    @Test
    void testCreateCompany() {
        when(companyRepository.save(any(Company.class))).thenReturn(company);

        Company createdCompany = companyService.createCompany(company);

        assertNotNull(createdCompany);
        assertEquals("Firma Test", createdCompany.getName());
    }

    @Test
    void testGetCompanyById() {
        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));

        Optional<Company> foundCompany = companyService.getCompanyById(1L);

        assertTrue(foundCompany.isPresent());
        assertEquals("Firma Test", foundCompany.get().getName());
    }

    @Test
    void testUpdateCompany() {
        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));
        company.setName("Firma Izmenjena");
        when(companyRepository.save(any(Company.class))).thenReturn(company);

        Optional<Company> updatedCompany = companyService.updateCompany(1L, company);

        assertTrue(updatedCompany.isPresent());
        assertEquals("Firma Izmenjena", updatedCompany.get().getName());
    }

    @Test
    void testDeleteCompany() {
        when(companyRepository.existsById(1L)).thenReturn(true);

        boolean isDeleted = companyService.deleteCompany(1L);

        assertTrue(isDeleted);
        verify(companyRepository, times(1)).deleteById(1L);
    }
}

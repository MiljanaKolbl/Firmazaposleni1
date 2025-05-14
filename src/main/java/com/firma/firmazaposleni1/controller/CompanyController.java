package com.firma.firmazaposleni1.controller;

import com.firma.firmazaposleni1.dto.request.CompanyRequest;
import com.firma.firmazaposleni1.dto.response.CompanyResponse;
import com.firma.firmazaposleni1.mapper.CompanyMapper;
import com.firma.firmazaposleni1.model.Company;
import com.firma.firmazaposleni1.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;


@RestController
@RequestMapping("/companies")

public class CompanyController {
    private final CompanyMapper companyMapper;
    public CompanyService companyService;

    @Autowired
    public CompanyController(CompanyService companyService, CompanyMapper companyMapper) {
        this.companyService = companyService;
        this.companyMapper = companyMapper;
    }


    @PostMapping
    public ResponseEntity<CompanyResponse> createCompany(@RequestBody CompanyRequest companyRequest) {
        Company savedCompany = companyService.createCompany(companyMapper.toEntity(companyRequest));
        return ResponseEntity.status(201)
                .body(companyMapper.toResponse(savedCompany));


    }

    @PutMapping("/{id}")
    public ResponseEntity<CompanyResponse> updateCompany(@PathVariable Long id, @RequestBody CompanyRequest companyRequest) {
        Company companyToUpdate = companyMapper.toEntity(companyRequest);
        return companyService.updateCompany(id, companyToUpdate)
                .map(updateCompany -> ResponseEntity.ok(companyMapper.toResponse(updateCompany)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable Long id) {
      boolean deleted = companyService.deleteCompany(id);
      return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }


    @GetMapping
    public ResponseEntity<List<CompanyResponse>> getAllCompanies() {
        List<Company> companies = companyService.getAllCompanies();
        List<CompanyResponse> responseList = companies.stream()
                .map(companyMapper::toResponse)
                .toList();
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyResponse> getCompanyById(@PathVariable Long id) {
        return companyService.getCompanyById(id)
                .map(companyMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}


package com.firma.firmazaposleni1.service;

import com.firma.firmazaposleni1.model.Company;

import java.util.List;
import java.util.Optional;

public interface CompanyService {

    List<Company> getAllCompanies();

    Company createCompany(Company company);

    boolean deleteCompany(Long id);

    Optional<Company> getCompanyById(Long id);

    Optional<Company> updateCompany(Long id, Company company);




}

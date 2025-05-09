package com.firma.firmazaposleni1.service;

import com.firma.firmazaposleni1.model.Company;
import com.firma.firmazaposleni1.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    @Autowired
    public CompanyServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }


    @Override
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    @Override
    public Company createCompany(Company company) {
       Company savedCompany = companyRepository.save(company);

        return savedCompany;
    }
    @Override
    public boolean deleteCompany(Long id) {
        if (companyRepository.existsById(id)) {
            companyRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Optional<Company> getCompanyById(Long id) {
        return companyRepository.findById(id);

    }

    @Override
    public Optional<Company> updateCompany(Long id, Company updatedCompany) {
        return companyRepository.findById(id).map(existing -> {
                    existing.setName(updatedCompany.getName());
                    existing.setAddress(updatedCompany.getAddress());
                    return companyRepository.save(existing);
                });
    }


}

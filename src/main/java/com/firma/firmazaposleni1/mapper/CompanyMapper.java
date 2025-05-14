package com.firma.firmazaposleni1.mapper;

import com.firma.firmazaposleni1.dto.request.CompanyRequest;
import com.firma.firmazaposleni1.dto.response.CompanyResponse;
import com.firma.firmazaposleni1.model.Company;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {EmployeeMapper.class})
public interface CompanyMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "employees", ignore = true)
    Company toEntity(CompanyRequest companyRequest);

    @Mapping(target = "employees", source = "employees")
    CompanyResponse toResponse(Company company);
}

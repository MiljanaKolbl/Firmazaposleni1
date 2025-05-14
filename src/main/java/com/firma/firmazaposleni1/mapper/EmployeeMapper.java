package com.firma.firmazaposleni1.mapper;


import com.firma.firmazaposleni1.dto.request.EmployeeRequest;
import com.firma.firmazaposleni1.dto.response.EmployeeResponse;
import com.firma.firmazaposleni1.model.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "company",  ignore = true)
    Employee toEntity(EmployeeRequest employeeRequest);
    @Mapping(target = "companyId", source = "company.id")
    EmployeeResponse toResponse(Employee employee);

    List<EmployeeResponse> toResponseList(List<Employee> employees);



}


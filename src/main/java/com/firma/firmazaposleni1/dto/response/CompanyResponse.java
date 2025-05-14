package com.firma.firmazaposleni1.dto.response;

import java.util.List;

public record CompanyResponse(
        Long id,
        String name,
        String address,
        List<EmployeeResponse> employees
) {}

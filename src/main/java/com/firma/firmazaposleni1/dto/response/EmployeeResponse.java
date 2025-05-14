package com.firma.firmazaposleni1.dto.response;

public record EmployeeResponse(
        Long id,
        String name,
        String position,
        Long companyId
) {}

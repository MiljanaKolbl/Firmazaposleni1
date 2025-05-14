package com.firma.firmazaposleni1.dto.request;

import org.springframework.lang.NonNull;

import java.util.List;

public record CompanyRequest(
         String name,
         String address,
         List<Long> employeeIds
) {}

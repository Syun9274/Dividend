package com.example.dividend.controller;

import com.example.dividend.exception.custom.CompanyException.EmptyTickerException;
import com.example.dividend.model.dto.CompanyDTO;
import com.example.dividend.model.entity.Company;
import com.example.dividend.model.request.CompanyRequest.AddCompanyRequest;
import com.example.dividend.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/company")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> searchCompany(final Pageable pageable) {
        Page<Company> companyList = companyService.getAllCompanies(pageable);
        return ResponseEntity.ok(companyList);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addCompany(@RequestBody AddCompanyRequest request) {
        String ticker = request.getTicker();
        if (ObjectUtils.isEmpty(ticker)) {
            throw new EmptyTickerException();
        }

        CompanyDTO companyDTO = companyService.saveCompany(ticker);

        return ResponseEntity.ok(companyDTO);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteCompany() {
        return null;
    }

}

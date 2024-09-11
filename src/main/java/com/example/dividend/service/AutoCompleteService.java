package com.example.dividend.service;

import com.example.dividend.model.entity.Company;
import com.example.dividend.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AutoCompleteService {

    private final CompanyRepository companyRepository;

    public List<String> getCompanyNamesByKeyword(String keyword) {
        Pageable limit = PageRequest.of(0, 10);
        List<Company> companyList = companyRepository.findAllByNameStartingWithIgnoreCase(keyword, limit);

        return companyList.stream()
                .map(e -> e.getName())
                .collect(Collectors.toList());
    }
}

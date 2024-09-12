package com.example.dividend.service;

import com.example.dividend.exception.custom.CompanyException.NoCompanyException;
import com.example.dividend.external.ScrapResult;
import com.example.dividend.external.scraper.Scraper;
import com.example.dividend.model.dto.CompanyDTO;
import com.example.dividend.model.entity.Company;
import com.example.dividend.model.entity.Dividend;
import com.example.dividend.repository.CompanyRepository;
import com.example.dividend.repository.DividendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.dividend.exception.custom.CompanyException.AlreadyExistCompanyException;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    private final Scraper yahooScraper;

    public CompanyDTO saveCompany(String ticker) {
        boolean exists = companyRepository.existsByTicker(ticker);
        if (exists) {
            throw new AlreadyExistCompanyException();
        }

        return saveCompanyAndDividend(ticker);
    }

    @Transactional
    protected CompanyDTO saveCompanyAndDividend(String ticker) {
        CompanyDTO companyDTO = yahooScraper.scrapCompanyByTicker(ticker);
        if (companyDTO == null) {
            throw new NoCompanyException();
        }

        ScrapResult scrapResult = yahooScraper.scrap(companyDTO);

        Company company = companyRepository.save(new Company(companyDTO));
        List<Dividend> dividendList = scrapResult.getDividends().stream()
                .map(dividend -> new Dividend(company, dividend))
                .collect(Collectors.toList());

        dividendRepository.saveAll(dividendList);

        return companyDTO;
    }

    public Page<Company> getAllCompanies(Pageable pageable) {
        return companyRepository.findAll(pageable);
    }

    @Transactional
    public String deleteCompany(String ticker) {
        var company =  companyRepository.findByTicker(ticker)
                .orElseThrow(NoCompanyException::new);

        dividendRepository.deleteByCompanyId(company.getId());
        companyRepository.delete(company);

        return company.getName();
    }
}

package com.example.dividend.service;

import com.example.dividend.external.ScrapResult;
import com.example.dividend.model.dto.CompanyDTO;
import com.example.dividend.model.dto.DividendDTO;
import com.example.dividend.model.entity.Company;
import com.example.dividend.model.entity.Dividend;
import com.example.dividend.repository.CompanyRepository;
import com.example.dividend.repository.DividendRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.example.dividend.exception.custom.CompanyException.NoCompanyException;

@Service
@AllArgsConstructor
public class FinanceService {

    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    public ScrapResult getDividendByCompanyName(String companyName) {
        Company company = companyRepository.findByName(companyName)
                .orElseThrow(NoCompanyException::new);

        List<Dividend> dividendList = dividendRepository.findByCompanyId(company.getId());

        List<DividendDTO> dividendDTOList = new ArrayList<>();
        for (Dividend dividend : dividendList) {
            dividendDTOList.add(
                    DividendDTO.builder()
                            .date(dividend.getDate())
                            .dividend(dividend.getDividend())
                            .build());
        }


        return new ScrapResult(
                CompanyDTO.builder()
                        .name(company.getName())
                        .ticker(company.getTicker())
                        .build(),
                dividendDTOList);
    }
}

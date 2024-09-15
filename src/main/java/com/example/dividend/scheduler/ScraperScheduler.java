package com.example.dividend.scheduler;

import com.example.dividend.external.ScrapResult;
import com.example.dividend.external.scraper.Scraper;
import com.example.dividend.model.dto.CompanyDTO;
import com.example.dividend.model.entity.Company;
import com.example.dividend.model.entity.Dividend;
import com.example.dividend.repository.CompanyRepository;
import com.example.dividend.repository.DividendRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@EnableCaching
@AllArgsConstructor
public class ScraperScheduler {

    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    private final Scraper yahooScraper;

    @CacheEvict(value = "finance", allEntries = true)
    @Scheduled(cron = "${scheduler.scrap.yahoo}")
    public void yahooFinanceScheduler() {
        log.info("Starting yahoo finance scheduler");

        List<Company> companyList = companyRepository.findAll();

        for (Company company : companyList) {
            log.info("Starting yahoo finance scheduler -> {}", company.toString());

            ScrapResult scrapResult = yahooScraper.scrap(CompanyDTO.builder()
                    .name(company.getName())
                    .ticker(company.getTicker())
                    .build());

            scrapResult.getDividends().stream()
                    .map(dividend -> new Dividend(company, dividend))
                    .forEach(e -> {
                        boolean exists = dividendRepository.existsByCompanyIdAndDate(e.getId(), e.getDate());
                        if (!exists) {
                            dividendRepository.save(e);
                        }
                    });

            // 과부하를 방지하기 위한 지연시간 설정
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                log.error(e.getMessage());
                Thread.currentThread().interrupt();
            }

        }

    }
}

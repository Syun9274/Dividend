package com.example.dividend.external.scraper;

import com.example.dividend.external.ScrapResult;
import com.example.dividend.model.dto.CompanyDTO;

public interface Scraper {

    ScrapResult scrap(CompanyDTO company);
    CompanyDTO scrapCompanyByTicker(String ticker);
}

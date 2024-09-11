package com.example.dividend.external.scraper;

import com.example.dividend.external.ScrapResult;
import com.example.dividend.model.dto.CompanyDTO;
import com.example.dividend.model.dto.DividendDTO;
import com.example.dividend.model.enums.Month;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class YahooScraper implements Scraper {

    // 2024년 9월 기준, 추후에 URL 형식이 변경될 수 있으며 자세한 내용은 "https://finance.yahoo.com/quote" 에서 확인 필요
    private static final String BASE_URL = "https://finance.yahoo.com/quote/%s/history?filter=div&period1=%d&period2=%d&frequency=1mo";
    private static final String SUMMARY_URL = "https://finance.yahoo.com/quote/%s/?p=%s";

    private static final long START_TIME = 86400;

    public ScrapResult scrap(CompanyDTO company) {
        var scrapResult = new ScrapResult();
        scrapResult.setCompany(company);

        try {
            long now = System.currentTimeMillis() / 1000;
            String url = String.format(BASE_URL, company.getTicker(), START_TIME, now);

            Connection connection = Jsoup.connect(url);
            Document document = connection.get();

            Elements elements = document
                    .getElementsByAttributeValue("data-testid", "history-table");
            if (elements.isEmpty()) {
                throw new RuntimeException("History table not found");
            }

            Element element = elements.get(0);
            Element tbody = element.getElementsByTag("tbody").first();

            List<DividendDTO> dividends = new ArrayList<>();

            for (Element tr : tbody.children()) {
                String txt = tr.text();

                if (!txt.contains("Dividend")) {
                    continue;
                }

                String[] splits = txt.split(" ");
                int month = Month.strToNum(splits[0]);
                int day = Integer.parseInt(splits[1].replace(",", ""));
                int year = Integer.parseInt(splits[2]);

                String dividend = splits[3];

                if (month < 0) {
                    throw new RuntimeException("Invalid month: " + splits[0]);
                }

                dividends.add(DividendDTO.builder()
                        .date(LocalDateTime.of(year, month, day, 0, 0))
                        .dividend(dividend)
                        .build());
            }

            scrapResult.setDividends(dividends);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return scrapResult;
    }

    public CompanyDTO scrapCompanyByTicker(String ticker) {
        String url = String.format(SUMMARY_URL, ticker, ticker);

        try {
            Document document = Jsoup.connect(url).get();

            // 회사 이름을 스크랩하는 부분, 코드 작성 전 해당 페이지의 html에서 클래스 이름이 올바른지 확인 필요
            Element titleElement = document.getElementsByClass("yf-3a2v0c").first();

            // "companyName (ticker)"의 형식을 갖고 있음.
            String fullTitle = titleElement.text();

            // "\\(.*\\)"는 괄호와 그 안의 모든 내용을 의미, "\\s*"는 앞뒤 공백을 제거
            String title = fullTitle.replaceAll("\\s*\\(.*\\)", "");

            return CompanyDTO.builder()
                    .ticker(ticker)
                    .name(title)
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

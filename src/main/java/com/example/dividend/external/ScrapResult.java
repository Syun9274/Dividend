package com.example.dividend.external;

import com.example.dividend.model.dto.CompanyDTO;
import com.example.dividend.model.dto.DividendDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class ScrapResult {

    private CompanyDTO company;
    private List<DividendDTO> dividends;

    public ScrapResult() {
        this.dividends = new ArrayList<>();
    }
}

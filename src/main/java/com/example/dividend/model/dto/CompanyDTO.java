package com.example.dividend.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CompanyDTO {

    private String name;
    private String ticker;
}

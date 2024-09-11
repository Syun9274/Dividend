package com.example.dividend.model.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
public class CompanyRequest {

    @Getter
    @Setter
    public static class AddCompanyRequest {

        private String ticker;
    }
}

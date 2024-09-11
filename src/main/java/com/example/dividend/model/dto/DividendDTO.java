package com.example.dividend.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class DividendDTO {

    private LocalDateTime date;
    private String dividend;
}

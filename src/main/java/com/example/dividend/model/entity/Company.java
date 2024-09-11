package com.example.dividend.model.entity;

import com.example.dividend.model.dto.CompanyDTO;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "company")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String ticker;

    public Company(CompanyDTO companyDTO) {
        this.name = companyDTO.getName();
        this.ticker = companyDTO.getTicker();
    }
}

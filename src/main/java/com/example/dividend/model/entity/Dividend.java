package com.example.dividend.model.entity;

import com.example.dividend.model.dto.DividendDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "dividend")
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"company_id", "date"})})
public class Dividend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    private LocalDateTime date;

    private String dividend;

    public Dividend(Company company, DividendDTO dividendDTO) {
        this.company = company;
        this.date = dividendDTO.getDate();
        this.dividend = dividendDTO.getDividend();
    }
}

package com.example.dividend.repository;

import com.example.dividend.model.entity.Company;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    boolean existsByTicker(String ticker);

    Optional<Company> findByName(String name);

    Optional<Company> findByTicker(String ticker);

    List<Company> findAllByNameStartingWithIgnoreCase(String keyword, Pageable pageable);
}

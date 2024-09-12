package com.example.dividend.repository;

import com.example.dividend.model.entity.Dividend;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DividendRepository extends JpaRepository<Dividend, Long> {

    List<Dividend> findByCompanyId(Long companyId);

    @Transactional
    void deleteByCompanyId(Long companyId);
}

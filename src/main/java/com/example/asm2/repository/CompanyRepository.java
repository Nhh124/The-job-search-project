package com.example.asm2.repository;

import com.example.asm2.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company,Integer> {
    Company findById(int id);

    Company findByUserId(int id);

}

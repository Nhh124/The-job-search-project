package com.example.asm2.service;

import com.example.asm2.entity.Company;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CompanyService {

    List<Company> findAll();

    Company findById(int id);

    void saveOrUpdate(Company company);

    Company findByUserId(int id);
}

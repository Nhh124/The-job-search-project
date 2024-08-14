package com.example.asm2.service;

import com.example.asm2.entity.Company;
import com.example.asm2.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService{

    @Autowired
    private CompanyRepository companyRepository;

    @Override
    public List<Company> findAll() {
        return companyRepository.findAll();
    }

    @Override
    public Company findById(int id) {
        return companyRepository.findById(id);
    }

    @Override
    public void saveOrUpdate(Company company) {
        companyRepository.save(company);
    }

    @Override
    public Company findByUserId(int id) {
        return companyRepository.findByUserId(id);
    }
}

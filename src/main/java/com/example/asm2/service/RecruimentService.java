package com.example.asm2.service;

import com.example.asm2.entity.Recruitment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RecruimentService {

    List<Recruitment> findAll();
    Recruitment findById(int id);

    void saveOrUpdate(Recruitment recruitment);

    void delete(Recruitment recruitment);

    List<Recruitment> findByUserId(int id);

    List<Recruitment> findRelated(Recruitment recruitment);


    List<Recruitment> findByTitle(String keyword);

    List<Recruitment> findByNameCompany(String keyword);

    List<Recruitment> findByAddress(String keyword);

    List<Recruitment> findByNewDate();
}

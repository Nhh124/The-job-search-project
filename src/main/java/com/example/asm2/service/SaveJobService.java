package com.example.asm2.service;

import com.example.asm2.entity.Recruitment;
import com.example.asm2.entity.SaveJob;
import com.example.asm2.entity.User;
import jakarta.persistence.Entity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SaveJobService {

    List<SaveJob> findAll();

    void saveOrUpdate(SaveJob saveJob);

    void delete(SaveJob saveJob);

    SaveJob findByUserAndRe(User user, Recruitment recruitment);

    void removeFollowJob(SaveJob existsJob);

    void addFollowJob(User user, Recruitment recruitment);

    List<SaveJob> findAllByUserId(int id);

    SaveJob findById(int id);
}

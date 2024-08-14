package com.example.asm2.service;

import com.example.asm2.entity.ApplyPost;
import com.example.asm2.entity.Recruitment;
import com.example.asm2.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ApplyPostService {

    List<ApplyPost> findAll();

    ApplyPost findById(int id);

    void saveOrUpdate(ApplyPost applyPost);

    ApplyPost findByRecruitmentAndUser(Recruitment recruitment, User user);

    List<ApplyPost> findByRecruitmentId(int id);
}

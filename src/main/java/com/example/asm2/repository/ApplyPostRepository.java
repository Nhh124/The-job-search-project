package com.example.asm2.repository;

import com.example.asm2.entity.ApplyPost;
import com.example.asm2.entity.Recruitment;
import com.example.asm2.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplyPostRepository extends JpaRepository<ApplyPost, Integer> {
    ApplyPost findById(int id);

    ApplyPost findByUserId(int id);
    ApplyPost findByRecruitmentIdAndUserId(int recruitmentId, int userId);

    List<ApplyPost> findByRecruitmentId(int id);
}

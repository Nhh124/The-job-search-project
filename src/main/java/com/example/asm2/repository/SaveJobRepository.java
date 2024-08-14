package com.example.asm2.repository;

import com.example.asm2.entity.SaveJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaveJobRepository extends JpaRepository<SaveJob,Integer> {
    SaveJob findByUserIdAndRecruitmentId(int userId, int recruitmentId);

    List<SaveJob> findByUserId(int id);

    SaveJob findById(int id);
}

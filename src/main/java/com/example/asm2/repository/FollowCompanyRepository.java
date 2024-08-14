package com.example.asm2.repository;

import com.example.asm2.entity.FollowCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowCompanyRepository extends JpaRepository<FollowCompany,Integer> {

    FollowCompany findById(int id);

    FollowCompany findByUserId(int id);
    FollowCompany findByUserIdAndCompanyId(int userId,int companyId);

    List<FollowCompany> findAllByUserId(int id);
}

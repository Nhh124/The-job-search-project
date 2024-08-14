package com.example.asm2.service;

import com.example.asm2.entity.Company;
import com.example.asm2.entity.FollowCompany;
import com.example.asm2.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FollowCompanyService {

    List<FollowCompany> findAll();

    FollowCompany findById(int id);

    void delete (FollowCompany followCompany);

    void saveOrUpdate(FollowCompany followCompany);

    void addFollowCompany(User user, Company company);

    FollowCompany findFollowByUserId(int id);

    void removeFollowCompany(FollowCompany existingFollow);

    FollowCompany findFollowByUserAndCompany(User user, Company company);

    List<FollowCompany> findALlFollowByUserId(int id);
}

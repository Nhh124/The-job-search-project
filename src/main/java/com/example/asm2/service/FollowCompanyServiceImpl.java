package com.example.asm2.service;

import com.example.asm2.entity.Company;
import com.example.asm2.entity.FollowCompany;
import com.example.asm2.entity.User;
import com.example.asm2.repository.FollowCompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FollowCompanyServiceImpl implements FollowCompanyService {

    private final FollowCompanyRepository followCompanyRepository;

    @Autowired
    public FollowCompanyServiceImpl(FollowCompanyRepository followCompanyRepository) {
        this.followCompanyRepository = followCompanyRepository;
    }

    @Override
    public List<FollowCompany> findAll() {
        return followCompanyRepository.findAll();
    }

    @Override
    public FollowCompany findById(int id) {
        return followCompanyRepository.findById(id);
    }

    @Override
    public void delete(FollowCompany followCompany) {
        followCompanyRepository.delete(followCompany);
    }

    @Override
    public void saveOrUpdate(FollowCompany followCompany) {
        followCompanyRepository.save(followCompany);
    }

    @Override
    public void addFollowCompany(User user, Company company) {
        FollowCompany followCompany = new FollowCompany(company,user);
        saveOrUpdate(followCompany);
    }

    @Override
    public FollowCompany findFollowByUserId(int id) {
        return followCompanyRepository.findByUserId(id);
    }

    @Override
    public FollowCompany findFollowByUserAndCompany(User user,Company company) {
        return followCompanyRepository.findByUserIdAndCompanyId(user.getId(),company.getId());
    }

    @Override
    public List<FollowCompany> findALlFollowByUserId(int id) {
        return followCompanyRepository.findAllByUserId(id);
    }

    @Override
    public void removeFollowCompany(FollowCompany existingFollow) {
        followCompanyRepository.delete(existingFollow);
    }
}
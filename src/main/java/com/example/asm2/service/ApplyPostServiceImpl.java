package com.example.asm2.service;

import com.example.asm2.entity.ApplyPost;
import com.example.asm2.entity.Recruitment;
import com.example.asm2.entity.User;
import com.example.asm2.repository.ApplyPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplyPostServiceImpl implements ApplyPostService{

    private ApplyPostRepository applyPostRepository;

    @Autowired
    public ApplyPostServiceImpl(ApplyPostRepository applyPostRepository) {
        this.applyPostRepository = applyPostRepository;
    }

    @Override
    public List<ApplyPost> findAll() {
        return applyPostRepository.findAll();
    }

    @Override
    public ApplyPost findById(int id) {
        return applyPostRepository.findById(id);
    }

    @Override
    public void saveOrUpdate(ApplyPost applyPost) {
        applyPostRepository.save(applyPost);
    }

    @Override
    public ApplyPost findByRecruitmentAndUser(Recruitment recruitment, User user) {
        List<ApplyPost> applyPostList = findAll();
        for (ApplyPost a : applyPostList){
            if (a.getRecruitment().getId() == recruitment.getId() && a.getUser().getId() == user.getId()) {
                return a;
            }
        }
        return null; // Trả về null nếu không tìm thấy ApplyPost tương ứng
    }

    @Override
    public List<ApplyPost> findByRecruitmentId(int id) {
        return applyPostRepository.findByRecruitmentId(id);
    }
}

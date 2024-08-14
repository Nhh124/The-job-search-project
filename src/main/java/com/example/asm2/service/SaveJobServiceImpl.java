package com.example.asm2.service;

import com.example.asm2.entity.Recruitment;
import com.example.asm2.entity.SaveJob;
import com.example.asm2.entity.User;
import com.example.asm2.repository.SaveJobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SaveJobServiceImpl implements SaveJobService {

    private SaveJobRepository saveJobRepository;

    @Autowired
    public SaveJobServiceImpl(SaveJobRepository saveJobRepository) {
        this.saveJobRepository = saveJobRepository;
    }

    @Override
    public List<SaveJob> findAll() {
        return saveJobRepository.findAll();
    }

    @Override
    public void saveOrUpdate(SaveJob saveJob) {
        saveJobRepository.save(saveJob);
    }

    @Override
    public void delete(SaveJob saveJob) {
        saveJobRepository.delete(saveJob);
    }

    @Override
    public SaveJob findByUserAndRe(User user, Recruitment recruitment) {
        return saveJobRepository.findByUserIdAndRecruitmentId(user.getId(),recruitment.getId());
    }

    @Override
    public void removeFollowJob(SaveJob existsJob) {
        saveJobRepository.delete(existsJob);
    }

    @Override
    public void addFollowJob(User user, Recruitment recruitment) {
        SaveJob saveJob = new SaveJob(recruitment,user);
        saveJobRepository.save(saveJob);
    }

    @Override
    public List<SaveJob> findAllByUserId(int id) {
       return saveJobRepository.findByUserId(id);
    }

    @Override
    public SaveJob findById(int id) {
        return saveJobRepository.findById(id);
    }
}

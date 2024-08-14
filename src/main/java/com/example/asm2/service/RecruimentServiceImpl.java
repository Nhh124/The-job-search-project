package com.example.asm2.service;

import com.example.asm2.entity.Recruitment;
import com.example.asm2.repository.RecruimentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class RecruimentServiceImpl implements RecruimentService{

    private RecruimentRepository recruimentRepository;

    @Autowired
    public RecruimentServiceImpl(RecruimentRepository recruimentRepository) {
        this.recruimentRepository = recruimentRepository;
    }

    @Override
    public List<Recruitment> findAll() {
        return recruimentRepository.findAll();
    }

    @Override
    public Recruitment findById(int id) {
        return recruimentRepository.findById(id);
    }

    @Override
    public void saveOrUpdate(Recruitment recruitment) {
        recruimentRepository.save(recruitment);
    }

    @Override
    public void delete(Recruitment recruitment) {
        recruimentRepository.delete(recruitment);
    }


    @Override
    public List<Recruitment> findByUserId(int id) {
        return recruimentRepository.findByCompanyUserId(id);
    }

    @Override
    public List<Recruitment> findRelated(Recruitment recruitment) {
        List<Recruitment> recruitmentList = findAll();
        List<Recruitment> listRelated = new ArrayList<>();

        for (Recruitment checkRecruitment : recruitmentList) {
            if (recruitment.getCompany().getCompanyName().equals(checkRecruitment.getCompany().getCompanyName())
                    && recruitment.getId() !=(checkRecruitment.getId())) { // Kiểm tra không thêm vào list nếu có cùng ID
                listRelated.add(checkRecruitment);
            }
        }

        return listRelated;
    }

    @Override
    public List<Recruitment> findByTitle(String keyword) {
        return recruimentRepository.findByTitle(keyword);
    }

    @Override
    public List<Recruitment> findByNameCompany(String keyword) {
        return recruimentRepository.findByNameCompany(keyword);
    }

    @Override
    public List<Recruitment> findByAddress(String keyword) {
        return recruimentRepository.findByAddress(keyword);
    }

    @Override
    public List<Recruitment> findByNewDate() {
        List<Recruitment> recruitmentList = recruimentRepository.findAllOrderByCreatedAtDesc();
        List<Recruitment> top5Recruitments = new ArrayList<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date();
        String currentDateString = dateFormat.format(currentDate);

        int count = 0;
        for (Recruitment recruitment : recruitmentList) {
            String createdAtString = recruitment.getCreatedAt(); // Lấy ngày tạo của Recruitment dưới dạng String

            if (createdAtString.equals(currentDateString)) {
                top5Recruitments.add(recruitment);
                count++;
            }

            if (count == 5) {
                break; // Nếu đã tìm được 5 Recruitment gần nhất, thoát khỏi vòng lặp
            }
        }

        return top5Recruitments;
    }
}

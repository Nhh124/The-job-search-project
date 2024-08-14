package com.example.asm2.service;

import com.example.asm2.entity.Cv;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface CvService {

    Cv findByUserId(int id);

    void deleteCv(Cv cv);

    void saveOrUpdateCv(Cv cv);

    String uploadCv(MultipartFile file, Cv cv, int userId);
}

package com.example.asm2.service;

import com.example.asm2.entity.Cv;
import com.example.asm2.repository.CvRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class CvServiceImpl implements CvService{

    private final CvRepository cvRepository;

    @Autowired
    public CvServiceImpl(CvRepository cvRepository) {
        this.cvRepository = cvRepository;
    }

    @Override
    public Cv findByUserId(int id) {
        return cvRepository.findByUserId(id);
    }

    @Override
    public void deleteCv(Cv cv) {
         cvRepository.delete(cv);
    }

    @Override
    public void saveOrUpdateCv(Cv cv) {
       cvRepository.save(cv);
    }

    @Override
    public String uploadCv(MultipartFile file, Cv cv, int userId) {
        if (file != null && !file.isEmpty()) {
            String filename = StringUtils.cleanPath(file.getOriginalFilename());
            if (!file.getContentType().equals("application/pdf")) {
                return "false";
            }

            String uploadPath = "src/main/resources/static/uploads";
            Path directory = Paths.get(uploadPath);

            try {
                if (!Files.exists(directory)) {
                    Files.createDirectories(directory);
                }

                Path filePath = directory.resolve(filename);
                Files.write(filePath, file.getBytes());

                if (cv == null){
                    cv = new Cv();
                }

                cv.setFileName(filename);
                cv.setUserId(userId);
                cvRepository.save(cv);

                return filename;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return "Error";
    }
}

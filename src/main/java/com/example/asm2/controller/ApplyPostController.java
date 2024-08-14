package com.example.asm2.controller;

import com.example.asm2.entity.ApplyPost;
import com.example.asm2.entity.Cv;
import com.example.asm2.entity.Recruitment;
import com.example.asm2.entity.User;
import com.example.asm2.service.ApplyPostService;
import com.example.asm2.service.CvService;
import com.example.asm2.service.RecruimentService;
import com.example.asm2.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/user")
public class ApplyPostController {

    // Lấy ngày hiện tại
    private LocalDate currentDate = LocalDate.now();

    // Định dạng ngày thành chuỗi theo định dạng yyyy-MM-dd
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private ApplyPostService applyPostService;

    private CvService cvService;

    private UserService userService;

    private RecruimentService recruimentService;

    @Autowired
    public ApplyPostController(ApplyPostService applyPostService,
                               CvService cvService, UserService userService,
                               RecruimentService recruimentService) {
        this.applyPostService = applyPostService;
        this.cvService = cvService;
        this.userService = userService;
        this.recruimentService = recruimentService;
    }

    @PostMapping("/apply-job")
    public ResponseEntity<String> applyJob(@RequestParam("file") MultipartFile file,
                                           @RequestParam("userId") int userId,
                                           @RequestParam("idRe") String idRe,
                                           @RequestParam("text") String text,
                                           HttpSession session,
                                           Model theModel) {

        Recruitment recruitment = recruimentService.findById(Integer.parseInt(idRe));
        Cv cv = cvService.findByUserId(userId);
        User sessionUser = (User) session.getAttribute("user");
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Bạn cần phải chọn cv!");
        }
        if (sessionUser == null){
            return ResponseEntity.ok("accountNull");
        }

        cvService.uploadCv(file, cv, userId);
        Cv existingCv = cvService.findByUserId(userId);
        ApplyPost existingApplyPost = applyPostService.findByRecruitmentAndUser(recruitment, sessionUser);
        theModel.addAttribute("session",sessionUser);
        if (existingApplyPost != null) {
            return ResponseEntity.ok("false"); // Trả về "false" nếu người dùng đã ứng tuyển công việc này trước đó
        }else {
            ApplyPost applyPost = new ApplyPost(formatter.format(currentDate), recruitment,
                    sessionUser, existingCv.getFileName(), 0, text);
            applyPostService.saveOrUpdate(applyPost);

            return ResponseEntity.ok("true"); // Trả về "true" nếu ứng tuyển thành công
        }

    }

    @PostMapping("/apply-job1")
    public ResponseEntity<String> applyJob1(@RequestParam("idRe") String idRe,
                                            @RequestParam("userId") int userId,
                                            HttpSession session,
                                            Model theModel,
                                            @RequestParam("text") String text) {

        // Lấy thông tin người dùng từ session
        User sessionUser = (User) session.getAttribute("user");

        Cv cv = cvService.findByUserId(sessionUser.getId());
        String cvName = "";

        if (cv != null) {
             cvName = cv.getFileName();
            if (sessionUser == null) {
                return ResponseEntity.ok("accountNull"); // Trả về "false" nếu người dùng chưa đăng nhập
            }
            theModel.addAttribute("session",sessionUser);
            try {
                Recruitment recruitment = recruimentService.findById(Integer.parseInt(idRe));

                if (recruitment == null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error"); // Trả về lỗi nếu không tìm thấy công việc
                }

                ApplyPost existingApplyPost = applyPostService.findByRecruitmentAndUser(recruitment, sessionUser);
                if (existingApplyPost != null) {
                    return ResponseEntity.ok("exist"); // Trả về "exist" nếu người dùng đã ứng tuyển công việc này trước đó
                }

                // Thực hiện lưu thông tin ứng tuyển vào CSDL
                ApplyPost applyPost = new ApplyPost(formatter.format(currentDate), recruitment,
                        sessionUser, cvName, 0, text);
                applyPostService.saveOrUpdate(applyPost);

                return ResponseEntity.ok("true"); // Trả về "true" nếu ứng tuyển thành công

            } catch (NumberFormatException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error"); // Trả về lỗi nếu idRe không hợp lệ
            }
        }else {
            return ResponseEntity.ok("false");
        }
    }

    @GetMapping("/approve/{userId}/{recruitmentId}")
    public String approveCv(@PathVariable("userId") int userId,
                            @PathVariable("recruitmentId") int idRe,
                            HttpSession session,
                            Model theModel){
        Recruitment recruitment = recruimentService.findById(idRe);
        theModel.addAttribute("recruitment", recruitment);

        User sessionUser = recruitment.getCompany().getUser();
        if (sessionUser.getRole().getId() == 0) {
            List<ApplyPost> applyPosts = applyPostService.findAll();
            theModel.addAttribute("applyPosts", applyPosts);
        }else {
            List<Recruitment> recruitmentList = recruimentService.findRelated(recruitment);
            theModel.addAttribute("listRelated",recruitmentList);
        }

        User user = userService.findById(userId);
        ApplyPost applyPost = applyPostService.findByRecruitmentAndUser(recruitment,user);

        applyPost.setStatus(1);
        applyPostService.saveOrUpdate(applyPost);
        theModel.addAttribute("session",sessionUser);
        theModel.addAttribute("success","Duyệt thành công!");
        return "public/detail-post";

    }




}

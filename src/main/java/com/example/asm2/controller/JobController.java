package com.example.asm2.controller;

import com.example.asm2.entity.*;
import com.example.asm2.service.ApplyPostService;
import com.example.asm2.service.RecruimentService;
import com.example.asm2.service.SaveJobService;
import com.example.asm2.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/save-job")
public class JobController {

    UserService userService;

    ApplyPostService applyPostService;

    RecruimentService recruimentService;

    SaveJobService saveJobService;

    @Autowired
    public JobController(UserService userService, ApplyPostService applyPostService,
                         RecruimentService recruimentService, SaveJobService saveJobService) {
        this.userService = userService;
        this.applyPostService = applyPostService;
        this.recruimentService = recruimentService;
        this.saveJobService = saveJobService;
    }

    @PostMapping("/save/")
    @ResponseBody
    public String saveJob(@RequestParam("idRe") String idRe, @RequestParam("userId") int userId) {
        try {
            User user = userService.findById(userId);
            Recruitment recruitment = recruimentService.findById(Integer.parseInt(idRe));
            SaveJob existsSaveJob = saveJobService.findByUserAndRe(user,recruitment);
            if (user != null) {

                if (existsSaveJob != null) {
                    saveJobService.delete(existsSaveJob);
                    return "unfollowed";
                } else {
                    saveJobService.addFollowJob(user,recruitment);
                    return "followed";
                }
            } else {
                return "error"; // Trả về "error" nếu không tìm thấy user
            }
        } catch (Exception e) {
            return "error"; // Trả về "error" nếu có lỗi xảy ra trong quá trình xử lý
        }
    }

    @GetMapping("/get-list")
    public String showSaveJob(Model theModel,
                              @RequestParam(value = "page", defaultValue = "1") int pageNumber,
                              HttpSession session){

        User sessionUser = (User) session.getAttribute("user");
        List<SaveJob> saveJobs = saveJobService.findAllByUserId(sessionUser.getId());
        int pageSize = 5; // Số lượng mục trên mỗi trang

        // Tính toán số trang
        int totalItems = saveJobs.size();
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);

        // Xác định vị trí bắt đầu và kết thúc mục trên từng trang
        int startIndex = (pageNumber - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, totalItems);

        List<SaveJob> saveJobList;
        if (startIndex < totalItems) {
            saveJobList = saveJobs.subList(startIndex, endIndex); // Lấy sublist cho trang hiện tại
        } else {
            saveJobList = Collections.emptyList();
        }

        theModel.addAttribute("saveJobList", saveJobList);
        theModel.addAttribute("pageNumber", pageNumber);
        theModel.addAttribute("totalPages", totalPages);
        theModel.addAttribute("session", sessionUser);

        return "public/list-save-job";
    }

    @RequestMapping(value = "/delete/{id}", method = {RequestMethod.GET, RequestMethod.POST})
    public String deleteSaveJob(@PathVariable("id") int id, Model theModel) {
        SaveJob saveJob = saveJobService.findById(id);
        if (saveJob == null) {
            return "errors/null-exception"; // trả về trang lỗi khác
        }

        // Thực hiện logic xóa saveJob
        saveJobService.delete(saveJob);
        theModel.addAttribute("success","Xóa thành công!");

        return "redirect:/save-job/get-list"; // Điều hướng sau khi xóa thành công
    }
}

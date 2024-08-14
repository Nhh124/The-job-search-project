package com.example.asm2.controller;

import com.example.asm2.entity.*;
import com.example.asm2.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/recruitment")
public class RecruitmentController {
    // Lấy ngày hiện tại
   private LocalDate currentDate = LocalDate.now();

    // Định dạng ngày thành chuỗi theo định dạng yyyy-MM-dd
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private String formattedDate = currentDate.format(formatter);
    private RecruimentService recruimentService;

    private CategoryService categoryService;

    private CompanyService companyService;

    private ApplyPostService applyPostService;

    private SaveJobService saveJobService;

    @Autowired
    public RecruitmentController(RecruimentService recruimentService,
                                 CategoryService categoryService,
                                 CompanyService companyService, ApplyPostService applyPostService,
                                 SaveJobService saveJobService) {
        this.recruimentService = recruimentService;
        this.categoryService = categoryService;
        this.companyService = companyService;
        this.applyPostService = applyPostService;
        this.saveJobService = saveJobService;
    }

    @GetMapping("/post")
    public String showRecruitment(HttpSession session, Model theModel){

        User user = (User) session.getAttribute("user");
        List<Category> categoryList = categoryService.findAll();

        Recruitment recruitment = new Recruitment();
        theModel.addAttribute("categories",categoryList);
        theModel.addAttribute("recruitment",recruitment);
        theModel.addAttribute("session", user);
        return "public/post-job";
    }

    @GetMapping("/update/{id}")
    public String showUpdateRecruitment(@PathVariable("id") int id,HttpSession session, Model theModel){

        User user = (User) session.getAttribute("user");
        List<Category> categoryList = categoryService.findAll();

        Recruitment recruitment = recruimentService.findById(id);
        theModel.addAttribute("categories",categoryList);
        theModel.addAttribute("recruitment",recruitment);
        theModel.addAttribute("session", user);
        return "public/edit-job";
    }

    @PostMapping("/add")
    public String addRecruitment(@ModelAttribute("recruitment") Recruitment recruitment,
                                 HttpSession session, Model theModel) {
        User user = (User) session.getAttribute("user");
        Company company = companyService.findByUserId(user.getId());

        if (company == null){
            return "public/post-job";
        } else {
            recruitment.setCreatedAt(formattedDate);
            recruitment.setCompany(company);
            recruimentService.saveOrUpdate(recruitment);
        }

        session.setAttribute("session", user);
        return "public/post-job";
    }

    @PostMapping("/edit")
    public String updateRecruitment(@RequestParam("id") int id,
                                    @RequestParam("category_id") int categoryId,
                                    @ModelAttribute("recruitment") Recruitment recruitment,
                                    HttpSession session, Model theModel) {
        User user = (User) session.getAttribute("user");
        Company company = companyService.findByUserId(user.getId());
        Category category = categoryService.findById(categoryId);

        if (company == null || category == null) {
            return "public/post-job";
        } else {
            recruitment.setId(id);
            recruitment.setCreatedAt(formattedDate);
            recruitment.setCompany(company);
            recruitment.setCategory(category);
            recruimentService.saveOrUpdate(recruitment);
        }

        session.setAttribute("session", user);
        return "public/edit-job";
    }

    @PostMapping("/delete")
    public String deleteRecruitment(@RequestParam("id") int id,Model theModel,
                                    @RequestParam(value = "page", defaultValue = "1") int pageNumber,
                                    HttpSession session){

        User sessionUser = (User) session.getAttribute("user");
        Recruitment recruitment = recruimentService.findById(id);
        int pageSize = 5; // Số lượng mục trên mỗi trang
        List<Recruitment> recruitmentList = recruimentService.findByUserId(sessionUser.getId()); // Lấy danh sách tất cả các Recruitment
        List<ApplyPost> applyPostList = applyPostService.findByRecruitmentId(recruitment.getId());

        if (!applyPostList.isEmpty()){
            theModel.addAttribute("error","Bài đăng đang có người ứng tuyển!");
        }else {
            recruimentService.delete(recruitment);
            recruitmentList = recruimentService.findByUserId(sessionUser.getId());
            theModel.addAttribute("success","Thành công!");
        }

        // Tính toán số trang
        int totalItems = recruitmentList.size();
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);

        // Xác định vị trí bắt đầu và kết thúc mục trên từng trang
        int startIndex = (pageNumber - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, totalItems);

        List<Recruitment> visibleRecruitments;
        if (startIndex < totalItems) {
            visibleRecruitments = recruitmentList.subList(startIndex, endIndex); // Lấy sublist cho trang hiện tại
        } else {
            visibleRecruitments = Collections.emptyList();
        }

        theModel.addAttribute("visibleRecruitments", visibleRecruitments);
        theModel.addAttribute("pageNumber", pageNumber);
        theModel.addAttribute("totalPages", totalPages);
        theModel.addAttribute("session", sessionUser);

        return "redirect:/user/list-post";
    }


    @GetMapping("/detail/{id}")
    public String showRecruitmentDetail(@PathVariable("id") int id, Model theModel, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        try {
            Recruitment recruitment = recruimentService.findById(id);
            theModel.addAttribute("recruitment", recruitment);
            ApplyPost applyPost = applyPostService.findByRecruitmentAndUser(recruitment, sessionUser);

            if (sessionUser.getRole().getId() == 0) {
                List<ApplyPost> applyPosts = applyPostService.findAll();
                List<Recruitment> recruitmentList = recruimentService.findRelated(recruitment);
                theModel.addAttribute("listRelated", recruitmentList);
                theModel.addAttribute("applyPosts", applyPosts);
            } else {

                theModel.addAttribute("existsApplyPost", applyPost);
                List<Recruitment> recruitmentList = recruimentService.findRelated(recruitment);
                theModel.addAttribute("listRelated", recruitmentList);
            }

        } catch (Exception e) {

            e.printStackTrace();
            return "errors/exception";
        }

        theModel.addAttribute("session", sessionUser);
        return "public/detail-post";
    }

    @GetMapping("/list-re")
    public String showRecruitmentDetail(Model theModel, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        List<SaveJob> saveJobs = saveJobService.findAllByUserId(sessionUser.getId());
        theModel.addAttribute("saveJobList",saveJobs);

        return "public/list-re";

    }

    @RequestMapping(value = "/search")
    public String searchRecruitment(@RequestParam("keySearch") String keyword, Model theModel,
                                    @RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
                                    HttpSession session){

        User sessionUser = (User) session.getAttribute("user");

        int pageSize = 5; // Số lượng mục trên mỗi trang
        List<Recruitment> recruitmentList = recruimentService.findByTitle(keyword);
        // Tính toán số trang
        int totalItems = recruitmentList.size();
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);

        // Xác định vị trí bắt đầu và kết thúc mục trên từng trang
        int startIndex = (pageNumber - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, totalItems);

        List<Recruitment> visibleRecruitments;
        if (startIndex < totalItems) {
            visibleRecruitments = recruitmentList.subList(startIndex, endIndex); // Lấy sublist cho trang hiện tại
        } else {
            visibleRecruitments = Collections.emptyList();
        }

        theModel.addAttribute("visibleRecruitments", visibleRecruitments);
        theModel.addAttribute("pageNumber", pageNumber);
        theModel.addAttribute("totalPages", totalPages);
        theModel.addAttribute("session", sessionUser);

//       theModel.addAttribute("recruitmentList",recruitmentList);

        return "public/result-search"; // Return view to display search results
    }

    @RequestMapping(value = "/company-search")
    public String searchCompany(@RequestParam("keySearch") String keyword, Model theModel,
                                    @RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
                                    HttpSession session){

        User sessionUser = (User) session.getAttribute("user");

        int pageSize = 5; // Số lượng mục trên mỗi trang
        List<Recruitment> recruitmentList = recruimentService.findByNameCompany(keyword);
        // Tính toán số trang
        int totalItems = recruitmentList.size();
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);

        // Xác định vị trí bắt đầu và kết thúc mục trên từng trang
        int startIndex = (pageNumber - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, totalItems);

        List<Recruitment> visibleRecruitments;
        if (startIndex < totalItems) {
            visibleRecruitments = recruitmentList.subList(startIndex, endIndex); // Lấy sublist cho trang hiện tại
        } else {
            visibleRecruitments = Collections.emptyList();
        }

        theModel.addAttribute("visibleRecruitments", visibleRecruitments);
        theModel.addAttribute("pageNumber", pageNumber);
        theModel.addAttribute("totalPages", totalPages);
        theModel.addAttribute("session", sessionUser);

//       theModel.addAttribute("recruitmentList",recruitmentList);

        return "public/result-search-company"; // Return view to display search results
    }

    @RequestMapping(value = "/search-address")
    public String searchAddress(@RequestParam("keySearch") String keyword, Model theModel,
                                @RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
                                HttpSession session){

        User sessionUser = (User) session.getAttribute("user");

        int pageSize = 5; // Số lượng mục trên mỗi trang
        List<Recruitment> recruitmentList = recruimentService.findByAddress(keyword);
        // Tính toán số trang
        int totalItems = recruitmentList.size();
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);

        // Xác định vị trí bắt đầu và kết thúc mục trên từng trang
        int startIndex = (pageNumber - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, totalItems);

        List<Recruitment> visibleRecruitments;
        if (startIndex < totalItems) {
            visibleRecruitments = recruitmentList.subList(startIndex, endIndex); // Lấy sublist cho trang hiện tại
        } else {
            visibleRecruitments = Collections.emptyList();
        }

        theModel.addAttribute("visibleRecruitments", visibleRecruitments);
        theModel.addAttribute("pageNumber", pageNumber);
        theModel.addAttribute("totalPages", totalPages);
        theModel.addAttribute("session", sessionUser);

//       theModel.addAttribute("recruitmentList",recruitmentList);

        return "public/result-search-address"; // Return view to display search results
    }
}

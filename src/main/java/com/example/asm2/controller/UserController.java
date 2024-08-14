package com.example.asm2.controller;

import com.example.asm2.entity.*;
import com.example.asm2.service.*;
import jakarta.mail.MessagingException;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/user")
public class UserController {

    
    private MailService emailService;

    
    private CompanyService companyService;

    
    private CvService cvService;
    
    private UserService userService;

    private ApplyPostService applyPostService;
    
    private RecruimentService recruimentService;
    
    private FollowCompanyService followCompanyService;

    @Autowired
    public UserController(MailService emailService, CompanyService companyService,
                          CvService cvService, UserService userService,
                          ApplyPostService applyPostService,
                          RecruimentService recruimentService,
                          FollowCompanyService followCompanyService) {
        this.emailService = emailService;
        this.companyService = companyService;
        this.cvService = cvService;
        this.userService = userService;
        this.applyPostService = applyPostService;
        this.recruimentService = recruimentService;
        this.followCompanyService = followCompanyService;
    }

    @GetMapping("/profile/{userId}")
    public String userProfile(@PathVariable("userId") int userId, Model model) {
        User user = userService.findById(userId);
        Company company = companyService.findByUserId(userId);
        Cv cv = cvService.findByUserId(userId);

        model.addAttribute("userId", userId);
        model.addAttribute("userInformation", user);

        model.addAttribute("Cv",cv);
        model.addAttribute("companyInformation", company);

        return "public/profile";
    }

    @PostMapping("/update-company/{userId}")
    public String saveOrUpdateCompany(
                                      @PathVariable("userId") String userId, Model model,
                                      @ModelAttribute("companyInformation") Company companyInformation
                                     ) {
        User user = userService.findById(Integer.parseInt(userId));

        if (companyInformation.getLogo() == null) {
            model.addAttribute("msg", "Logo is missing");
        } else {
            companyInformation.setUser(user);
            companyService.saveOrUpdate(companyInformation);
            model.addAttribute("msg", "Update Successfully!!");
        }
        return "redirect:/user/profile/" + userId;
    }

    @PostMapping("/update-profile/{userId}")
    public String updateProfile(@PathVariable("userId") int userId,
                                Model themodel,
                                @ModelAttribute("userInformation") User userInformation) {
        User user = userService.findById(userId);

        if (user != null) {
            Cv cv = user.getCv();
            if (cv != null) {
                userInformation.setCv(cv);
            }

            String image = user.getImage();
            if (image != null && !image.isEmpty()) {
                userInformation.setImage(image);
            }
            themodel.addAttribute("msg","Update SuccessFully");
            userService.updateUser(userInformation);
        }

        // Chuyển hướng tới trang thông tin người dùng đã cập nhật
        return "redirect:/user/profile/" + userId;
    }

    
    @PostMapping("/register")
    public String register(User user, HttpServletRequest request) throws MessagingException {

        String token = UUID.randomUUID().toString();
        String confirmationLink = request.getRequestURL().toString() + "?token=" + token;

        return "public/profile";
    }

    @PostMapping("/upload/{userId}")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file,
                                              @PathVariable("userId") String userId) {
        User user = userService.findById(Integer.parseInt(userId));

        if (file != null && !file.isEmpty()) {
            String filename = StringUtils.cleanPath(file.getOriginalFilename());
            String uploadPath = "src/main/resources/static/images/avatar/";
            Path directory = Paths.get(uploadPath);

            try {
                if (!Files.exists(directory)) {
                    Files.createDirectories(directory);
                }

                Path filePath = directory.resolve(filename);
                Files.write(filePath, file.getBytes());

                user.setImage(filename);
                userService.updateUser(user);

                String imageURL = "/images/avatar/" + filename;

                return ResponseEntity.ok().body(imageURL);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
    }

    @PostMapping("/upload-company/{userId}")
    public ResponseEntity<String> uploadLogo(@RequestParam("file") MultipartFile file,
                                             @PathVariable("userId") String userId) {
        Company company = companyService.findByUserId(Integer.parseInt(userId));

        if (file != null && !file.isEmpty() && company != null) {
            String filename = StringUtils.cleanPath(file.getOriginalFilename());
            String uploadPath = "src/main/resources/static/images/";
            Path directory = Paths.get(uploadPath);

            try {
                if (!Files.exists(directory)) {
                    Files.createDirectories(directory);
                }

                Path filePath = directory.resolve(filename);
                Files.write(filePath, file.getBytes());

                company.setLogo(filename);
                companyService.saveOrUpdate(company);

                String imageURL = "/images/company-logo/" + filename;

                return ResponseEntity.ok().body(imageURL);
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while uploading file");
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: Invalid file or company not found");
    }

    @PostMapping("/uploadCv/{userId}")
    public ResponseEntity<String> handleCvUpload(@RequestParam("file") MultipartFile file,
                                                 @PathVariable("userId") String userId) {
        Cv cv = cvService.findByUserId(Integer.parseInt(userId));
        String response = cvService.uploadCv(file, cv,Integer.parseInt(userId));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/list-post")
    public String listPosts(@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
                            HttpSession session,
                            Model theModel) {
        User sessionUser = (User) session.getAttribute("user");
        int pageSize = 5; // Số lượng mục trên mỗi trang
        List<Recruitment> recruitmentList = recruimentService.findByUserId(sessionUser.getId()); // Lấy danh sách tất cả các Recruitment

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

        return "public/post-list";
    }

    @GetMapping("/detail-company/{id}")
    public String showCompanyDetail(@PathVariable("id") int id, Model theModel,HttpSession session) {
        Company company = companyService.findById(id);
        User sessionUser = (User) session.getAttribute("user");
        FollowCompany followCompany = followCompanyService.findFollowByUserId(sessionUser.getId());
        if (company == null) {
            // Xử lý khi không tìm thấy công ty
            return "errors/null-exception";
        }
        theModel.addAttribute("followCompany",followCompany);
        theModel.addAttribute("userId",sessionUser.getId());
        theModel.addAttribute("session", sessionUser);
        theModel.addAttribute("company", company);
        return "public/detail-company";
    }

    @PostMapping("/toggle-follow-company/{userId}/{idCompany}")
    @ResponseBody
    public String toggleFollowCompany(@PathVariable("userId") int userId,
                                      @PathVariable("idCompany") int idCompany) {
        try {
            User user = userService.findById(userId);
            Company company = companyService.findById(idCompany);

            // Kiểm tra xem đã theo dõi chưa
            FollowCompany existingFollow = followCompanyService.findFollowByUserAndCompany(user,company);

            if(existingFollow != null) {
                // Nếu đã theo dõi, thực hiện xóa follow
                followCompanyService.removeFollowCompany(existingFollow);
                return "unfollowed";
            } else {
                // Nếu chưa theo dõi, thêm mới FollowCompany
                followCompanyService.addFollowCompany(user, company);
                return "followed";
            }
        } catch (Exception e) {
            // Trả về chuỗi "error" nếu xảy ra lỗi trong quá trình xử lý
            return "error";
        }
    }

    @GetMapping("/list-follow-company")
    public String showFollowCompany(@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
                                    HttpSession session,
                                    Model theModel) {
        User sessionUser = (User) session.getAttribute("user");
        int pageSize = 5; // Số lượng mục trên mỗi trang
        List<FollowCompany> followCompanyList =  followCompanyService.findALlFollowByUserId(sessionUser.getId()); // Lấy danh sách tất cả các Recruitment

        // Tính toán số trang
        int totalItems = followCompanyList.size();
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);

        // Xác định vị trí bắt đầu và kết thúc mục trên từng trang
        int startIndex = (pageNumber - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, totalItems);

        List<FollowCompany> visibleFlCompany;
        if (startIndex < totalItems) {
            visibleFlCompany = followCompanyList.subList(startIndex, endIndex); // Lấy sublist cho trang hiện tại
        } else {
            visibleFlCompany = Collections.emptyList();
        }

        theModel.addAttribute("ListFollowCompany", visibleFlCompany);
        theModel.addAttribute("pageNumber", pageNumber);
        theModel.addAttribute("totalPages", totalPages);

        return "public/list-follow-company";
    }

    @RequestMapping(value = "/delete-follow/{id}", method = {RequestMethod.GET, RequestMethod.POST})
    public String deleteSaveJob(@PathVariable("id") int id, Model theModel) {
        FollowCompany followCompany = followCompanyService.findById(id);
        if (followCompany == null) {
            return "errors/null-exception"; // trả về trang lỗi khác
        }

        // Thực hiện logic xóa saveJob
        followCompanyService.delete(followCompany);
        theModel.addAttribute("success","Xóa thành công!");

        return "redirect:/user/list-follow-company"; // Điều hướng sau khi xóa thành công
    }

//    @GetMapping("/list-candidate")
//    public String listCandidate(@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
//                                HttpSession session,
//                                Model theModel) {
//        User sessionUser = (User) session.getAttribute("user");
//        int pageSize = 5; // Số lượng mục trên mỗi trang
//        List<ApplyPost> applyPostList = applyPostService.findByRecruitmentId()
//        // Tính toán số trang
//        int totalItems = followCompanyList.size();
//        int totalPages = (int) Math.ceil((double) totalItems / pageSize);
//
//        // Xác định vị trí bắt đầu và kết thúc mục trên từng trang
//        int startIndex = (pageNumber - 1) * pageSize;
//        int endIndex = Math.min(startIndex + pageSize, totalItems);
//
//        List<FollowCompany> visibleFlCompany;
//        if (startIndex < totalItems) {
//            visibleFlCompany = followCompanyList.subList(startIndex, endIndex); // Lấy sublist cho trang hiện tại
//        } else {
//            visibleFlCompany = Collections.emptyList();
//        }
//
//        theModel.addAttribute("ListFollowCompany", visibleFlCompany);
//        theModel.addAttribute("pageNumber", pageNumber);
//        theModel.addAttribute("totalPages", totalPages);
//
//        return "public/list-user";
//    }
}
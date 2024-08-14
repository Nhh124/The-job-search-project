package com.example.asm2.controller;


import com.example.asm2.entity.*;
import com.example.asm2.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class AuthController {

    private UserService userService;


    private HomeController homeController;


    private RoleService roleService;

    private CategoryService categoryService;

    private CompanyService companyService;

    private RecruimentService recruimentService;


    private ApplyPostService applyPostService;

    private  SaveJobService saveJobService;

    @Autowired
    public AuthController(UserService userService, HomeController homeController,
                          RoleService roleService, CategoryService categoryService,
                          CompanyService companyService, RecruimentService recruimentService,
                          ApplyPostService applyPostService,SaveJobService saveJobService) {
        this.userService = userService;
        this.homeController = homeController;
        this.roleService = roleService;
        this.categoryService = categoryService;
        this.companyService = companyService;
        this.recruimentService = recruimentService;
        this.applyPostService = applyPostService;
        this.saveJobService = saveJobService;
    }

    @GetMapping("/login")
    public String home(HttpSession session,Model theModel) {

        User sessionUser = (User) session.getAttribute("user");
        theModel.addAttribute("session", sessionUser);
        loadAll(theModel,0);
        return "public/login";

    }

    /**
     * Xử lý yêu cầu POST gửi đến "/login" để xác thực thông tin đăng nhập và điều hướng người dùng đến các trang tương ứng.
     *
     *
     * @param session   Đối tượng HttpSession để lưu trữ dữ liệu trạng thái người dùng.
     * @param theModel  Đối tượng Model để cung cấp dữ liệu cho View Template.
     * @return Trả về tên của View Template tương ứng với trang hợp lệ sau khi xử lý yêu cầu đăng nhập.
     *         Nếu đăng nhập thành công với vai trò là "admin", chuyển hướng người dùng đến "admin/home".
     *         Nếu đăng nhập thành công với vai trò là người dùng, chuyển hướng người dùng đến "public/home".
     *         Nếu thông tin đăng nhập không hợp lệ hoặc người dùng bị khóa, trả về trang "admin/login" với thông báo lỗi tương ứng.
     *         Trước khi chuyển hướng, tải dữ liệu người dùng và vai trò sử dụng phương thức loadAllData() trong commonController.
     */
    @PostMapping("/login")
    public String processLogin(@RequestParam("email") String email,
                               @RequestParam("password") String password,
                               HttpSession session,
                               Model theModel
    ) {
        User user = userService.getUserByEmail(email);

        if (user.getStatus() == 1){
            if (user.getPassword().equals(password)) {
                session.setAttribute("user", user);
                User sessionUser = (User) session.getAttribute("user");
                theModel.addAttribute("session",sessionUser);
            } else {
                //theModel.addAttribute("error", true);
            }
        } else {
            //theModel.addAttribute("errorLock", true);
        }


        loadAll(theModel,user.getId());
        return "public/home";
    }


    /**
     * Xử lý yêu cầu POST gửi đến "/save" để thêm người dùng vào hệ thống.
     *
     * @param user Đối tượng User chứa thông tin người dùng cần thêm.
     * @return Chuyển hướng người dùng đến trang "admin/account" với thông báo thành công hoặc thất bại.
     *         Nếu email hoặc số điện thoại đã tồn tại, chuyển hướng đến "admin/account?error=true".
     *         Ngược lại, thêm người dùng vào hệ thống, đặt trạng thái là 1 và chuyển hướng đến "admin/account?success=true".
     */
    @PostMapping("/register")
    public String addUser(@ModelAttribute("user") User user,
                          @RequestParam("rePassword") String rePassword,
                          Model theModel) {

        if (userService.isEmailExists(user.getEmail()) || userService.isPhoneNumberExists(user.getPhoneNumber())
            || !user.getPassword().equals(rePassword)
        ) {
            theModel.addAttribute("msg_register_error","Register Failed!");
        } else {

            theModel.addAttribute("msg_register_success","Register Successfully!");
            user.setStatus(0);
            user.setAddress("");
            user.setDescription("");
            user.setImage("");
            userService.addUser(user);
        }
        loadAll(theModel,0);
        return "public/login";
    }


    /**Xử lý yêu cầu GET gửi đến "/logout"
     *   để đăng xuất người dùng admin bằng cách xóa thuộc tính "admin" trong session
     *   và chuyển hướng người dùng đến trang đăng nhập "admin/login".
     */
    @GetMapping("/logout")
    public String logout(Model theModel,HttpSession session){
        session.invalidate(); // Xóa toàn bộ thông tin của session
        loadAll(theModel,0);
        return "public/login";
    }

    private void loadAll(Model theModel,int id){
        User user = new User();
        List<Role> listRole = roleService.findAll();
        if (id != 0){
            System.out.println(id);
            List<SaveJob> saveJobList = saveJobService.findAllByUserId(id);
            System.out.println(saveJobList.size());
            theModel.addAttribute("saveJobList",saveJobList);
        }
        List<Category> categories = categoryService.getCategoryListSortedByCount();
        List<Company> companies = companyService.findAll();
        List<Recruitment> recruitments = recruimentService.findAll();

        theModel.addAttribute("companies",companies);
        theModel.addAttribute("categories",categories);
        theModel.addAttribute("recruitments", recruitments);
        theModel.addAttribute("role",listRole);
        theModel.addAttribute("user",user);
    }

}

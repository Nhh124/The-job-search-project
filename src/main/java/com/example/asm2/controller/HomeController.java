package com.example.asm2.controller;


import com.example.asm2.entity.Category;
import com.example.asm2.entity.Company;
import com.example.asm2.entity.Recruitment;
import com.example.asm2.service.CategoryService;
import com.example.asm2.service.CompanyService;
import com.example.asm2.service.RecruimentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/home")
public class HomeController {

    private CategoryService categoryService;

    private CompanyService companyService;

    private RecruimentService recruimentService;

    public HomeController(CategoryService categoryService,
                          CompanyService companyService, RecruimentService recruimentService) {
        this.categoryService = categoryService;
        this.companyService = companyService;
        this.recruimentService = recruimentService;
    }

    @GetMapping("/")
    public String home(Model theModel){
        List<Category> categories = categoryService.getCategoryListSortedByCount();
        List<Company> companies = companyService.findAll();
        List<Recruitment> recruitments = recruimentService.findAll();
        theModel.addAttribute("companies",companies);
        theModel.addAttribute("categories",categories);
        theModel.addAttribute("recruitments", recruitments);
        return "public/home";
    }





}

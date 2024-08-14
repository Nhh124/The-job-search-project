package com.example.asm2.service;

import com.example.asm2.entity.Category;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {

    List<Category> findAll();

    Category findByName(String name);

    Category findById(int id);

    List<Object[]> getCategoryCounts();

    List<Category> getCategoryListSortedByCount();

    Category saveOrUpdate(Category category);
}

package com.example.asm2.repository;

import com.example.asm2.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Integer> {

    Category findByName(String name);

    Category findById(int id);
}

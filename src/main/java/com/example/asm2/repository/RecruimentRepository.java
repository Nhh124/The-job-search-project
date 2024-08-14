package com.example.asm2.repository;

import com.example.asm2.entity.Recruitment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecruimentRepository extends JpaRepository<Recruitment,Integer> {

    Recruitment findById(int id);

    List<Recruitment> findByCompanyUserId(int id);

    @Query("SELECT r FROM Recruitment r WHERE lower(r.title) LIKE %:keyword%")
    List<Recruitment> findByTitle(@Param("keyword") String keyword);

    @Query("SELECT r FROM Recruitment r JOIN r.company c WHERE lower(c.companyName) LIKE %:keyword%")
    List<Recruitment> findByNameCompany(@Param("keyword") String keyword);

    @Query("SELECT r FROM Recruitment r JOIN r.company c WHERE lower(c.address) LIKE %:keyword%")
    List<Recruitment> findByAddress(@Param("keyword") String keyword);

    @Query("SELECT r FROM Recruitment r ORDER BY r.createdAt DESC")
    List<Recruitment> findAllOrderByCreatedAtDesc();
}


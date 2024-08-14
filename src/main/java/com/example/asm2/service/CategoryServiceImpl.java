package com.example.asm2.service;

import com.example.asm2.entity.ApplyPost;
import com.example.asm2.entity.Category;
import com.example.asm2.entity.Recruitment;
import com.example.asm2.repository.CategoryRepository;
import com.example.asm2.repository.RecruimentRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CategoryServiceImpl implements CategoryService{

    private CategoryRepository categoryRepository;

    private ApplyPostService applyPostService;

    private RecruimentService recruimentService;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, ApplyPostService applyPostService,
                               RecruimentService recruimentService) {
        this.categoryRepository = categoryRepository;
        this.applyPostService = applyPostService;
        this.recruimentService = recruimentService;
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Lấy số lượng tuyển dụng trong mỗi danh mục dưới dạng một danh sách các mảng Object.
     * Phương thức này thực thi một truy vấn JPQL để trả về tên danh mục và số lượng tuyển dụng
     * liên quan với mỗi danh mục.
     *
     * @return Một danh sách các mảng Object, trong đó mỗi mảng chứa tên danh mục và số lượng
     *         tuyển dụng cho danh mục đó. Truy vấn được thực hiện bằng cách kết hợp các
     *         thực thể Category, Recruitment, và ApplyPost và nhóm kết quả theo tên danh mục.
     */
    @Override
    public List<Object[]> getCategoryCounts() {
        String jpql = "SELECT c.name, COUNT(r.category.id) " +
                "FROM Category c " +
                "JOIN c.recruitments r " +
                "JOIN r.applyPosts a " +
                "GROUP BY c.name";
        TypedQuery<Object[]> query = entityManager.createQuery(jpql, Object[].class);
        return query.getResultList();
    }

    @Override
    public List<Category> getCategoryListSortedByCount() {
        List<Object[]> categoryList = getCategoryCounts();

        List<Category> sortedCategories = new ArrayList<>();
        for (Object[] category : categoryList) {
            String categoryName = (String) category[0];
            Long categoryCount = (Long) category[1];

            if (categoryCount >= 3) { // Chỉ thêm những category có categoryCount >= 3
                Category c = findByName(categoryName);
                c.setNumberChoose(Math.toIntExact(categoryCount));
                saveOrUpdate(c);
                sortedCategories.add(c);
            }
        }

        sortedCategories.sort((c1, c2) -> Long.compare(c2.getCategoryCount(), c1.getCategoryCount()));

        return sortedCategories;
    }

    @Override
    public Category saveOrUpdate(Category category) {
        return categoryRepository.save(category);
    }


    @Override
    public Category findByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public Category findById(int id) {
        return categoryRepository.findById(id);
    }
}

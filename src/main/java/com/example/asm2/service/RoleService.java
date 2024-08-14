package com.example.asm2.service;


import com.example.asm2.entity.Role;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Chịu trách nhiệm xử lý logic kinh doanh liên quan đến Role.
 *
 * Interface này cung cấp các phương thức để thực hiện các tác vụ tương ứng với Role, như tìm kiếm, thêm mới, cập nhật, xóa, và kiểm tra sự tồn tại của ID.
 */
@Service
public interface RoleService {
    List<Role> findAll();

    Role findById(int id);
}

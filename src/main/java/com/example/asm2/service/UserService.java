package com.example.asm2.service;


import com.example.asm2.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Chịu trách nhiệm xử lý logic kinh doanh liên quan đến User.
 *
 * Interface này cung cấp các phương thức để thực hiện các tác vụ tương ứng với User, như tìm kiếm, thêm mới, cập nhật, xóa, và kiểm tra sự tồn tại của ID.
 */
@Service
public interface UserService {
    List<User> findAll();

    User getUserByEmail(String email);

    User addUser(User user);

    User updateUser(User user);

    void deleteUserById(int id);

    User findById(int userId);

    boolean isEmailExists(String email);

    boolean isPhoneNumberExists(String phoneNumber);

}

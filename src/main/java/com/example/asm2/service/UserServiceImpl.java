package com.example.asm2.service;


import com.example.asm2.entity.User;
import com.example.asm2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
        * Implement Service cho  UserService.
        *
        * Class này triển khai các phương thức được định nghĩa trong interface  UserService
        * và  UserRepository
        * để xử lý logic kinh doanh liên quan đến Donation.
        */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User addUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteUserById(int id) {
        userRepository.deleteById(id);
    }

    @Override
    public User findById(int userId) {
        return userRepository.findById(userId);
    }

    @Override
    public boolean isEmailExists(String email) {
        List<User> users = findAll();
        for (User user : users){
            if (user.getEmail().equals(email)){
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean isPhoneNumberExists(String phoneNumber) {
        List<User> users = findAll();
        for (User user : users){
            if (user.getPhoneNumber().equals(phoneNumber)){
                return true;
            }
        }
        return false;
    }


}

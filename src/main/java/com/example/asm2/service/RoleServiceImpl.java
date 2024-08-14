package com.example.asm2.service;


import com.example.asm2.entity.Role;
import com.example.asm2.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implement Service cho RoleService.
 *
 * Class này triển khai các phương thức được định nghĩa trong interface RoleService
 * và RoleRepository
 * để xử lý logic kinh doanh liên quan đến Donation.
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleRepository roleRepository;
    @Override
    public List<Role> findAll() {
        List<Role> roles = roleRepository.findAll();

        for (Role role : roles) {
            if (role.getRoleName().equalsIgnoreCase("admin")) {
                role.setRoleName("Nhà tuyển dụng");
            } else if (role.getRoleName().equalsIgnoreCase("user")) {
                role.setRoleName("Người ứng tuyển");
            }
        }

        return roles;
    }

    @Override
    public Role findById(int id) {
        return roleRepository.findById(id);
    }


}

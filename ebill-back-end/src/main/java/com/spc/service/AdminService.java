package com.spc.service;

import com.spc.entity.Admin;
import com.spc.entity.Customer;
import com.spc.repo.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    public AdminRepository adminRepository;


    public Admin createAdmin(Admin admin) {
        try {
            return adminRepository.save(admin);
        }
        catch (Exception e)
        {
            return null;
        }

    }

    public Optional<Admin> getAdminByUsername(String username) {
        return adminRepository.findByUserName(username);
    }

    public void deleteAdmin(String username) {
        adminRepository.delete(adminRepository.findByUserName(username).get());
    }

    public void saveAdmin(Admin admin) {
        adminRepository.save(admin);
    }

    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    public Admin authenticateCustomer(String username, String password) throws Exception {
        Optional<Admin> customerOpt = adminRepository.findByUserName(username);

        if (customerOpt.isPresent()) {
            Admin admin = customerOpt.get();
            if (admin.getPassword().equals(password)) {
                return admin;
            } else {
                throw new Exception("Invalid credential");
            }
        } else {
            throw new Exception("Not found customer");
        }
    }
}

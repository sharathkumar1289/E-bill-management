package com.spc.controller;

import com.spc.entity.Admin;
import com.spc.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/admin-login")
public class AdminLoginController {
    @Autowired
    public AdminService adminService;

    @PostMapping
    public ResponseEntity<Object> authenticateAdmin(@RequestBody Admin admin) {
        try {
            Admin authenticatedCustomer = adminService.authenticateCustomer(admin.getUserName(), admin.getPassword());
            return ResponseEntity.ok(
                    Map.of(
                            "message", "Login successful",
                            "Admin username", authenticatedCustomer.getUserName()
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    Map.of("message", "Login failed", "error", e.getMessage())
            );
        }
    }

}

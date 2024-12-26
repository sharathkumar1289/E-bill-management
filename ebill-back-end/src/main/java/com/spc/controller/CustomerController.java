//package com.spc.controller;
//
//import com.spc.entity.Customer;
//import com.spc.service.CustomerService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Optional;
//
//@RestController
//@RequestMapping("customers")
//public class CustomerController {
//
//
//    @Autowired
//    public CustomerService customerService;
//
//    @PostMapping
//    public ResponseEntity<String> createCustomer(@RequestBody Customer customer) {
//        try {
//            Customer savedCustomer = customerService.createCustomer(customer);
//            return ResponseEntity.status(201).body("success! Customer Created with ID"+ savedCustomer.getCustomerId());
//        }
//        catch (Exception e)
//        {
//            return new ResponseEntity<>("Customer not created", HttpStatus.BAD_REQUEST);
//        }
//    }
//
//    @PutMapping("/password/{id}")
//    public ResponseEntity<String> changePassword(@PathVariable Long id,@RequestParam String oldPassword, @RequestParam String newPassword) {
//
//        try{
//            String changePasswordResponse = customerService.changePassword(id,oldPassword,newPassword);
//            return ResponseEntity.status(201).body(changePasswordResponse);
//        }
//        catch (Exception e)
//        {
//            return new ResponseEntity<>("Password not changed", HttpStatus.BAD_REQUEST);
//        }
//    }
//
//}

package com.spc.controller;

import com.spc.entity.Customer;
import com.spc.entity.LoginRequest;
import com.spc.service.CustomerService;
import com.spc.utility.ChangePasswordBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;


    @PostMapping("/signup")
    public ResponseEntity<String> createCustomer(@RequestBody Customer customer) {
        try {
            Customer savedCustomer = customerService.createCustomer(customer);
            return ResponseEntity.status(HttpStatus.CREATED).body("Customer created successfully with ID: " + savedCustomer.getCustomerId());
        } catch (Exception e) {
            return new ResponseEntity<>("Error creating customer: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Object> authenticateCustomer(@RequestBody LoginRequest loginRequest) {
        try {
            Customer authenticatedCustomer = customerService.authenticateCustomer(loginRequest.getEmail(), loginRequest.getPassword());
            return ResponseEntity.ok(
                    Map.of(
                            "message", "Login successful",
                            "customerId", authenticatedCustomer.getCustomerId(),
                            "name", authenticatedCustomer.getName(),
                            "email", authenticatedCustomer.getEmail()
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    Map.of("message", "Login failed", "error", e.getMessage())
            );
        }
    }


    @GetMapping
    public ResponseEntity<List<Customer>> getAllAccounts() {
        List<Customer> accounts = customerService.getAllAccounts();
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Customer>> getCustomerbyId(@PathVariable Long id) {
        Optional<Customer> accounts = customerService.getCustomerbyId(id);
        return ResponseEntity.ok(accounts);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long id, @RequestBody Customer account) {
        Optional<Customer> existingAccount = customerService.getAccountById(id);
        if (existingAccount.isPresent()) {
            account.setCustomerId(id);
            Customer updatedAccount = customerService.saveAccount(account);
            return ResponseEntity.ok(updatedAccount);
        } else {
            return ResponseEntity.status(404).body(null);
        }
    }



    @PutMapping("/password/{id}")
    public ResponseEntity<String> changePassword(@PathVariable Long id, @RequestBody ChangePasswordBody changePasswordBody) {
        try {
            String response = customerService.changePassword(id, changePasswordBody.getOldPassword(), changePasswordBody.getNewPassword());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return new ResponseEntity<>("Password not changed: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}

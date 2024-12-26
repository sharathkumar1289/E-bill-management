//package com.spc.service;
//
//import com.spc.entity.Customer;
//import com.spc.repo.AccountRepository;
//import com.spc.repo.CustomerRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//
//@Service
//public class CustomerService{
//
//    @Autowired
//    public CustomerRepository customerRepository;
//
//    public Customer createCustomer(Customer customer) throws Exception {
//
//        Customer customer1;
//        try {
//            customer1 = customerRepository.save(customer);
//        }
//        catch (Exception e)
//        {
//            throw new Exception("Customer Not Created");
//        }
//        return customer1;
//    }
//
//    public String changePassword(Long id, String oldPassword, String newPassword) throws Exception {
//        Optional<Customer> optionalCustomer = customerRepository.findById(id);
//        if(optionalCustomer.isEmpty())
//            throw new Exception();
//        Customer customer = optionalCustomer.get();
//        if(customer.getPassword().equals(oldPassword))
//        {
//            customer.setPassword(newPassword);
//            customerRepository.save(customer);
//            return "Password Changed";
//        }
//        else
//        {
//            return "Incorrect Old Password";
//        }
//    }
//}



package com.spc.service;

import com.spc.entity.Customer;
import com.spc.repo.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;
    public Customer createCustomer(Customer customer) throws Exception {
        try {
            return customerRepository.save(customer);
        } catch (Exception e) {
            throw new Exception("Unable to create Customer");
        }
    }
    public List<Customer> getAllAccounts() {
        return customerRepository.findAll();
    }
    public Optional<Customer> getCustomerbyId(Long id) {
        return customerRepository.findCustomerByCustomerId(id);
    }


    public Customer saveAccount(Customer account) {
        return customerRepository.save(account);
    }


    public Optional<Customer> getAccountById(Long id) {
        return customerRepository.findById(id);
    }

    public Customer authenticateCustomer(String email, String password) throws Exception {
        Optional<Customer> customerOpt = customerRepository.findByEmail(email);

        if (customerOpt.isPresent()) {
            Customer customer = customerOpt.get();
            if (customer.getPassword().equals(password)) {
                return customer;
            } else {
                throw new Exception("Invalid credential");
            }
        } else {
            throw new Exception("Not found customer");
        }
    }


    public String changePassword(Long id, String oldPassword, String newPassword) throws Exception {
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        if(optionalCustomer.isEmpty())
            throw new Exception();
        Customer customer = optionalCustomer.get();
        if(customer.getPassword().equals(oldPassword))
        {
            customer.setPassword(newPassword);
            customerRepository.save(customer);
            return "Password Changed";
        }
        else
        {
            return "Incorrect Old Password";
        }
    }
}

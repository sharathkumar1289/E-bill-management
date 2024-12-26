package com.spc.repo;

import com.spc.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment,Long> {
    List<Payment> findByBillCustomerCustomerId(Long customerId);
}

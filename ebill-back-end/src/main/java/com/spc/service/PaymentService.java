package com.spc.service;

import com.spc.entity.Payment;
import com.spc.repo.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    @Autowired
    public PaymentRepository paymentRepository;

    public Payment savePayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public Optional<Payment> getPaymentById(Long  paymentId) {
        return paymentRepository.findById(paymentId);
    }

    public void deletePayment(Long paymentId) {
        paymentRepository.delete(paymentRepository.findById(paymentId).get());
    }

    public List<Payment> getAllPaymentsByCustomerId(Long customerId) {
        return paymentRepository.findByBillCustomerCustomerId(customerId);
    }
}

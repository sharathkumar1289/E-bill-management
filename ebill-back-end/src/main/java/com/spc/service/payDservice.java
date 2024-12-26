package com.spc.service;

import com.spc.entity.PaymentDetails;
import com.spc.entity.PaymentType;
import com.spc.repo.PaymentDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class payDservice {
    @Autowired
    private PaymentDetailsRepository paymentDetailsRepository;

    public List<PaymentDetails> getPaymentDetailsList() {
        return paymentDetailsRepository.findAll();
    }

    public List<PaymentDetails> getPaymentDetailsByType(PaymentType paymentType) {
        return paymentDetailsRepository.findByPaymentType(paymentType);
    }

    public PaymentDetails savePaymentDetails(PaymentDetails paymentDetails) {
        return paymentDetailsRepository.save(paymentDetails);
    }
}

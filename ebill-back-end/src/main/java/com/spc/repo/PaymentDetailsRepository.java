package com.spc.repo;

import com.spc.entity.PaymentDetails;
import com.spc.entity.PaymentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentDetailsRepository extends JpaRepository<PaymentDetails, Long> {

    List<PaymentDetails> findByPaymentType(PaymentType paymentType);
}

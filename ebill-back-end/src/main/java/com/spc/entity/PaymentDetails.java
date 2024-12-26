package com.spc.entity;

import jakarta.persistence.*;
import lombok.Getter;


@Getter
@Entity
@Table(name = "pay_details")
public class PaymentDetails {

    // Getters and Setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pay_id")
    private Long payId;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type") // Column name for paymentType
    private PaymentType paymentType;

    @Column(name = "payment_detail") // Column name for paymentDetail
    private String paymentDetail;
    public PaymentDetails() {
    }

    public PaymentDetails(PaymentType paymentType, String paymentDetail) {
        this.paymentType = paymentType;
        this.paymentDetail = paymentDetail;
    }

    public void setpayId(Long id) {
        this.payId = payId;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public void setPaymentDetail(String paymentDetail) {
        this.paymentDetail = paymentDetail;
    }
}

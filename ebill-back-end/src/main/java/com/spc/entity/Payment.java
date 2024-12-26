package com.spc.entity;

import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Date;

@Entity
public class Payment {

    @Id
    @Column(name = "PaymentID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payment_id_seq")
    @SequenceGenerator(name = "payment_id_seq", sequenceName = "payment_id_sequence", initialValue = 189001, allocationSize = 1)
    private Long paymentId;

    @ManyToOne
    @JoinColumn(name = "BillID", nullable = false)
    private Bill bill;

    @Column(name = "PaymentDate", nullable = false)
    private Date paymentDate;

    @Column(name = "PaymentMethod", nullable = false)
    private String paymentMethod;

    @Column(name = "AmountPaid", nullable = false)
    private Double amountPaid;

    @Column(name = "PaymentStatus", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    public enum PaymentStatus {
        COMPLETED, PENDING, FAILED
    }


    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(Double amountPaid) {
        this.amountPaid = amountPaid;
    }


    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}

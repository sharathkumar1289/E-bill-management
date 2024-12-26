package com.spc.entity;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Bill")
public class Bill {

    @Id
    @Column(name = "BillNo")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bill_no_seq")
    @SequenceGenerator(name = "bill_no_seq", sequenceName = "bill_no_sequence", initialValue = 189001, allocationSize = 1)
    private Long billNo;

    @ManyToOne
    @JoinColumn(name = "CustomerId")
    private Customer customer;

    @Column(name = "PaidDate")
    private Date paidDate;

    @Column(name = "TotalAmount")
    private Double totalAmount;

    @Column(name = "DueDate")
    private Date dueDate;

    @Column(name = "OverdueAmount")
    private Double overdueAmount;

    @Column(name = "UnitsConsumed")
    private Integer unitsConsumed;

    @Column(name = "Status")
    private String status;

    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> payments;

    // Getters and setters

    public Long getBillNo() {
        return billNo;
    }

    public void setBillNo(Long billNo) {
        this.billNo = billNo;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Date getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(Date paidDate) {
        this.paidDate = paidDate;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Double getOverdueAmount() {
        return overdueAmount;
    }

    public void setOverdueAmount(Double overdueAmount) {
        this.overdueAmount = overdueAmount;
    }

    public Integer getUnitsConsumed() {
        return unitsConsumed;
    }

    public void setUnitsConsumed(Integer unitsConsumed) {
        this.unitsConsumed = unitsConsumed;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

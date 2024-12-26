package com.spc.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "feedback") // Specify the table name if different from the class name
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedback_id") // Map to `feedback_id` column in the database
    private Long feedbackId;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false) // Map to `customer_id` column
    private Customer customer;

    @Column(name = "feedback") // Map to `feedback` column
    private String feedback;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "feedback_date") // Map to `feedback_date` column
    private Date feedbackDate;

    @Column(name = "status")
    private String status;

    // Getters and Setters

    public Long getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(Long feedbackId) {
        this.feedbackId = feedbackId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public Date getFeedbackDate() {
        return feedbackDate;
    }

    public void setFeedbackDate(Date feedbackDate) {
        this.feedbackDate = feedbackDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

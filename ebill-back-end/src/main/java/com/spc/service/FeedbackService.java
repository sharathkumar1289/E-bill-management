package com.spc.service;

import com.spc.entity.Customer;
import com.spc.entity.Feedback;
import com.spc.repo.CustomerRepository;
import com.spc.repo.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class FeedbackService{

    @Autowired
    public FeedbackRepository feedbackRepository;

    @Autowired
    public CustomerRepository customerRepository;



    public Feedback saveFeedback(Feedback feedback) {
        Customer customer = customerRepository.findById(feedback.getCustomer().getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        feedback.setCustomer(customer);
        feedback.setFeedbackDate(new Date());
        feedback.setStatus("Pending");
        return feedbackRepository.save(feedback);
    }

    public List<Feedback> getAllFeedbacks() {
        return feedbackRepository.findAll();
    }

    public Optional<Feedback> getFeedbackById(Long id) {
        return feedbackRepository.findById(id);
    }

    public void deleteFeedback(Long id) {
        feedbackRepository.delete(feedbackRepository.findById(id).get());
    }

    public List<Feedback> getFeedbacksByCustomerId(Long customerId) {
        return feedbackRepository.findByCustomer(customerRepository.findById(customerId).get());
    }
}

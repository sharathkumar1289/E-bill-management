package com.spc.controller;

import com.spc.entity.Feedback;
import com.spc.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/feedbacks")
public class FeedbackController {

    @Autowired
    public FeedbackService feedbackService;

    @PostMapping
    public ResponseEntity<Feedback> createFeedback(@RequestBody Feedback feedback) {
        Feedback savedFeedback = feedbackService.saveFeedback(feedback);
        return ResponseEntity.status(201).body(savedFeedback);
    }

    @GetMapping
    public ResponseEntity<List<Feedback>> getAllFeedbacks() {
        List<Feedback> feedbacks = feedbackService.getAllFeedbacks();
        return ResponseEntity.ok(feedbacks);
    }


    @GetMapping("/id/{customerId}")
    public ResponseEntity<List<Feedback>> getFeedbacksByCustomerId(@PathVariable Long customerId) {
        List<Feedback> feedbacks = feedbackService.getFeedbacksByCustomerId(customerId);
        if (feedbacks.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(feedbacks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Feedback> getFeedbackById(@PathVariable Long id) {
        Optional<Feedback> feedback = feedbackService.getFeedbackById(id);
        return feedback.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).body(null));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Feedback> updateFeedback(@PathVariable Long id, @RequestBody Feedback feedback) {
        Optional<Feedback> existingFeedback = feedbackService.getFeedbackById(id);
        if (existingFeedback.isPresent()) {
            feedback.setFeedbackId(id);
            Feedback updatedFeedback = feedbackService.saveFeedback(feedback);
            return ResponseEntity.ok(updatedFeedback);
        } else {
            return ResponseEntity.status(404).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFeedback(@PathVariable Long id) {
        Optional<Feedback> feedback = feedbackService.getFeedbackById(id);
        if (feedback.isPresent()) {
            feedbackService.deleteFeedback(id);
            return ResponseEntity.ok("Feedback deleted successfully.");
        } else {
            return ResponseEntity.status(404).body("Feedback not found.");
        }
    }
}

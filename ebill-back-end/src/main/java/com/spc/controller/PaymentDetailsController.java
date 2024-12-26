package com.spc.controller;
import com.spc.entity.PaymentDetails;
import com.spc.entity.PaymentType;
import com.spc.service.PaymentService;
import com.spc.service.payDservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



import java.util.List;
import java.util.Optional;

@RestController
    @RequestMapping("/api/paymentDetails")
    public class PaymentDetailsController {

        @Autowired
        private payDservice paymentDService;

        @GetMapping
        public List<PaymentDetails> getPaymentDetails() {
                return paymentDService.getPaymentDetailsList();
            }

        @GetMapping("/{paymentType}")
        public List<PaymentDetails> getPaymentDetailsByType(@PathVariable PaymentType paymentType) {
            return paymentDService.getPaymentDetailsByType(paymentType);
        }

    @PostMapping("/validatePayment")
    public ResponseEntity<String> validatePayment(@RequestBody PaymentDetails paymentDetails) {
        // Check if the provided PaymentType and paymentDetail exist in the valid list
        Optional<PaymentDetails> validPayment = paymentDService.getPaymentDetailsList().stream()
                .filter(payment -> payment.getPaymentType().equals(paymentDetails.getPaymentType()) &&
                        payment.getPaymentDetail().equals(paymentDetails.getPaymentDetail()))
                .findFirst();

        if (validPayment.isPresent()) {
            // PaymentType and PaymentDetail are valid
            return ResponseEntity.ok("Success: Payment type and details are valid!");
        } else {
            // PaymentType and/or PaymentDetail are invalid
            return ResponseEntity.status(400).body("Error: Invalid payment type or payment detail.");
        }
    }
    }





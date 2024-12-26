package com.spc.controller;

import com.spc.DTO.PaymentDTO;
import com.spc.entity.Bill;
import com.spc.entity.Payment;
import com.spc.service.BillService;
import com.spc.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    public PaymentService paymentService;

    @Autowired
    public BillService billService;

    @Autowired
    public BillController billController;

    @PostMapping("/{billNo}")
    public ResponseEntity<Payment> createPayment(@PathVariable Long billNo, @RequestBody Payment payment) {

        ResponseEntity<Bill> responseEntityBill = billController.updateBill(billNo);
        Bill bill = responseEntityBill.getBody();
        payment.setBill(bill);
        payment.setPaymentDate(bill.getPaidDate());
        payment.setAmountPaid(bill.getTotalAmount());
        payment.setPaymentStatus(Payment.PaymentStatus.COMPLETED);
        Payment savedPayment = paymentService.savePayment(payment);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPayment);
    }


    @GetMapping
    public ResponseEntity<List<Payment>> getAllPayments() {
        List<Payment> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(payments);
    }

    @GetMapping("id/P{customerId}")
    public ResponseEntity<List<Payment>> getAllPaymentsByCustomerId(@PathVariable Long customerId) {
        List<Payment> payments = paymentService.getAllPaymentsByCustomerId(customerId);
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/P{paymentId}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable Long paymentId) {
        Optional<Payment> paymentOptional = paymentService.getPaymentById(paymentId);
        if (paymentOptional.isPresent()) {
            return ResponseEntity.ok(paymentOptional.get());
        } else {
            return ResponseEntity.status(404).body(null);
        }
    }

    @PutMapping("/pay/{billNo}")
    public ResponseEntity<?> processPayment(@PathVariable Long billNo, @RequestBody PaymentDTO paymentDTO) {
        Optional<Bill> billOptional = billService.getBillById(billNo);

        if (billOptional.isPresent()) {
            Bill bill = billOptional.get();

            if ("Paid".equalsIgnoreCase(bill.getStatus())) {
                return ResponseEntity.badRequest().body("Bill already paid.");
            }

            // Update the bill
            bill.setStatus("Paid");
            bill.setPaidDate(new Date());

            // Create the payment
            Payment payment = new Payment();
            payment.setBill(bill);
            payment.setPaymentDate(new Date());
            payment.setPaymentMethod(paymentDTO.getPaymentMethod());
            payment.setAmountPaid(bill.getTotalAmount());
            payment.setPaymentStatus(Payment.PaymentStatus.COMPLETED);

            // Save updates
            billService.saveBill(bill);
            Payment savedPayment = paymentService.savePayment(payment);

            return ResponseEntity.ok(savedPayment);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Bill not found.");
    }



    @DeleteMapping("/P{paymentId}")
    public ResponseEntity<String> deletePayment(@PathVariable Long paymentId) {
        Optional<Payment> paymentOptional = paymentService.getPaymentById(paymentId);
        if (paymentOptional.isPresent()) {
            paymentService.deletePayment(paymentId);
            return ResponseEntity.ok("Payment deleted successfully.");
        } else {
            return ResponseEntity.status(404).body("Payment not found.");
        }
    }
}

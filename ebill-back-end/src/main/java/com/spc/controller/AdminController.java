package com.spc.controller;

import com.spc.entity.*;
import com.spc.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admins")
public class AdminController {

    @Autowired
    public AdminService adminService;

    @PostMapping
    public ResponseEntity<String> addAdmin(@RequestBody Admin admin) {
        try {
            Admin savedAdmin = adminService.createAdmin(admin);

            return ResponseEntity.status(201).body("success! Admin Created with username"+ savedAdmin.getUserName());
        }
        catch (Exception e)
        {
            return new ResponseEntity<>("Admin not created", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<String> deleteAdmin(@PathVariable String username) {
        Optional<Admin> adminOptional = adminService.getAdminByUsername(username);
        if (adminOptional.isPresent()) {
            adminService.deleteAdmin(username);
            return ResponseEntity.ok("Admin deleted successfully.");
        } else {
            return ResponseEntity.status(404).body("Admin not found.");
        }
    }

    @PutMapping("/{username}/password")
    public ResponseEntity<String> changePassword(@PathVariable String username, @RequestParam String newPassword) {
        Optional<Admin> adminOptional = adminService.getAdminByUsername(username);
        if (adminOptional.isPresent()) {
            Admin admin = adminOptional.get();
            admin.setPassword(newPassword);
            adminService.saveAdmin(admin);
            return ResponseEntity.ok("Password updated successfully.");
        } else {
            return ResponseEntity.status(404).body("Admin not found.");
        }
    }

    // Get all admins (GET /admins)
    @GetMapping
    public ResponseEntity<List<Admin>> getAllAdmins() {
        List<Admin> admins = adminService.getAllAdmins();
        return ResponseEntity.ok(admins);
    }


    @Autowired
    public FeedbackService feedbackService;

    @PostMapping("feedbacks")
    public ResponseEntity<Feedback> createFeedback(@RequestBody Feedback feedback) {
        Feedback savedFeedback = feedbackService.saveFeedback(feedback);
        return ResponseEntity.status(201).body(savedFeedback);
    }

    @GetMapping("feedbacks")
    public ResponseEntity<List<Feedback>> getAllFeedbacks() {
        List<Feedback> feedbacks = feedbackService.getAllFeedbacks();
        return ResponseEntity.ok(feedbacks);
    }

    @GetMapping("feedbacks/{id}")
    public ResponseEntity<Feedback> getFeedbackById(@PathVariable Long id) {
        Optional<Feedback> feedback = feedbackService.getFeedbackById(id);
        return feedback.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).body(null));
    }

    @PutMapping("feedbacks/{id}")
    public ResponseEntity<Feedback> updateFeedback(@PathVariable Long id, @RequestBody Feedback feedback) {
        Optional<Feedback> existingFeedback = feedbackService.getFeedbackById(id);
        if (existingFeedback.isPresent()) {
            Feedback updatedFeedback = feedbackService.saveFeedback(feedback);
            return ResponseEntity.ok(updatedFeedback);
        } else {
            return ResponseEntity.status(404).body(null);
        }
    }

    @DeleteMapping("feedbacks/{id}")
    public ResponseEntity<String> deleteFeedback(@PathVariable Long id) {
        Optional<Feedback> feedback = feedbackService.getFeedbackById(id);
        if (feedback.isPresent()) {
            feedbackService.deleteFeedback(id);
            return ResponseEntity.ok("Feedback deleted successfully.");
        } else {
            return ResponseEntity.status(404).body("Feedback not found.");
        }
    }



    @Autowired
    public BillService billService;


    @PostMapping("bills")
    public ResponseEntity<Bill> addBill( @RequestBody Bill bill) {
        Bill savedBill;
        try {
            savedBill = billService.saveBill(bill);
        }catch (Exception e)
        {
            return ResponseEntity.status(400).body(null);
        }
        return ResponseEntity.status(201).body(savedBill);
    }

    @GetMapping("bills")
    public ResponseEntity<List<Bill>> getAllBills() {
        List<Bill> bills = billService.getAllBills();
        return ResponseEntity.ok(bills);
    }

    @GetMapping("bills/id/{customerId}")
    public ResponseEntity<List<Bill>> getAllBillsByCustomerId(@PathVariable Long customerId) {
        List<Bill> bills = billService.getAllBillsByCustomerId(customerId);
        return ResponseEntity.ok(bills);
    }

    @GetMapping("bills/B{billNo}")
    public ResponseEntity<Bill> getBillById(@PathVariable Long billNo) {
        Optional<Bill> billOptional = billService.getBillById(billNo);
        if (billOptional.isPresent()) {
            return ResponseEntity.ok(billOptional.get());
        } else {
            return ResponseEntity.status(404).body(null);
        }
    }

    @PutMapping("bills/B{billNo}")
    public ResponseEntity<Bill> updateBill(@PathVariable Long billNo) {
        Optional<Bill> billOptional = billService.getBillById(billNo);

        if (billOptional.isPresent()) {
            Bill existingBill = billOptional.get();

            existingBill.setStatus("Paid");

            Date currentDate = new Date();
            existingBill.setPaidDate(currentDate);

            long daysOverdue = calculateOverdueDays(existingBill.getDueDate(), currentDate);
            double overdueCharge=0;
            if (daysOverdue > 0) {
                overdueCharge = daysOverdue * 0.25;
                existingBill.setTotalAmount(existingBill.getTotalAmount() + overdueCharge);
            }
            existingBill.setOverdueAmount(overdueCharge);

            Bill updatedBill = billService.saveBill(existingBill);

            return ResponseEntity.ok(updatedBill);
        } else {
            return ResponseEntity.status(404).body(null);
        }
    }

    private long calculateOverdueDays(Date dueDate, Date paidDate) {
        LocalDate dueLocalDate = convertToLocalDateViaInstant(dueDate);
        LocalDate paidLocalDate = convertToLocalDateViaInstant(paidDate);

        return ChronoUnit.DAYS.between(dueLocalDate, paidLocalDate);
    }

    private LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDate();
    }

    @DeleteMapping("bills/{billNo}")
    public ResponseEntity<String> deleteBill(@PathVariable Long billNo) {
        Optional<Bill> billOptional = billService.getBillById(billNo);
        if (billOptional.isPresent()) {
            billService.deleteBill(billNo);
            return ResponseEntity.ok("Bill deleted successfully.");
        } else {
            return ResponseEntity.status(404).body("Bill not found.");
        }
    }

    @GetMapping("bills/pending")
    public List<Bill> getPendingBills() {
        return billService.findByStatusNot("Paid");
    }

    @GetMapping("bills/paid")
    public List<Bill> getPaidBills() {
        return billService.findByStatus("Paid");
    }


    @Autowired
    private CustomerService customerService;


    @PutMapping("customers/password/{id}")
    public ResponseEntity<String> changePassword(@PathVariable Long id, @RequestParam String oldPassword, @RequestParam String newPassword) {
        try {
            String response = customerService.changePassword(id, oldPassword, newPassword);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return new ResponseEntity<>("Password not changed: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Autowired
    public PaymentService paymentService;

    @PostMapping("payments")
    public ResponseEntity<Payment> createPayment(@RequestBody Payment payment) {
        Payment savedPayment = paymentService.savePayment(payment);
        return ResponseEntity.status(201).body(savedPayment);
    }

    @GetMapping("payments")
    public ResponseEntity<List<Payment>> getAllPayments() {
        List<Payment> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(payments);
    }

    @GetMapping("payments/id/{customerId}")
    public ResponseEntity<List<Payment>> getAllPaymentsByCustomerId(@PathVariable Long customerId) {
        List<Payment> payments = paymentService.getAllPaymentsByCustomerId(customerId);
        return ResponseEntity.ok(payments);
    }

    @GetMapping("payments/{paymentId}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable Long paymentId) {
        Optional<Payment> paymentOptional = paymentService.getPaymentById(paymentId);
        if (paymentOptional.isPresent()) {
            return ResponseEntity.ok(paymentOptional.get());
        } else {
            return ResponseEntity.status(404).body(null);
        }
    }

    @PutMapping("payments/{paymentId}")
    public ResponseEntity<Payment> updatePaymentStatus(
            @PathVariable Long paymentId, @RequestBody Payment payment) {

        Optional<Payment> existingPaymentOptional = paymentService.getPaymentById(paymentId);
        if (existingPaymentOptional.isPresent()) {
            payment.setPaymentId(paymentId);
            Payment updatedPayment = paymentService.savePayment(payment);
            return ResponseEntity.ok(updatedPayment);
        } else {
            return ResponseEntity.status(404).body(null);
        }
    }

    @DeleteMapping("payments/{paymentId}")
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

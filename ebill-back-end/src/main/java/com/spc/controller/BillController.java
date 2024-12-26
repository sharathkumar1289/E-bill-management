package com.spc.controller;


import com.spc.entity.Bill;
import com.spc.entity.Customer;
import com.spc.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/bills")
public class BillController {

    private final BillService billService;

    @Autowired
    public BillController(BillService billService) {
        this.billService = billService;
    }

    @PostMapping
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

    @GetMapping
    public ResponseEntity<List<Bill>> getAllBills() {
        List<Bill> bills = billService.getAllBills();
        return ResponseEntity.ok(bills);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/{customerId}/{billId}")
    public ResponseEntity<Bill> getBillDetails(
            @PathVariable Customer customerId,
            @PathVariable Long billId) {
        try {
            Bill bill = billService.getBillByCustomerIdAndBillId(customerId, billId);
            if (bill != null) {
                return ResponseEntity.ok(bill);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("id/{customerId}")
    public ResponseEntity<List<Bill>> getAllBillsByCustomerId(@PathVariable Long customerId) {
        List<Bill> bills = billService.getAllBillsByCustomerId(customerId);
        return ResponseEntity.ok(bills);
    }

    @GetMapping("/{billNo}")
    public ResponseEntity<Bill> getBillById(@PathVariable Long billNo) {
        Optional<Bill> billOptional = billService.getBillById(billNo);
        if (billOptional.isPresent()) {
            return ResponseEntity.ok(billOptional.get());
        } else {
            return ResponseEntity.status(404).body(null);
        }
    }
    @PutMapping("/change/{billId}")
    public ResponseEntity<Bill> updateBill(@PathVariable Long billId, @RequestBody Bill updatedBill) {
        Optional<Bill> existingBillOpt = billService.getBillById(billId);
        if (existingBillOpt.isPresent()) {
            Bill existingBill = existingBillOpt.get();
            existingBill.setTotalAmount(updatedBill.getTotalAmount());
            existingBill.setUnitsConsumed(updatedBill.getUnitsConsumed());
            existingBill.setDueDate(updatedBill.getDueDate());
            existingBill.setStatus(updatedBill.getStatus());
            existingBill.setOverdueAmount(updatedBill.getOverdueAmount());
            existingBill.setPaidDate(updatedBill.getPaidDate());
            existingBill.setCustomer(updatedBill.getCustomer());

            billService.saveBill(existingBill);
            return ResponseEntity.ok(existingBill);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }



    @PutMapping("/{billNo}")
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

    @DeleteMapping("/{billNo}")
    public ResponseEntity<String> deleteBill(@PathVariable Long billNo) {
        Optional<Bill> billOptional = billService.getBillById(billNo);
        if (billOptional.isPresent()) {
            billService.deleteBill(billNo);
            return ResponseEntity.ok("Bill deleted successfully.");
        } else {
            return ResponseEntity.status(404).body("Bill not found.");
        }
    }

    @GetMapping("/pending")
    public List<Bill> getPendingBills() {
        return billService.findByStatusNot("Paid");
    }

    @GetMapping("/paid")
    public List<Bill> getPaidBills() {
        return billService.findByStatus("Paid");
    }

    @GetMapping("pending/id/{customerId}")
    public List<Bill> getPendingBillsByCustomerId(@PathVariable Long customerId) {
        return billService.findByCustomerCustomerIdStatusNot(customerId,"Paid");
    }



}

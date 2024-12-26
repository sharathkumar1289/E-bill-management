package com.spc.service;

import com.spc.entity.Bill;
import com.spc.entity.Customer;
import com.spc.repo.BillRepository;
import com.spc.repo.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BillService {

    @Autowired
    public BillRepository billRepository;

    @Autowired
    public CustomerRepository customerRepository;

    public Bill saveBill(Bill bill) {
        try {
            return billRepository.save(bill);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public List<Bill> getAllBills() {
        return billRepository.findAll();
    }

    public Optional<Bill> getBillById(Long billNo) {
        return billRepository.findById(billNo);
    }

    public void deleteBill(Long billNo) {
        billRepository.delete(billRepository.findById(billNo).get());
    }

    public List<Bill> getAllBillsByCustomerId(Long customerId) {
        return billRepository.findByCustomer(customerRepository.findById(customerId).get());
    }

    public List<Bill> findByStatusNot(String paid) {
        return billRepository.findByStatusNot(paid);
    }

    public List<Bill> findByStatus(String paid) {
        return billRepository.findByStatus(paid);
    }

    public List<Bill> findByCustomerCustomerIdStatusNot(Long customerId, String paid) {
        return billRepository.findByCustomerAndStatusNot(customerRepository.findCustomerByCustomerId(customerId).get(),paid);
    }
    public Bill getBillByCustomerIdAndBillId(Customer customerId, Long billId) {
        return billRepository.findByCustomerAndBillNo(customerId, billId);
    }
}

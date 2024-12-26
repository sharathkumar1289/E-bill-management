package com.spc.repo;

import com.spc.entity.Bill;
import com.spc.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    List<Bill> findByCustomer(Customer customer);

    List<Bill> findByStatusNot(String paid);

    List<Bill> findByStatus(String paid);

    List<Bill> findByCustomerAndStatusNot(Customer customer, String status);

    Bill findByCustomerAndBillNo(Customer customer, Long billNo);

}

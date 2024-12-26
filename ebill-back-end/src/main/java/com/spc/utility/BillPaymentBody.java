package com.spc.utility;

import com.spc.entity.Bill;
import com.spc.entity.Customer;
import com.spc.entity.Payment;
import jakarta.persistence.*;

import java.util.Date;

public class BillPaymentBody {
    private Long billNo;
    private String paymentMethod;
    private Bill bill;
    private Date paymentDate;
    private Double amountPaid;
    private Date paidDate;
}

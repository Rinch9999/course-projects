package com.su.property.service;

import com.su.property.entity.PaymentBill;
import com.su.property.mapper.PaymentMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 缴费服务。核心：缴费事务（更新账单状态 + 写缴费记录）。
 */
@Service
public class PaymentService {

    private final PaymentMapper paymentMapper;

    public PaymentService(PaymentMapper paymentMapper) {
        this.paymentMapper = paymentMapper;
    }

    public List<PaymentBill> listByOwner(int ownerId) {
        return paymentMapper.listByOwner(ownerId);
    }

    /**
     * 缴费（事务）：幂等校验 + 账单置为已支付 + 写缴费记录。
     */
    @Transactional
    public void pay(int billId) {
        PaymentBill bill = paymentMapper.findBillById(billId);
        if (bill == null) {
            throw new IllegalArgumentException("账单不存在");
        }
        if (PaymentBill.STATUS_PAID.equals(bill.getStatus())) {
            throw new IllegalStateException("账单已支付，请勿重复缴费");
        }
        paymentMapper.markPaid(billId);
        paymentMapper.insertPaymentRecord(billId);
    }
}
package com.su.property.controller;

import com.su.property.entity.Owner;
import com.su.property.entity.PaymentBill;
import com.su.property.mapper.OwnerMapper;
import com.su.property.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * 缴费接口（RESTful）。业主身份来自 JWT，防止越权查询他人账单。
 */
@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentService paymentService;
    private final OwnerMapper ownerMapper;

    public PaymentController(PaymentService paymentService, OwnerMapper ownerMapper) {
        this.paymentService = paymentService;
        this.ownerMapper = ownerMapper;
    }

    /** 我的账单 */
    @GetMapping
    public List<PaymentBill> list(HttpServletRequest request) {
        int userId = Integer.parseInt((String) request.getAttribute("userId"));
        Owner owner = ownerMapper.findByUserId(userId);
        if (owner == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "当前账号未绑定业主档案");
        }
        return paymentService.listByOwner(owner.getId());
    }

    /** 缴费 */
    @PostMapping("/{billId}/pay")
    public void pay(@PathVariable int billId) {
        paymentService.pay(billId);
    }
}
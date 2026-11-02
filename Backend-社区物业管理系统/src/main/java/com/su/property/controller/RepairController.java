package com.su.property.controller;

import com.su.property.entity.Owner;
import com.su.property.entity.RepairOrder;
import com.su.property.mapper.OwnerMapper;
import com.su.property.service.RepairService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * 报修接口（RESTful）。业主身份来自 JWT，防止越权提交报修。
 */
@RestController
@RequestMapping("/api/repair")
public class RepairController {

    private final RepairService repairService;
    private final OwnerMapper ownerMapper;

    public RepairController(RepairService repairService, OwnerMapper ownerMapper) {
        this.repairService = repairService;
        this.ownerMapper = ownerMapper;
    }

    /** 提交报修 */
    @PostMapping
    public int submit(HttpServletRequest request, @RequestBody RepairOrder order) {
        int userId = Integer.parseInt((String) request.getAttribute("userId"));
        Owner owner = ownerMapper.findByUserId(userId);
        if (owner == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "当前账号未绑定业主档案");
        }
        order.setOwnerId(owner.getId());
        return repairService.submit(order);
    }

    /** 报修列表（按状态） */
    @GetMapping
    public List<RepairOrder> list(@RequestParam String status,
                                  @RequestParam(defaultValue = "1") int page,
                                  @RequestParam(defaultValue = "10") int size) {
        return repairService.listByStatus(status, page, size);
    }

    /** 工单状态流转 */
    @PutMapping("/{id}/status")
    public void transit(@PathVariable int id, @RequestParam String status) {
        repairService.transit(id, status);
    }
}
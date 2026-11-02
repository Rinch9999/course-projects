package com.su.property.service;

import com.su.property.entity.RepairOrder;
import com.su.property.mapper.RepairOrderMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 报修服务。核心：工单状态流转。
 */
@Service
public class RepairService {

    private final RepairOrderMapper repairOrderMapper;

    public RepairService(RepairOrderMapper repairOrderMapper) {
        this.repairOrderMapper = repairOrderMapper;
    }

    /** 业主提交报修 */
    public int submit(RepairOrder order) {
        order.setStatus(RepairOrder.STATUS_PENDING);
        return repairOrderMapper.insert(order);
    }

    /** 工单状态流转：校验合法流转 + 更新状态（骨架版，可扩展写 repair_status_log） */
    @Transactional
    public void transit(int id, String toStatus) {
        RepairOrder order = repairOrderMapper.findById(id);
        if (order == null) {
            throw new IllegalArgumentException("工单不存在");
        }
        // 简单校验：已完成单不能再流转
        if (RepairOrder.STATUS_DONE.equals(order.getStatus())) {
            throw new IllegalStateException("已完成工单不可流转");
        }
        repairOrderMapper.updateStatus(id, toStatus);
        // 难点：状态流转留痕，写入 repair_status_log
        repairOrderMapper.insertStatusLog(id, order.getStatus(), toStatus);
    }

    /** 按状态分页查询（命中联合索引） */
    public List<RepairOrder> listByStatus(String status, int page, int size) {
        return repairOrderMapper.listByStatus(status, (page - 1) * size, size);
    }
}

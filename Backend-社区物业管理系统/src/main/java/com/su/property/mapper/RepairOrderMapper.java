package com.su.property.mapper;

import com.su.property.entity.RepairOrder;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 报修工单 Mapper。
 */
@Mapper
public interface RepairOrderMapper {

    int insert(RepairOrder order);

    /** 按状态分页查询报修列表（配合联合索引，见 XML） */
    List<RepairOrder> listByStatus(@Param("status") String status,
                                   @Param("offset") int offset,
                                   @Param("limit") int limit);

    RepairOrder findById(int id);

    int updateStatus(@Param("id") int id, @Param("status") String status);

    /** 写入状态流转日志（难点：工单流转留痕） */
    @Insert("INSERT INTO repair_status_log(order_id, from_status, to_status, log_time) "
            + "VALUES(#{orderId}, #{fromStatus}, #{toStatus}, NOW())")
    int insertStatusLog(@Param("orderId") int orderId,
                        @Param("fromStatus") String fromStatus,
                        @Param("toStatus") String toStatus);
}

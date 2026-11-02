package com.su.property.mapper;

import com.su.property.entity.PaymentBill;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 缴费 Mapper。
 */
@Mapper
public interface PaymentMapper {

    @Select("SELECT id, owner_id, amount, status, created_at FROM payment_bill "
            + "WHERE owner_id = #{ownerId} ORDER BY created_at DESC")
    List<PaymentBill> listByOwner(int ownerId);

    @Select("SELECT id, owner_id, amount, status, created_at FROM payment_bill WHERE id = #{id}")
    PaymentBill findBillById(int id);

    @Update("UPDATE payment_bill SET status = 'PAID' WHERE id = #{id}")
    int markPaid(int id);

    @Insert("INSERT INTO payment_record(bill_id, paid_at) VALUES(#{billId}, NOW())")
    int insertPaymentRecord(int billId);
}
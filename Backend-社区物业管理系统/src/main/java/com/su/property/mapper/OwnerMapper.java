package com.su.property.mapper;

import com.su.property.entity.Owner;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 业主 Mapper。把 JWT 里的 userId 关联到 owner_id，防止越权。
 */
@Mapper
public interface OwnerMapper {

    @Select("SELECT id, user_id, room_id, name, phone FROM owner WHERE user_id = #{userId}")
    Owner findByUserId(int userId);
}
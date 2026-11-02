package com.su.property.mapper;

import com.su.property.entity.Announcement;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 公告 Mapper。
 */
@Mapper
public interface AnnouncementMapper {

    @Select("SELECT id, title, content, created_at FROM announcement "
            + "ORDER BY created_at DESC LIMIT #{limit}")
    List<Announcement> list(int limit);
}
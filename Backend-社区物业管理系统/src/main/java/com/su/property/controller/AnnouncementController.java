package com.su.property.controller;

import com.su.property.entity.Announcement;
import com.su.property.mapper.AnnouncementMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 公告接口（只读）。
 */
@RestController
@RequestMapping("/api/announcement")
public class AnnouncementController {

    private final AnnouncementMapper announcementMapper;

    public AnnouncementController(AnnouncementMapper announcementMapper) {
        this.announcementMapper = announcementMapper;
    }

    @GetMapping
    public List<Announcement> list() {
        return announcementMapper.list(10);
    }
}
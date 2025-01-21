package com.example.htaproject2308.mapper;

import com.example.htaproject2308.dto.Notification;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NotificationMapper {
    public void saveNotification(Notification notification);
}

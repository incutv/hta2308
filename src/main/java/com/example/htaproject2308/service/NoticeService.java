package com.example.htaproject2308.service;


import com.example.htaproject2308.dto.Notice;
import java.util.List;


public interface NoticeService {
    List<Notice> getAllNotices();

    List<Notice> getTop10Views();

}
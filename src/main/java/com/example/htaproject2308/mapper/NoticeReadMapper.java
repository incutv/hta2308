package com.example.htaproject2308.mapper;

import com.example.htaproject2308.dto.Notice;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface NoticeReadMapper {
     public List<Notice> findAll();

     public List<Notice> findTop10Views();

     public List<Notice> findOne();

}
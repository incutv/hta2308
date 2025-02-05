package com.example.htaproject2308.mapper;

import com.example.htaproject2308.dto.Book;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface ProductMapper {
     public void saveProduct(Book book);
}
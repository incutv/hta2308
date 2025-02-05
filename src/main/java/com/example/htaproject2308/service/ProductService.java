package com.example.htaproject2308.service;

import com.example.htaproject2308.dto.Book;
import com.example.htaproject2308.mapper.ProductMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.sql.BatchUpdateException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

@Slf4j
@Service
public class ProductService {

    private final ExcelReaderService excelReaderService;
    private final ProductMapper productMapper;

    private final JdbcTemplate jdbcTemplate;
    public ProductService(ExcelReaderService excelReaderService, ProductMapper productMapper, JdbcTemplate jdbcTemplate){
        this.excelReaderService = excelReaderService;
        this.productMapper = productMapper;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public void saveProduct(InputStream inputStream){
        List<Book> data = excelReaderService.readExcelWithXSSF(inputStream);
        // 1방식 순차처리
        data.forEach(productMapper::saveProduct);

        // 2방식 병렬처리
        data.parallelStream().forEach(productMapper::saveProduct);


        //bulkInsertBooks(data);
        // 병렬 스트림을 사용하여 데이터 삽입
    }

    public void bulkInsertBooks(List<Book> excelDatas) {
        String sql = "INSERT INTO book (title, author, price) VALUES (?, ? ,?)";
        List<Book> failedBookIds = new ArrayList<>(); // 실패한 book ID를 저장할 리스트

        try {
            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    Book book = excelDatas.get(i);
                    ps.setString(1, book.getTitle());
                    ps.setString(2, book.getAuthor());
                    ps.setInt(3, book.getPrice());
                }

                @Override
                public int getBatchSize() {
                    return excelDatas.size();
                }
            });
        } catch (DataAccessException e) {
            Throwable cause = e.getCause();
            if (cause instanceof BatchUpdateException) {
                BatchUpdateException batchEx = (BatchUpdateException) cause;
                int[] updateCounts = batchEx.getUpdateCounts();

                // 실패한 인덱스만 추출
                int index = 0;
                for (Book book : excelDatas) {
                    if (updateCounts[index] == Statement.EXECUTE_FAILED) { // 실패한 경우
                        failedBookIds.add(book);
                    }
                    index++;
                }

                // 로그 또는 추가 처리
                notifyError(failedBookIds);
            }
            throw e; // 실패 예외를 다시 던져 호출자에게 알림
        }
    }

    private void notifyError(List<Book> books) {
        for (Book book : books) {
            log.error("Failed to insert book: Title={}, Author={}, Price={}",
                    book.getTitle(), book.getAuthor(), book.getPrice());

            log.debug("Detailed Book Info: {}", book);
        }
        // 담당자 알림(email, webhook)
        // 정산 실패 케이스를 DB에 저장
    }


}

package com.example.htaproject2308.controller;

import com.example.htaproject2308.dto.Book;
import com.example.htaproject2308.mapper.ProductMapper;
import com.example.htaproject2308.service.ExcelReaderService;
import com.example.htaproject2308.service.ExcelSheetHandler;
import com.example.htaproject2308.service.ExcelSheetHandler2;
import com.example.htaproject2308.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ExcelReaderService excelReaderService;
    private final ProductService productService;

    private final ProductMapper productMapper;

    @PostMapping("/upload-excel")
    public ResponseEntity<List<Book>> uploadExcel(@RequestParam("file") MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            productService.saveProduct(inputStream);
            //List<Book> data = excelReaderService.readExcelWithXSSF(inputStream);
            return ResponseEntity.ok(Collections.emptyList());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<List<List<String>>> handleFileUpload(@RequestParam("file") MultipartFile multipartFile) throws Exception {
        File file = getFile(multipartFile);

        ExcelSheetHandler2 excelSheetHandler = ExcelSheetHandler2.readExcel(file);

        List<Book> excelDatas = excelSheetHandler.getBooks();

        // 순차 처리 방식 :  실행 시간(ms): 806 , 실행 시간(ms): 1477
        /*long beforeTime1 = System.currentTimeMillis();
        excelDatas.forEach(productMapper::saveProduct);
        long afterTime1 = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
        long diffTime1 = afterTime1 - beforeTime1; // 두 개의 실행 시간
        System.out.println("실행 시간(ms): " + diffTime1); // 세컨드(초 단위 변환)*/

        // 병렬 처리 방식 : 실행 시간(ms): 412 , 실행 시간(ms): 465
       /* long beforeTime1 = System.currentTimeMillis();
        excelDatas.parallelStream().forEach(productMapper::saveProduct);
        long afterTime1 = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
        long diffTime1 = afterTime1 - beforeTime1; // 두 개의 실행 시간
        System.out.println("실행 시간(ms): " + diffTime1); // 세컨드(초 단위 변환)*/

        // bulk insert 방식 : 실행 시간(ms): 340 , 실행 시간(ms): 405
        long beforeTime1 = System.currentTimeMillis();
        productService.bulkInsertBooks(excelDatas);
        long afterTime1 = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
        long diffTime1 = afterTime1 - beforeTime1; // 두 개의 실행 시간
        System.out.println("실행 시간(ms): " + diffTime1); // 세컨드(초 단위 변환)*/

        return ResponseEntity.ok(Collections.emptyList());
    }

    private File getFile(MultipartFile file) throws IOException {
        String originalFileName = file.getOriginalFilename();

        String directoryPath = System.getProperty("user.dir") + "/uploads/";  // 리소스 경로로 설정

        // 디렉토리 경로와 파일명을 결합하여 전체 파일 경로 생성
        String filePath = directoryPath + originalFileName;

        // 파일을 저장할 경로 객체
        Path path = Paths.get(filePath);

        // 파일이 존재하지 않으면 디렉토리 생성
        if (!Files.exists(path.getParent())) {
            Files.createDirectories(path.getParent());
        }

        // 파일을 지정된 경로에 저장
        file.transferTo(path.toFile());  // MultipartFile을 디스크에 저장

        // 저장된 파일 객체 반환
        return path.toFile();  // File 객체 반환
    }

}


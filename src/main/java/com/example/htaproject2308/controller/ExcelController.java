package com.example.htaproject2308.controller;

import com.example.htaproject2308.service.ExcelReaderService;
import com.example.htaproject2308.service.ExcelSheetHandler;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.RequiredArgsConstructor;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ExcelController {

    private final ExcelReaderService excelReaderService;

    @PostMapping("/upload-excel")
    public ResponseEntity<List<List<String>>> uploadExcel(@RequestParam("file") MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            List<List<String>> data = excelReaderService.readExcelWithXSSF(inputStream);

            return ResponseEntity.ok(data);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
드
    @PostMapping("/upload")
    public ResponseEntity<List<List<String>>> handleFileUpload(@RequestParam("file") MultipartFile multipartFile) throws Exception {
        File file = getFile(multipartFile);

        ExcelSheetHandler excelSheetHandler = ExcelSheetHandler.readExcel(file);

        List<List<String>> excelDatas = excelSheetHandler.getRows();

        return ResponseEntity.ok(excelDatas);
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


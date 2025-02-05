package com.example.htaproject2308.service;


import com.example.htaproject2308.dto.Book;
import org.apache.poi.ooxml.util.SAXHelper;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.xml.sax.ContentHandler;
import org.xml.sax.XMLReader;
import org.xml.sax.InputSource;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ExcelSheetHandler2 implements XSSFSheetXMLHandler.SheetContentsHandler {

    private int currentCol = -1;
    private int currRowNum = 0;

    private final List<Book> books = new ArrayList<>();  // BookDto 리스트
    private Book row = new Book(); // 한 행의 데이터 저장
    private List<String> header = new ArrayList<>();

    public static ExcelSheetHandler2 readExcel(File file) throws Exception {

        ExcelSheetHandler2 sheetHandler = new ExcelSheetHandler2();
        try {

            OPCPackage opc = OPCPackage.open(file);
            XSSFReader xssfReader = new XSSFReader(opc);
            StylesTable styles = xssfReader.getStylesTable();
            ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(opc);

            InputStream inputStream = xssfReader.getSheetsData().next();
            InputSource inputSource = new InputSource(inputStream);
            ContentHandler handle = new XSSFSheetXMLHandler(styles, strings, sheetHandler, false);

            XMLReader xmlReader = SAXHelper.newXMLReader();
            xmlReader.setContentHandler(handle);

            xmlReader.parse(inputSource);
            inputStream.close();
            opc.close();
        } catch (Exception e) {
            // 에러 처리 로직 추가 가능
        }

        return sheetHandler;
    }

    public List<Book> getBooks() {
        return books;
    }

    @Override
    public void startRow(int rowNum) {
        this.currentCol = -1;
        this.currRowNum = rowNum;
        this.row = new Book(); // 새로운 DTO 객체 생성
    }

    @Override
    public void cell(String columnName, String value, XSSFComment comment) {
        int iCol = (new CellReference(columnName)).getCol();
        currentCol = iCol;

        switch (iCol) {
            case 0: // 제목
                row.setTitle(value);
                break;
            case 1: // 가격
                row.setPrice(parsePrice(value));
                break;
            case 2: // 저자
                row.setAuthor(value);
                break;
            default:
                break;
        }
    }

    @Override
    public void endRow(int rowNum) {
        if (rowNum == 0) {
            // 첫 번째 행을 헤더로 설정
            header = List.of(row.getTitle(), String.valueOf(row.getPrice()), row.getAuthor());
        } else {
            if(row.getTitle() != null){
                books.add(row);
            }
        }
    }

    private int parsePrice(String priceStr) {
        try {
            return (int) Double.parseDouble(priceStr); // "12000.0" 같은 값 변환
        } catch (NumberFormatException e) {
            return 0; // 변환 실패 시 기본값
        }
    }

    @Override
    public void headerFooter(String text, boolean isHeader, String tagName) {
        // 사용 안 함
    }
}


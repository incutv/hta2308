package com.example.htaproject2308.service;

import com.example.htaproject2308.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ExcelReaderService {
    private static final long MAX_FILE_SIZE_THRESHOLD = 30000L;

    /* 파일의 사이즈의 따라 분기 치는 코드 */
    public List<List<String>> readExcel(MultipartFile file) throws Exception {
        List<List<String>> dataList = new ArrayList<>();
        if(file.getSize() < MAX_FILE_SIZE_THRESHOLD){
            dataList = readExcelWithXSSF(file.getInputStream());
        }else{
            dataList = readExcelWithSAX(FileUtil.saveAndGetFile(file));
        }

        return dataList;
    }

    /**
     * SAX 기반 엑셀 파일 읽기
     */
    public List<List<String>> readExcelWithSAX(File file) throws Exception {
        ExcelSheetHandler excelSheetHandler = ExcelSheetHandler.readExcel(file);

        List<List<String>> excelDatas = excelSheetHandler.getRows();

        return excelDatas;
    }


    /**
     * XSSFWorkbook 기반 엑셀 파일 읽기
     */
    public List<List<String>> readExcelWithXSSF(InputStream inputStream) {
        List<List<String>> dataList = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(inputStream)) { // XSSFWorkbook 사용
            Sheet sheet = workbook.getSheetAt(0); // 첫 번째 시트 가져오기

            for (Row row : sheet) {
                List<String> rowData = new ArrayList<>();
                for (Cell cell : row) {
                    rowData.add(getCellValue(cell));
                }
                dataList.add(rowData);
            }
        } catch (Exception e) {
            log.error("XSSFWorkbook 방식 엑셀 파일 읽기 오류", e);
        }

        return dataList;
    }

    /**
     * SXSSFWorkbook는 읽기지원을 하지 않음 -> 해당 코드는 정상적으로 작동하지 않음.
     */
    public List<List<String>> readExcelWithSXSSF(InputStream inputStream) {
        List<List<String>> dataList = new ArrayList<>();

        try (OPCPackage pkg = OPCPackage.open(inputStream);
             Workbook workbook = new SXSSFWorkbook(new XSSFWorkbook(pkg))) { // SXSSFWorkbook 사용

            // 첫 번째 시트를 가져옴
            Sheet sheet = workbook.getSheetAt(0);

            // 시트의 전체 행 수를 체크하여 유효한 데이터만 처리
            for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
                Row row = sheet.getRow(i);

                // 해당 행이 null이 아닌 경우만 처리
                if (row != null) {
                    List<String> rowData = new ArrayList<>();

                    // 해당 행의 모든 셀을 처리
                    for (int j = 0; j < row.getPhysicalNumberOfCells(); j++) {
                        Cell cell = row.getCell(j);
                        rowData.add(getCellValue(cell));
                    }

                    dataList.add(rowData);
                }
            }
        } catch (Exception e) {
            log.error("SXSSFWorkbook 방식 엑셀 파일 읽기 오류", e);
        }

        return dataList;
    }


    private String getCellValue(Cell cell) {
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf(cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getCellFormula();
            case BLANK, ERROR -> "";
            default -> "";
        };
    }

    /**
     * 현재 JVM 사용 메모리 측정
     */
    private long getUsedMemory() {
        System.gc(); // GC 실행 후 메모리 측정 (더 정확한 값 얻기 위해)
        return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }
}

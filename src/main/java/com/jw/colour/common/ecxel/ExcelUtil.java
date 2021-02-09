package com.jw.colour.common.ecxel;

import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * download and upload Excel
 *
 * @author jw on 2021/2/2
 */
public class ExcelUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelUtil.class);

    public static void downExcel(HttpServletResponse response, Map<String, Object> param) {
        try {
            String path = System.getProperty("user.dir") + File.separator +
                    "WEB-INF/excel.xls";
            File file = new File(path);
            if (!file.exists()) {
                LOGGER.error(path + " This file does not exist! ");
            }
            POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(file));
            String excelName = file.getName();
            HSSFWorkbook wb = new HSSFWorkbook(fs);
            HSSFSheet sheet = wb.getSheetAt(0);

            int rowNum = sheet.getLastRowNum();
            HSSFRow row;
            for (int i = 0; i < rowNum; i++) {
                row = sheet.getRow(i);
                // 获取行里面的总列数
                int columnNum = 0;
                if (row != null) {
                    columnNum = row.getPhysicalNumberOfCells();
                }
                for (int j = 0; j < columnNum; j++) {
                    HSSFCell cell = sheet.getRow(i).getCell(j);
                    String cellValue = cell.getStringCellValue();
                    for (Map.Entry<String, Object> entry : param.entrySet()) {
                        String key = entry.getKey();
                        if (key.equals(cellValue)) {
                            String value = entry.getValue().toString();
                            cell.setCellValue(value);
                        }
                    }
                }
            }
            response.setContentType("application/binary;charset=utf-8");
            response.setHeader("Content-disposition", "attachment; filename=" + excelName);
            ServletOutputStream outputStream = response.getOutputStream();
            wb.write(outputStream);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

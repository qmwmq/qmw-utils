package io.github.qmwmq.utils;

import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.OLE2NotOfficeXmlFileException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ExcelUtils {

    public static Workbook streamToWorkbook(InputStream stream) throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(IOUtils.toByteArray(stream));
        try {
            return new XSSFWorkbook(inputStream); // 先按xlsx读
        } catch (Exception e) {
            try {
                if (e instanceof OLE2NotOfficeXmlFileException) { // 报告版本错误再按xls读取
                    inputStream.reset();
                    return new HSSFWorkbook(inputStream);
                } else {
                    throw e;
                }
            } catch (Exception ex) { // 其他错误统一处理
                throw new RuntimeException("读取Excel文件失败");
            }
        }
    }

}

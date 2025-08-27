package io.github.qmwmq.utils;

import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ooxml.POIXMLException;
import org.apache.poi.openxml4j.exceptions.OLE2NotOfficeXmlFileException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Excel生成工具
 */
public class ExcelGenerator {

    /**
     * 默认构造器
     */
    private ExcelGenerator() {
    }

    public static Workbook streamToWorkbook(InputStream stream) throws IOException {
        byte[] bytes = IOUtils.toByteArray(stream);
        try {
            return new XSSFWorkbook(new ByteArrayInputStream(bytes)); // 先尝试xlsx
        } catch (OLE2NotOfficeXmlFileException e) {
            return new HSSFWorkbook(new ByteArrayInputStream(bytes)); // 再尝试xls
        } catch (POIXMLException e) { // 其他 POI 内部异常
            throw new RuntimeException("Excel文件解析失败", e);
        } catch (Exception e) {
            throw new RuntimeException("读取Excel文件失败", e);
        }
    }

}

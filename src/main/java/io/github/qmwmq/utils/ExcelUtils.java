package io.github.qmwmq.utils;

import io.github.qmwmq.exception.CustomException;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.OLE2NotOfficeXmlFileException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * 将某一行复制并插入到下一行
     *
     * @param sheet Sheet对象
     * @param row   行号，从0开始
     * @param size  复制多少次
     */
    public static void copyRow(Sheet sheet, int row, int size) {
        if (row < 0)
            throw new CustomException("row必须大于等于0");
        if (size <= 0 || sheet.getLastRowNum() < row)
            return;
        // 预存行高
        List<Short> rowHeights = new ArrayList<>();
        short defaultHeight = sheet.getDefaultRowHeight();
        for (int i = row; i <= sheet.getLastRowNum(); i++) {
            Row sheetRow = sheet.getRow(i);
            rowHeights.add(sheetRow == null ? defaultHeight : sheetRow.getHeight());
        }
        for (int i = 0; i < size; i++)
            rowHeights.add(1, rowHeights.getFirst());

        // 移动
        if (sheet.getLastRowNum() > row) // 如果要复制的行就是最后一行，则不需要移动，直接向下复制即可
            sheet.shiftRows(row + 1, sheet.getLastRowNum(), size);
        // 插入预存的行高
        for (int i = row; i <= sheet.getLastRowNum(); i++) {
            Row sheetRow = sheet.getRow(i);
            if (sheetRow == null)
                sheetRow = sheet.createRow(i);
            sheetRow.setHeight(rowHeights.get(i - row));
        }
        // 复制样式和文字
        for (int i = 0; i < sheet.getRow(row).getLastCellNum(); i++) {
            Cell cell1 = sheet.getRow(row).getCell(i);
            if (cell1 == null) continue;
            for (int j = 1; j <= size; j++) {
                Row row1 = sheet.getRow(row + j);
                if (row1 == null)
                    row1 = sheet.createRow(row + j);
                Cell cell2 = row1.createCell(i);
                cell2.setCellStyle(cell1.getCellStyle());
                cell2.setCellValue(getCellValue(cell1));
            }
        }
    }

    /**
     * 解析单元格内容
     *
     * @param cell 要解析的单元格对象
     * @return 单元格中的内容
     */
    public static String getCellValue(Cell cell) {
        if (cell == null)
            return "";
        // 判断是否日期格式
        if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            long date = cell.getDateCellValue().getTime();
            if ((date + DateUtil.DAY_MILLISECONDS / 3) % DateUtil.DAY_MILLISECONDS == 0) // 是0点则转化为yyyy-MM-dd
                return new Date(date).toString();
            else // 不是0点则转化为yyyy-MM-dd HH:mm:ss
                return new Timestamp(date).toString();
        } else {
            // 非日期格式直接解析为string
            return new DataFormatter().formatCellValue(cell).trim();
        }
    }

}

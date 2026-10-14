package io.github.qmwmq.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import io.github.qmwmq.exception.CustomException;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Excel生成工具
 */
public class ExcelWriter {

    public static final HorizontalCellStyleStrategy STYLE;

    static {
        WriteCellStyle headStyle = new WriteCellStyle();
        headStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        headStyle.setWrapped(false); // 不自动换行
        WriteFont headFont = new WriteFont();
        headFont.setFontHeightInPoints((short) 13); // 13号字
        headFont.setFontName("Microsoft YaHei");
        headStyle.setWriteFont(headFont);

        WriteCellStyle cellStyle = new WriteCellStyle();
        WriteFont cellFont = new WriteFont();
        cellFont.setFontName("Microsoft YaHei");
        cellStyle.setWriteFont(cellFont);

        STYLE = new HorizontalCellStyleStrategy(headStyle, cellStyle);
    }

    static void main() {
        write(new HashMap<>() {{
            put("s1", new ArrayList<>() {{
                for (int i = 0; i < 10000; i++) {
                    add(new HashMap<>() {{
                        put("a", "123");
                        put("b", "456");
                        put("c", "789");
                        put("d", "111");
                    }});
                }
                add(new HashMap<>() {{
                    put("e", "333");
                }});
            }});
        }}, "C:\\Users\\12334\\Desktop\\test.xlsx");
    }

    /**
     * 默认构造器
     */
    private ExcelWriter() {
    }

    public static void write(Map<String, List<? extends Map<String, Object>>> map, String filePath) {
        try (com.alibaba.excel.ExcelWriter writer = EasyExcel.write(filePath)
                .registerWriteHandler(STYLE)
                .build()) {
            write(map, writer);
        }
    }

    public static void download(Map<String, List<? extends Map<String, Object>>> map, HttpServletResponse response, String fileName) {
        response.setContentType("application/x-download");
        response.setCharacterEncoding(StandardCharsets.UTF_8);
        if (StringUtils.isBlank(fileName))
            fileName = StringUtils.uuid();
        fileName = URLEncoder.encode(fileName + ".xlsx", StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        response.setHeader("Content-Disposition", "attachment;filename*=utf-8''" + fileName);
        try (com.alibaba.excel.ExcelWriter writer = EasyExcel.write(response.getOutputStream())
                .registerWriteHandler(STYLE)
                .build()) {
            write(map, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void download(Map<String, List<? extends Map<String, Object>>> map, HttpServletResponse response) {
        download(map, response, null);
    }

    public static void download(List<? extends Map<String, Object>> list, HttpServletResponse response) {
        download(Collections.singletonMap("Sheet1", list), response, null);
    }

    public static void download(List<? extends Map<String, Object>> list, HttpServletResponse response, String fileName) {
        download(Collections.singletonMap("Sheet1", list), response, fileName);
    }

    public static void download(Workbook workbook, HttpServletResponse response, String fileName) throws IOException {
        response.setContentType("application/x-download");
        response.setCharacterEncoding(StandardCharsets.UTF_8);
        if (StringUtils.isBlank(fileName))
            fileName = StringUtils.uuid();
        String extension = workbook instanceof XSSFWorkbook ? ".xlsx" : ".xls";
        fileName = URLEncoder.encode(fileName + extension, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        response.setHeader("Content-Disposition", "attachment;filename*=utf-8''" + fileName);
        workbook.write(response.getOutputStream());
        workbook.close();
    }

    public static void downloadOnlyHeaders(Collection<String> headers, HttpServletResponse response, String fileName) {
        download(Collections.singletonMap("Sheet1", new ArrayList<>() {{
            add(new LinkedHashMap<>() {{
                headers.forEach(h -> put(h, ""));
            }});
        }}), response, fileName);
    }

    public static void downloadOnlyHeaders(Collection<String> headers, HttpServletResponse response) {
        downloadOnlyHeaders(headers, response, null);
    }

    private static void write(Map<String, List<? extends Map<String, Object>>> map, com.alibaba.excel.ExcelWriter writer) {
        Set<String> head = new LinkedHashSet<>(); // 表头
        List<List<Object>> data = new ArrayList<>(); // 内容

        AtomicInteger sheetIndex = new AtomicInteger();
        // 循环写多个sheet
        map.forEach((k, v) -> {
            head.clear();
            data.clear();
            // 当前sheet的内容，需要转化为List<List<Object>>
            v.forEach(i -> {
                head.addAll(i.keySet()); // 每次循环添加表头，因为每一行的表头可能参差不齐
                List<Object> row = new ArrayList<>();
                head.forEach(j -> {
                    Object value = i.get(j);
                    if (value instanceof Number) // 防止变成科学计数
                        value = new BigDecimal(value.toString()).toPlainString();
                    row.add(StringUtils.strip(value));
                });
                data.add(row);
            });
            WriteSheet writeSheet = EasyExcel.writerSheet(sheetIndex.getAndIncrement(), k)
                    .head(head.stream().map(List::of).collect(Collectors.toList())) // 需要转化为List<List<String>>
                    .build();
            writer.write(data, writeSheet);
        });
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

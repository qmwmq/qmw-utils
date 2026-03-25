package io.github.qmwmq.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ooxml.POIXMLException;
import org.apache.poi.openxml4j.exceptions.OLE2NotOfficeXmlFileException;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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

    public static Workbook convertToWorkbook(InputStream stream) throws IOException {
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

    public static void downloadOnlyHeaders(Collection<String> headers, HttpServletResponse response, String fileName) {
        download(Collections.singletonMap("Sheet1", new ArrayList<>() {{
            add(new HashMap<>() {{
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

}

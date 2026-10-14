package io.github.qmwmq.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.Getter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ooxml.POIXMLException;
import org.apache.poi.openxml4j.exceptions.OLE2NotOfficeXmlFileException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;

/**
 * Excel解析工具
 */
public class ExcelReader {

    /**
     * 默认构造器
     */
    private ExcelReader() {
    }

    static void main() throws FileNotFoundException {
        File file = new File("C:\\Users\\12334\\Desktop\\xlsx.xlsx");
//        readAllSheets(new FileInputStream(file));
        List<Map<String, Object>> list = readFirstSheet(new FileInputStream(file));
//        list.forEach(System.out::println);
        System.out.println(list.size());
    }

    public static List<Map<String, Object>> readFirstSheet(InputStream stream) {
        DataListener dataListener = new DataListener();
        EasyExcel.read(stream, dataListener).sheet(0).headRowNumber(1).doRead();
        return dataListener.getDataMap().values().stream().findFirst().orElse(new ArrayList<>());
    }

    public static Map<String, List<Map<String, Object>>> readAllSheets(InputStream stream) {
        DataListener dataListener = new DataListener();
        EasyExcel.read(stream, dataListener).headRowNumber(1).doReadAll();
        return dataListener.getDataMap();
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

    public static class DataListener extends AnalysisEventListener<Map<Integer, Object>> {

        @Getter
        private final Map<String, List<Map<String, Object>>> dataMap = new LinkedHashMap<>();
        // 存储 Excel 表头：key=列索引，value=列名
        private final Map<Integer, String> headerMap = new HashMap<>();

        @Override
        public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
            headMap.forEach((k, v) -> headerMap.put(k, StringUtils.strip(v)));
        }

        @Override
        public void invoke(Map<Integer, Object> data, AnalysisContext context) {
            String sheetName = context.readSheetHolder().getSheetName();
            List<Map<String, Object>> list = dataMap.computeIfAbsent(sheetName, _ -> new ArrayList<>());
            list.add(new LinkedHashMap<>() {{
                headerMap.forEach((k, v) -> put(v, data.get(k)));
            }});
        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {
        }

    }
}

package io.github.qmwmq.utils;

import com.alibaba.excel.EasyExcel;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Servlet相关工具类
 */
public class ServletUtils {

    private ServletUtils() {
    }

    public static String getParamsString(HttpServletRequest request) {
        String separator = System.lineSeparator();
        if ("OPTIONS".equalsIgnoreCase(request.getMethod()))
            return "";

        String method = request.getMethod().toUpperCase();
        String uri = request.getRequestURI();

        // 拼接成字符串一次性打印出，防止多个请求的同时打印造成混杂
        StringBuilder builder = new StringBuilder();
        builder.append("[").append(method).append("]");
        builder.append(uri).append(separator);
        builder.append("params:").append(separator);
        request.getParameterMap().forEach((k, v) -> {
            String value = v.length == 1 ? v[0] : (Arrays.toString(v) + "(size=" + v.length + ")");
            builder.append("\t").append(k).append(": ").append(value).append(separator);
        });

        builder.append("headers:").append(separator);
        Enumeration<String> headers = request.getHeaderNames();
        while (headers.hasMoreElements()) {
            String name = headers.nextElement();
            List<String> values = Collections.list(request.getHeaders(name));
            String value = values.size() == 1 ? values.getFirst() : (values + "(size=" + values.size() + ")");
            builder.append("\t").append(name).append(": ").append(value).append(separator);
        }
        return builder.toString();
    }

    /**
     * 设置返回头
     *
     * @param response HttpServletResponse
     * @param header   名称
     * @param value    值
     */
    public static void setHeader(HttpServletResponse response, String header, String value) {
        response.setHeader("Access-Control-Expose-Headers", header);
        response.setHeader(header, value);
    }

    //    public void download(HttpServletResponse response) throws Exception {
//        ByteArrayOutputStream byteArrayOutputStream = PdfUtils.test2();
//        response.setContentType("application/x-download");
//        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
//        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
//        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("用户信息.pdf", StandardCharsets.UTF_8.name()));
//        ServletOutputStream stream = response.getOutputStream();
//        byteArrayOutputStream.writeTo(stream);
//        stream.close();
//        byteArrayOutputStream.close();
//    }

//    fileName = URLEncoder.encode(com.medibee.util.StringUtil.ifEmpty(fileName, UUID.randomUUID().toString()) + ".xlsx", StandardCharsets.UTF_8.name());
//                response.setContentType("application/x-download");
//                response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
//                response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
//    builder = EasyExcel.write(response.getOutputStream());
}

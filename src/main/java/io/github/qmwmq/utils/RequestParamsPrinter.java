package io.github.qmwmq.utils;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * 参数打印工具
 */
public class RequestParamsPrinter {

    /**
     * 屏蔽默认构造器
     */
    private RequestParamsPrinter() {
    }

    /**
     * 打印参数
     * @param request request对象
     */
    public static void print(HttpServletRequest request) {
        String separator = System.lineSeparator();
        if ("OPTIONS".equalsIgnoreCase(request.getMethod()))
            return;

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
        System.out.println(builder);
    }

}

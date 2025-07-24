package io.github.qmwmq.utils;

import jakarta.servlet.http.HttpServletRequest;

import java.util.stream.Stream;

/**
 * ip获取工具
 */
public class IPUtils {

    private IPUtils() {
    }

    /**
     * 获取ip地址
     *
     * @param request 请求
     * @return ip地址
     */
    public static String getIP(HttpServletRequest request) {
        return Stream.of(
                request.getHeader("X-Forwarded-For"),
                request.getHeader("Proxy-Client-IP"),
                request.getHeader("WL-Proxy-Client-IP"),
                request.getHeader("Http-Client-IP"),
                request.getHeader("X-Real-IP"),
                request.getRemoteAddr()
        ).filter(ip -> !notIp(ip)).findFirst().orElse("");
    }

    /**
     * 判断是否合法的ip
     *
     * @param ip ip地址
     * @return 是否ip
     */
    private static boolean notIp(String ip) {
        return StringUtils.isBlank(ip)
                || "null".equalsIgnoreCase(ip)
                || "unknown".equalsIgnoreCase(ip)
                || "0:0:0:0:0:0:0:1".equals(ip)
                || "0:0:0:0:0:0:0:0".equals(ip);
    }

}

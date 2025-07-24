package io.github.qmwmq.utils;

/**
 * 操作系统判断
 */
public class OSUtils {

    /**
     * 屏蔽默认构造器
     */
    private OSUtils() {
    }

    /**
     * 获取操作系统名称
     *
     * @return 操作系统名称
     */
    public static String getOSName() {
        return System.getProperty("os.name");
    }

    /**
     * 判断是否mac
     *
     * @return 是否mac
     */
    public static boolean isMac() {
        return getOSName().toLowerCase().contains("mac");
    }

    /**
     * 判断是否linux
     *
     * @return 是否linux
     */
    public static boolean isLinux() {
        return getOSName().toLowerCase().contains("linux");
    }

    /**
     * 判断是否windows
     *
     * @return 是否windows
     */
    public static boolean isWindows() {
        return getOSName().toLowerCase().contains("windows");
    }

}

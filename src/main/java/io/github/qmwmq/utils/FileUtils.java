package io.github.qmwmq.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Objects;

/**
 * 文件工具
 */
public class FileUtils {

    /**
     * 默认构造器
     */
    private FileUtils() {
    }

    public static String getFileType(String name) {
        return name != null && name.contains(".") ? name.substring(name.lastIndexOf(".") + 1) : "";
    }

    public static String getFileType(File file) {
        return getFileType(Objects.requireNonNull(file, "文件不能为空").getName());
    }

    public static String getFileType(MultipartFile file) {
        return getFileType(Objects.requireNonNull(file, "文件不能为空").getOriginalFilename());
    }

}

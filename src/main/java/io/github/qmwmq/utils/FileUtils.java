package io.github.qmwmq.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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

    // stream 用完了需要重置，构造一个新的ByteArrayInputStream这样可以复用
    public static String sha256Hex(ByteArrayInputStream stream) throws IOException {
        String hex = DigestUtils.sha256Hex(stream);
        stream.reset();
        return hex;
    }

    static void main() throws IOException {
        // 04f5fefad1989449ae3bfdda6f72c258f1e19c4e02b15821876039f15f73f1b8
        // 04f5fefad1989449ae3bfdda6f72c258f1e19c4e02b15821876039f15f73f1b8
        FileInputStream stream = new FileInputStream("C:\\Users\\12334\\Desktop\\pgsql\\tb_account.sql");
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(stream.readAllBytes());
        System.out.println(sha256Hex(byteArrayInputStream));
        System.out.println(sha256Hex(byteArrayInputStream));
        System.out.println("04f5fefad1989449ae3bfdda6f72c258f1e19c4e02b15821876039f15f73f1b8".equals("04f5fefad1989449ae3bfdda6f72c258f1e19c4e02b15821876039f15f73f1b8"));
    }

}

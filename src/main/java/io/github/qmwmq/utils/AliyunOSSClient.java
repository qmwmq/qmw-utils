package io.github.qmwmq.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

public class AliyunOSSClient implements AutoCloseable {

    private final Options options;
    private final OSS oss;

    /**
     * 注册为单利bean
     *
     * @param options 上传参数
     */
    public AliyunOSSClient(Options options) {
        if (options.endpoint.startsWith("http"))
            throw new IllegalArgumentException("endpoint must not start with http or https");
        this.options = options;
        this.oss = new OSSClientBuilder().build("https://" + options.endpoint, options.accessKeyId, options.accessKeySecret);
    }

    public Signature getSignature(String fileName, String fileHash) throws NoSuchAlgorithmException, InvalidKeyException {
        // 1. 生成 OSS 文件存储路径（两层哈希目录）
        String dir1 = fileHash.substring(0, 2);
        String dir2 = fileHash.substring(2, 4);
        String ossKey = dir1 + "/" + dir2 + "/" + fileHash + "." + FileUtils.getFileType(fileName);

        // 2. 生成上传策略（Policy）
        long expireEndTime = System.currentTimeMillis() + options.signatureExpireTime * 1000;
        Date expiration = new Date(expireEndTime);
        PolicyConditions policyConditions = new PolicyConditions();
        // 限制上传的 Bucket 和文件路径
        policyConditions.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1024 * 1024 * 100); // 限制文件大小 0-100MB
        policyConditions.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, ossKey);

        String postPolicy = this.oss.generatePostPolicy(expiration, policyConditions);
        byte[] binaryData = postPolicy.getBytes(StandardCharsets.UTF_8);
        String encodedPolicy = BinaryUtil.toBase64String(binaryData);

        // 3. 生成签名（核心：用 AccessKeySecret 加密 Policy）
        String signature = BinaryUtil.toBase64String(HmacSHA1Signature(encodedPolicy));

        // 4. 组装返回参数（前端上传时需要这些参数）
        return new Signature()
                .setOSSAccessKeyId(options.accessKeyId)
                .setPolicy(encodedPolicy)
                .setSignature(signature)
                .setKey(ossKey)
                .setHost("https://" + options.bucket + "." + options.endpoint);
    }

    public String upload(InputStream stream, String fileType) throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(stream.readAllBytes());
        String fileHash = FileUtils.sha256Hex(byteArrayInputStream);
        String dir1 = fileHash.substring(0, 2);
        String dir2 = fileHash.substring(2, 4);
        String ossKey = dir1 + "/" + dir2 + "/" + fileHash + "." + fileType;
        oss.putObject(options.bucket, ossKey, byteArrayInputStream);
        return "https://" + options.bucket + "." + options.endpoint + "/" + ossKey;
    }

    private byte[] HmacSHA1Signature(String data) throws NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(options.accessKeySecret.getBytes(StandardCharsets.UTF_8), "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(secretKeySpec);
        return mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 实现AutoCloseable接口，在销毁时自动关闭OSSClient
     */
    @Override
    public void close() {
        if (oss != null) {
            System.out.println("Closing OSSClient...");
            oss.shutdown(); // 仅在此处统一关闭OSSClient
        }
    }

    @Data
    @Accessors(chain = true)
    public static class Options {
        private String accessKeyId;
        private String accessKeySecret;
        private String bucket;
        private String endpoint;
        private long signatureExpireTime = 300;
    }

    // 前端上传时需要这些参数，参数名保持一致即可
    @Data
    @Accessors(chain = true)
    public static class Signature {
        private String OSSAccessKeyId;
        private String policy;
        private String signature;
        private String host;            // 上传地址
        private String key;             // 存储路径，例如aa/bb/cc.pdf
        private String url;             // 文件访问地址，上传完成后写入数据库需要用到

        public String getUrl() {
            return host + "/" + key;
        }
    }
}

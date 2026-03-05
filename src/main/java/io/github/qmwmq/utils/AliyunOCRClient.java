package io.github.qmwmq.utils;

import com.aliyun.ocr_api20210707.Client;
import com.aliyun.ocr_api20210707.models.RecognizeInvoiceRequest;
import com.aliyun.ocr_api20210707.models.RecognizeInvoiceResponse;
import com.aliyun.teaopenapi.models.Config;
import io.github.qmwmq.entity.OCRInvoice;
import lombok.Data;
import lombok.experimental.Accessors;
import tools.jackson.databind.JsonNode;

import java.io.InputStream;

public class AliyunOCRClient {

    private final Client client;

    /**
     * 注册为单利bean
     *
     * @param options 上传参数
     */
    public AliyunOCRClient(AliyunOCRClient.Options options) throws Exception {
        if (options.endpoint.startsWith("http"))
            throw new IllegalArgumentException("endpoint must not start with http or https");
        this.client = new Client(new Config()
                .setAccessKeyId(options.accessKeyId)
                .setAccessKeySecret(options.accessKeySecret)
                .setEndpoint(options.endpoint)
        );
    }

    public OCRInvoice readInvoice(InputStream stream) throws Exception {
        RecognizeInvoiceRequest request = new RecognizeInvoiceRequest().setBody(stream);
        RecognizeInvoiceResponse response = client.recognizeInvoice(request);
        JsonNode jsonNode = JacksonUtils.getJsonMapper().readTree(response.getBody().getData());
        return JacksonUtils.getJsonMapper().readValue(jsonNode.get("data").toString(), OCRInvoice.class);
    }

    @Data
    @Accessors(chain = true)
    public static class Options {
        private String accessKeyId;
        private String accessKeySecret;
        private String endpoint;
    }
}

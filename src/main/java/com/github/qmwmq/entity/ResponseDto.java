package com.github.qmwmq.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ResponseDto {

    /**
     * 默认构造器
     */
    public ResponseDto() {
        this.code = null;
        this.msg = null;
    }

    /**
     * 两个参数构造器
     *
     * @param code code
     * @param msg  msg
     */
    public ResponseDto(Code code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private final Code code;
    private final String msg;
    private Object data;

    public interface Code {
    }

    public enum CommonCode implements Code {
        SUCCESS,
        CUSTOM_ERROR,
        SYSTEM_ERROR,
        LOGIN_REQUIRED,
        LOGIN_EXPIRED,
    }

}

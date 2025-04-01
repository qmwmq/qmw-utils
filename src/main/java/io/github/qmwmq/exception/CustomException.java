package io.github.qmwmq.exception;

import io.github.qmwmq.entity.ResponseDto;
import lombok.Getter;

/**
 * 自定义异常
 */
@Getter
public class CustomException extends RuntimeException {

    private ResponseDto.Code code = ResponseDto.ConstantCode.CUSTOM_ERROR;

    /**
     * 抛出异常
     *
     * @param message 异常信息
     */
    public CustomException(String message) {
        super(message);
    }

    /**
     * 抛出异常
     *
     * @param code 异常code
     */
    public CustomException(ResponseDto.Code code) {
        super();
        this.code = code;
    }

    /**
     * 抛出异常
     *
     * @param code    异常code
     * @param message 异常信息
     */
    public CustomException(ResponseDto.Code code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * 抛出异常
     *
     * @param condition 抛出异常的情况
     * @param message   异常信息
     */
    public static void throwIf(boolean condition, String message) {
        if (condition)
            throw new CustomException(message);
    }

    /**
     * 抛出异常
     *
     * @param condition 抛出异常的情况
     * @param code      异常code
     */
    public static void throwIf(boolean condition, ResponseDto.Code code) {
        if (condition)
            throw new CustomException(code);
    }

    /**
     * 抛出异常
     *
     * @param condition 抛出异常的情况
     * @param code      异常code
     * @param message   异常信息
     */
    public static void throwIf(boolean condition, ResponseDto.Code code, String message) {
        if (condition)
            throw new CustomException(code, message);
    }

}

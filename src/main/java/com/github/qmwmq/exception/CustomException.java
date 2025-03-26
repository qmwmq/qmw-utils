package com.github.qmwmq.exception;

import com.github.qmwmq.entity.ResponseDto;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private ResponseDto.Code code = ResponseDto.CommonCode.CUSTOM_ERROR;

    public CustomException(String message) {
        super(message);
    }

    public CustomException(ResponseDto.Code code, String message) {
        super(message);
        this.code = code;
    }

    public static void throwIf(boolean condition, String message) {
        if (condition)
            throw new CustomException(message);
    }

    public static void throwIf(boolean condition, ResponseDto.Code code, String message) {
        if (condition)
            throw new CustomException(code, message);
    }

}

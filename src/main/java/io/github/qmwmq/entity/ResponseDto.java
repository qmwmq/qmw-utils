package io.github.qmwmq.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ResponseDto<T> {

    private Code code;
    private String msg;
    private T data;

    /**
     * 屏蔽默认构造器
     */
    private ResponseDto() {
    }

    /**
     * 静态成功方法
     *
     * @param msg  信息
     * @param data 数据
     * @param <T>  泛型
     * @return ResponseDto
     */
    public static <T> ResponseDto<T> ok(String msg, T data) {
        return new ResponseDto<T>()
                .setCode(ConstantCode.OK)
                .setMsg(msg)
                .setData(data);
    }

    /**
     * 静态成功方法
     *
     * @param data 数据
     * @param <T>  泛型
     * @return ResponseDto
     */
    public static <T> ResponseDto<T> ok(T data) {
        return ok(null, data);
    }

    /**
     * 静态成功方法
     *
     * @param <T> 泛型
     * @return ResponseDto
     */
    public static <T> ResponseDto<T> ok() {
        return ok(null, null);
    }

    /**
     * 静态失败方法
     *
     * @param code 失败码
     * @param msg  信息
     * @param data 有时失败也需要返回数据
     * @param <T>  泛型
     * @return ResponseDto
     */
    public static <T> ResponseDto<T> error(Code code, String msg, T data) {
        return new ResponseDto<T>()
                .setCode(code)
                .setMsg(msg)
                .setData(data);
    }

    /**
     * 静态失败方法
     *
     * @param code 失败码
     * @param msg  信息
     * @param <T>  泛型
     * @return ResponseDto
     */
    public static <T> ResponseDto<T> error(Code code, String msg) {
        return error(code, msg, null);
    }

    /**
     * 静态失败方法
     *
     * @param msg 信息
     * @param <T> 泛型
     * @return ResponseDto
     */
    public static <T> ResponseDto<T> error(String msg) {
        return error(null, msg, null);
    }

    public interface Code {
    }

    public enum ConstantCode implements Code {
        OK,
        CUSTOM_ERROR,
        SYSTEM_ERROR,
        LOGIN_REQUIRED,
        LOGIN_EXPIRED,
    }

}

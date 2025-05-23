package io.github.qmwmq.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 数据返回结果
 *
 * @param <T> 泛型
 */
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

    /**
     * 状态码接口，要扩展状态码需要enum该接口
     */
    public interface Code {
    }

    /**
     * 常用状态码
     */
    public enum ConstantCode implements Code {
        /**
         * 成功
         */
        OK,
        /**
         * 自定义错误
         */
        CUSTOM_ERROR,
        /**
         * 系统错误
         */
        SYSTEM_ERROR,
        /**
         * 需要登录
         */
        LOGIN_REQUIRED,
        /**
         * 登录过期
         */
        LOGIN_EXPIRED,
    }

}

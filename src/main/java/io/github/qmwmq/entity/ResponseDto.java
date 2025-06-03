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

    private int code;
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
                .setCode(ConstantStatus.OK.code())
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
        return ok(ConstantStatus.OK.msg(), data);
    }

    /**
     * 静态成功方法
     *
     * @param <T> 泛型
     * @return ResponseDto
     */
    public static <T> ResponseDto<T> ok() {
        return ok(ConstantStatus.OK.msg(), null);
    }

    /**
     * 静态失败方法
     *
     * @param status 失败状态
     * @param msg    信息
     * @param data   有时失败也需要返回数据
     * @param <T>    泛型
     * @return ResponseDto
     */
    public static <T> ResponseDto<T> error(Status status, String msg, T data) {
        return new ResponseDto<T>()
                .setCode(status.code())
                .setMsg(msg)
                .setData(data);
    }

    /**
     * 静态失败方法
     *
     * @param status 失败状态
     * @param msg    信息
     * @param <T>    泛型
     * @return ResponseDto
     */
    public static <T> ResponseDto<T> error(Status status, String msg) {
        return error(status, msg, null);
    }

    /**
     * 静态失败方法
     *
     * @param status 失败状态
     * @param <T>    泛型
     * @return ResponseDto
     */
    public static <T> ResponseDto<T> error(Status status) {
        return error(status, status.msg(), null);
    }

    /**
     * 静态失败方法
     *
     * @param msg 信息
     * @param <T> 泛型
     * @return ResponseDto
     */
    public static <T> ResponseDto<T> error(String msg) {
        return error(ConstantStatus.CUSTOM_ERROR, msg, null);
    }

    /**
     * 状态码接口，要扩展状态码需要enum该接口
     */
    public interface Status {
        int code();
        String msg();
    }

    /**
     * 常用状态码
     */
    public enum ConstantStatus implements Status {
        /**
         * 成功
         */
        OK(1, "操作成功"),
        /**
         * 系统错误
         */
        SYSTEM_ERROR(1000, "系统错误"),
        /**
         * 自定义错误
         */
        CUSTOM_ERROR(1001, "操作失败"),
        /**
         * 需要登录
         */
        LOGIN_REQUIRED(1002, "需要登录"),
        /**
         * 登录过期
         */
        LOGIN_EXPIRED(1003, "登录过期");

        private final int code;
        private final String msg;

        ConstantStatus(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        @Override
        public int code() {
            return code;
        }

        @Override
        public String msg() {
            return msg;
        }
    }

}

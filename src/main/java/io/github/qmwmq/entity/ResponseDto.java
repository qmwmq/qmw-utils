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
     * @param data 数据
     * @param <T>  泛型
     * @return ResponseDto
     */
    public static <T> ResponseDto<T> ok(T data) {
        return new ResponseDto<T>()
                .setCode(1)
                .setMsg("操作成功")
                .setData(data);
    }

    /**
     * 静态成功方法
     *
     * @param <T> 泛型
     * @return ResponseDto
     */
    public static <T> ResponseDto<T> ok() {
        return ok(null);
    }

    /**
     * 静态失败方法
     *
     * @param code 状态码
     * @param msg  信息
     * @param <T>  泛型
     * @return ResponseDto
     */
    public static <T> ResponseDto<T> error(int code, String msg) {
        return new ResponseDto<T>()
                .setCode(code)
                .setMsg(msg);
    }

    /**
     * 静态失败方法
     *
     * @param code 状态码
     * @param <T>  泛型
     * @return ResponseDto
     */
    public static <T> ResponseDto<T> error(int code) {
        return error(code, "操作失败");
    }

    /**
     * 静态失败方法
     *
     * @param msg 信息
     * @param <T> 泛型
     * @return ResponseDto
     */
    public static <T> ResponseDto<T> error(String msg) {
        return error(0, msg);
    }

}

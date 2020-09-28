package com.beinet.firstpg.controller.dto;

import lombok.Data;

/**
 * 响应数据
 */
@Data
public class Result {
    private int code;
    private String msg;
    private Object data;

    /**
     * 返回成功结果
     *
     * @param data 数据
     * @return 结果
     */
    public static Result success(Object data) {
        Result ret = new Result();
        ret.setCode(200);
        ret.setData(data);
        return ret;
    }

    /**
     * 返回成功结果
     *
     * @param msg 数据
     * @return 结果
     */
    public static Result error(String msg) {
        return error(500, msg);
    }

    /**
     * 返回错误结果
     *
     * @param code 错误码
     * @param msg  错误信息
     * @return 结果
     */
    public static Result error(int code, String msg) {
        Result ret = new Result();
        ret.setCode(code);
        ret.setMsg(msg);
        return ret;
    }
}

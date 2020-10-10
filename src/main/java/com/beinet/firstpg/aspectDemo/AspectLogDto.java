package com.beinet.firstpg.aspectDemo;

import lombok.Data;

@Data
public class AspectLogDto {
    /**
     * 调用类名
     */
    private String className;
    /**
     * 调用方法名
     */
    private String methodName;
    /**
     * 调用的参数
     */
    private String params;
    /**
     * 方法返回结果
     */
    private String result;
    /**
     * 执行时长，毫秒
     */
    private long executeTime;
    /**
     * 备注
     */
    private String remark;

    /**
     * 出现的异常
     */
    private String exp;
}

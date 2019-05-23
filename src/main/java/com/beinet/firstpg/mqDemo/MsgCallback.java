package com.beinet.firstpg.mqDemo;

import java.util.Map;

/**
 * 消息回调处理类
 * @param <T> 参数类型
 */
public interface MsgCallback<T> {
    /**
     * 消息回调处理方法
     * @param obj 参数
     */
    void callback(T obj, String routeKey, Map<String, Object> header);
}

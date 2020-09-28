package com.beinet.firstpg.controller;

import com.beinet.firstpg.controller.dto.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestControllerAdvice
@Slf4j
public class ControllerExceptionAdvice {

    /**
     * 其它ExceptionHandler不处理的异常，都归集到这里
     *
     * @param request  请求对象，此参数非必需
     * @param response 响应对象，此参数非必需
     * @param exp      异常
     * @return 输出给前端的对象
     */
    @ExceptionHandler(value = Exception.class)
    //@ResponseBody
    public Result ExceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception exp) {
        response.setStatus(501);
        log.error("出错了: " + exp.toString());
        return Result.error("全局错误: " + exp.getMessage());
    }

    /**
     * 只处理 NullPointerException 异常，其它不管
     *
     * @param request  请求对象，此参数非必需
     * @param response 响应对象，此参数非必需
     * @param exp      异常
     * @return 输出给前端的对象
     */
    @ExceptionHandler(value = NullPointerException.class)
    //@ResponseBody
    public Result NullExceptionHandler(HttpServletRequest request, HttpServletResponse response, NullPointerException exp) {
        response.setStatus(503);
        log.error("空引用出错了: " + exp.toString());
        return Result.error("空引用错误: " + exp.getMessage());
    }

}

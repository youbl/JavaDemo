package com.beinet.firstpg.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.List;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class ResponseLogAdvice implements ResponseBodyAdvice<Object> {
    private static ObjectMapper mapper = new ObjectMapper();

    /**
     * 返回true表示要执行这个Advice
     *
     * @param methodParameter methodParameter
     * @param aClass          aClass
     * @return 是否
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    /**
     * 在输出流给客户端之前调用
     *
     * @param body               输出对象
     * @param methodParameter    方法参数
     * @param mediaType          响应类型
     * @param aClass             消息转换类
     * @param serverHttpRequest  请求上下文
     * @param serverHttpResponse 响应上下文
     * @return 输出对象
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType mediaType,
                                  Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        StringBuilder sb = new StringBuilder();
        sb.append(serverHttpRequest.getMethodValue())
                .append(" ")
                .append(serverHttpRequest.getURI().toString());

        if (serverHttpResponse instanceof ServletServerHttpResponse) {
            sb.append(" 响应: ")
                    .append(((ServletServerHttpResponse) serverHttpResponse).getServletResponse().getStatus());
        }
        HttpHeaders headers = serverHttpResponse.getHeaders();
        if (!headers.isEmpty()) {
            sb.append("\n  响应Header:");
            for (Map.Entry<String, List<String>> item : serverHttpResponse.getHeaders().entrySet()) {
                for (String val : item.getValue()) {
                    sb.append("\n")
                            .append(item.getKey())
                            .append(" : ")
                            .append(" ").append(val);
                }
            }
        }
        if (body == null) {
            sb.append("\n  无返回内容");
        } else {
            try {
                sb.append("\n  响应内容:\n")
                        .append(mapper.writeValueAsString(body));
            } catch (JsonProcessingException e) {
                sb.append("\n  序列化出错:")
                        .append(e.getMessage());
            }
        }
        log.info(sb.toString());

        // 需要的话，可以对body进行编辑后，再返回
        return body;
    }
}

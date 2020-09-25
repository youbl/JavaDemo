package com.beinet.firstpg.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Objects;

/**
 * 因为 RequestBodyAdvice 只支持带 RequestBody注解的方法，不支持GET或其它没有RequestBody注解的方法。
 * 所以要用Filter
 */
@Configuration
@Slf4j
public class RequestLogFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        long startTime = System.currentTimeMillis();
        if (!(request instanceof ContentCachingRequestWrapper)) {
            // 解决 inputStream 只能读取一次的问题
            request = new ContentCachingRequestWrapper(request);
        }
        if (!(response instanceof ContentCachingResponseWrapper)) {
            // 同样用于解决 响应只能读取一次的问题，注意要在最后调用 responseWrapper.copyBodyToResponse();
            response = new ContentCachingResponseWrapper(response);
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            long latency = System.currentTimeMillis() - startTime;
            doLog(request, response, latency);

            repairResponse(response);
        }
    }

    private void doLog(HttpServletRequest request, HttpServletResponse response, long latency) {
        StringBuilder sb = new StringBuilder();
        try {
            sb.append(getRequestMsg(request));

            sb.append("\n  响应 ")
                    .append(response.getStatus())
                    .append("  耗时 ")
                    .append(latency)
                    .append("ms\n");

            sb.append(getResponseMsg(response));

            logger.info(sb.toString());
        } catch (Exception exp) {
            sb.append("\n").append(exp.getMessage());
            logger.error(sb.toString());
        }
    }

    private static String getRequestMsg(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        String query = request.getQueryString();
        if (StringUtils.isNotEmpty(query)) {
            query = "?" + query;
        }
        sb.append(request.getMethod())
                .append(" ")
                .append(request.getRequestURL())
                .append(query)
                .append("\n  用户IP: ")
                .append(request.getRemoteAddr())
                .append("\n  请求Header:");

        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String header = headerNames.nextElement();
            sb.append("\n")
                    .append(header)
                    .append(" : ")
                    .append(" ").append(request.getHeader(header));
        }
        String requestBody = readFromStream(request.getInputStream());
        if (StringUtils.isNotEmpty(requestBody)) {
            sb.append("\n  请求体:\n")
                    .append(requestBody);
        }
        return sb.toString();
    }

    private static String getResponseMsg(HttpServletResponse response) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder("  响应Header: ");
        for (String header : response.getHeaderNames()) {
            sb.append("\n")
                    .append(header)
                    .append(" : ")
                    .append(" ").append(response.getHeader(header));
        }

        ContentCachingResponseWrapper wrapper = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        if (wrapper != null) {
            String responseBody = transferFromByte(wrapper.getContentAsByteArray(), wrapper.getCharacterEncoding());
            if (StringUtils.isNotEmpty(responseBody)) {
                sb.append("\n  响应Body:\n")
                        .append(responseBody);
            } else {
                sb.append("\n  无响应Body.");
            }
        }
        return sb.toString();
    }

    private static void repairResponse(HttpServletResponse response) throws IOException {
        ContentCachingResponseWrapper responseWrapper = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        Objects.requireNonNull(responseWrapper).copyBodyToResponse();
    }

    private static String readFromStream(InputStream stream) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = stream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toString(StandardCharsets.UTF_8.name());
        // return new BufferedReader(new InputStreamReader(stream)).lines().collect(Collectors.joining(System.lineSeparator()));
    }

    private static String transferFromByte(byte[] arr, String encoding) throws UnsupportedEncodingException {
        return new String(arr, encoding);
    }
}

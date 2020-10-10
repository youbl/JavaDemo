package com.beinet.firstpg.aspectDemo;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;

/**
 * 设置aop切面，并进行日志记录的服务类
 */
@Service
@Slf4j
@Aspect
public class AspectLogService {
    private ObjectMapper mapper = new ObjectMapper();

    @Pointcut("@annotation(com.beinet.firstpg.aspectDemo.NeedLog)")
    public void logPointcut() {
        // do nothing because @Pointcut
    }

    @Around("logPointcut()")
    public Object logAround(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        Object result = null;
        Exception exception = null;
        try {
            result = point.proceed();
        } catch (Exception exp) {
            exception = exp;
        }
        long time = System.currentTimeMillis() - beginTime;
        saveLog(point, result, exception, time);

        if (exception != null) {
            throw exception;
        }
        return result;
    }

    private void saveLog(ProceedingJoinPoint joinPoint, Object result, Exception exception, long time) {
        AspectLogDto dto = new AspectLogDto();
        dto.setExecuteTime(time);

        try {
            if (exception != null) {
                dto.setExp(exception.toString());
            }

            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            NeedLog annotation = method.getAnnotation(NeedLog.class);
            if (annotation != null) {
                //注解上的描述
                dto.setRemark(annotation.value());
            }

            //请求的 类名、方法名
            String className = joinPoint.getTarget().getClass().getName();
            dto.setClassName(className);

            String methodName = signature.getName();
            dto.setMethodName(methodName);

            //请求的参数
            Object[] args = joinPoint.getArgs();
            if (args != null && args.length > 0) {
                dto.setParams(mapper.writeValueAsString(args));
            }

            if (result != null) {
                dto.setResult(mapper.writeValueAsString(result));
            }
        } catch (Exception e) {
            dto.setExp(e.getMessage());
        }
        if (exception != null) {
            log.warn(dto.toString());
        } else {
            log.info(dto.toString());
        }
    }
}

package com.beinet.firstpg.aspectDemo;

import java.lang.annotation.*;

/**
 * 用了这个注解的方法，都应该记录info日志
 */
@Target(ElementType.METHOD) // 表示NeedLog注解只能用于方法
@Retention(RetentionPolicy.RUNTIME) // 修饰注解的生命周期，RUNTIME表示jvm加载class文件后依旧存在
@Documented
public @interface NeedLog {
    String value() default "";
}

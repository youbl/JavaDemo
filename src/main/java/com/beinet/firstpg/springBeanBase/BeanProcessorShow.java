package com.beinet.firstpg.springBeanBase;

import com.beinet.firstpg.configs.FeignConfiguration;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/**
 * 展示创建了哪些Bean
 */
@Component
public class BeanProcessorShow implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if(bean instanceof FeignConfiguration){
            System.out.println(beanName);
            System.out.println(((FeignConfiguration)bean).getIdx());
            ((FeignConfiguration)bean).setIdx(456);
            // 修改一个bean
            return new FeignConfiguration(98765);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if(bean instanceof FeignConfiguration){
            System.out.println(beanName);
            System.out.println(((FeignConfiguration)bean).getIdx());
            ((FeignConfiguration)bean).setIdx(789);
        }
        return bean;
    }
}

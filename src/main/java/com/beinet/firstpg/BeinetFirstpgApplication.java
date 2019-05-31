package com.beinet.firstpg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.io.IOException;

@EnableSwagger2  // 根据SpringBootApplication的 scanBasePackages参数配置，进行扫描，默认扫描当前类所在package，就是 com.beinet.firstpg
@SpringBootApplication // (scanBasePackages = "com.beinet.firstpg.controller.demo")
@EnableFeignClients
public class BeinetFirstpgApplication {

    public static void main(String[] args) {

        // testExcelHelper();
        SpringApplication.run(BeinetFirstpgApplication.class, args);
    }


    static void testExcelHelper() throws IOException {
        System.out.println(com.beinet.firstmaven.ExcelHelper.readExcel("D:\\work\\1.xlsx", 1, 0));
    }
}

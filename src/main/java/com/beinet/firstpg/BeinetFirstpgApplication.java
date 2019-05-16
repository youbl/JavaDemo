package com.beinet.firstpg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.io.IOException;

@SpringBootApplication
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

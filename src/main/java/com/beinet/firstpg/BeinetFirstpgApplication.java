package com.beinet.firstpg;

import com.beinet.firstmaven.ExcelHelper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.io.IOException;

@SpringBootApplication
@EnableFeignClients
public class BeinetFirstpgApplication {

    public static void main(String[] args) throws IOException {

        System.out.println(ExcelHelper.readExcel("D:\\work\\1.xlsx", 1, 0));
        SpringApplication.run(BeinetFirstpgApplication.class, args);
    }

}

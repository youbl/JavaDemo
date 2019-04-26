package com.beinet.firstpg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class BeinetFirstpgApplication {

    public static void main(String[] args) {

        SpringApplication.run(BeinetFirstpgApplication.class, args);
    }

}

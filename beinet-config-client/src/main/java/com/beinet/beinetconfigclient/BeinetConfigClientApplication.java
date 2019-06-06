package com.beinet.beinetconfigclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BeinetConfigClientApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(BeinetConfigClientApplication.class, args);
    }

    @Value("${Site.Name}")
    private String configValue;

    @Override
    public void run(String... args) {
        System.out.println(configValue);
    }
}

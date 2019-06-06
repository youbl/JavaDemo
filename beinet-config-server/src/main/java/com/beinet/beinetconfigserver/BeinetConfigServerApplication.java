package com.beinet.beinetconfigserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class BeinetConfigServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BeinetConfigServerApplication.class, args);
    }

}

package com.laterna.xaxaxa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class XaxaxaApplication {

    public static void main(String[] args) {
        SpringApplication.run(XaxaxaApplication.class, args);
    }

}

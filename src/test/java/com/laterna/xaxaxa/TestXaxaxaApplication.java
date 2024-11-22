package com.laterna.xaxaxa;

import org.springframework.boot.SpringApplication;

public class TestXaxaxaApplication {

    public static void main(String[] args) {
        SpringApplication.from(XaxaxaApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}

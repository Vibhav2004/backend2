package com.swipenow.swipenow;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EntityScan("com.swipenow.swipenow.entity")
public class SwipenowApplication {
    public static void main(String[] args) {

        SpringApplication.run(SwipenowApplication.class, args);
    }
}




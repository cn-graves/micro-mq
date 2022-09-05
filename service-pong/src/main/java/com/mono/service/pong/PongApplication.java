package com.mono.service.pong;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Application Starter
 *
 * @author Mono 2022/8/31 14:19 gralves@163.com
 */
@EnableScheduling
@SpringBootApplication
public class PongApplication {

    /**
     * application entry
     *
     * @param args args
     */
    public static void main(String[] args) {
        // start application
        SpringApplication.run(PongApplication.class, args);
    }
}

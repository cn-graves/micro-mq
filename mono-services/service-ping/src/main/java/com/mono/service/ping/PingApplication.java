package com.mono.service.ping;

import cn.hutool.core.net.NetUtil;
import com.mono.component.common.constant.AppConst;
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
public class PingApplication {

    /**
     * application entry
     *
     * @param args args
     */
    public static void main(String[] args) {
        // get an available random port
        int port = NetUtil.getUsableLocalPort();
        // set server port as random port
        System.setProperty(AppConst.SERVER_PORT, String.valueOf(port));
        // start application
        SpringApplication.run(PingApplication.class, args);
    }
}

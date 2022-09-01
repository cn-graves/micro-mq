package com.mono.service.pong.handler;

import java.util.HashSet;

/**
 * handler mapping
 *
 * @author Mono 2022/9/1 22:24 gralves@163.com
 */
public class HandleMapping {

    /**
     * forbidden instantiate by constructor
     */
    private HandleMapping() {
        throw new RuntimeException();
    }

    private volatile static HashSet<String> instance;

    /**
     * getInstance
     *
     * @return instance
     * @author Mono 2022/9/1 22:26 gralves@163.com
     */
    public static HashSet<String> getInstance() {
        if (instance == null) {
            synchronized (HandleMapping.class) {
                if (instance == null) {
                    instance = new HashSet<>();
                }
            }
        }
        return instance;
    }
}

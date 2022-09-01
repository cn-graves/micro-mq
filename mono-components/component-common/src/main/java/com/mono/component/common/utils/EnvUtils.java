package com.mono.component.common.utils;

import cn.hutool.extra.spring.SpringUtil;

/**
 * Spring Enviroment Utils
 *
 * @author Mono 2022/9/1 21:30 gralves@163.com
 */
public class EnvUtils {

    /**
     * get target folder path
     *
     * @return target folder path
     * @author Mono 2022/9/1 21:31 gralves@163.com
     */
    public static String getTargetFolder() {
        return SpringUtil.getProperty("common.folder.path");
    }

    /**
     * get by custom key
     *
     * @param propertyKey propertyKey
     * @return value
     * @author Mono 2022/9/1 22:04 gralves@163.com
     */
    public static String getProperties(String propertyKey) {
        return SpringUtil.getProperty("common.folder.path");
    }
}

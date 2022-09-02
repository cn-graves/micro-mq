package com.mono.component.common.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.mono.component.common.constant.AppConst;
import org.springframework.boot.web.reactive.context.StandardReactiveWebEnvironment;

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
        return FileUtil.getUserHomePath() + getProperties(AppConst.COMMON_FOLDER);
    }

    /**
     * get by custom key
     *
     * @param propertyKey propertyKey
     * @return value
     * @author Mono 2022/9/1 22:04 gralves@163.com
     */
    public static String getProperties(String propertyKey) {
        String res = null;
        res = SpringUtil.getProperty(propertyKey);
        if (ObjectUtil.isEmpty(res)) {
            StandardReactiveWebEnvironment environment = SpringUtil.getBean(StandardReactiveWebEnvironment.class);
            if (null != environment) {
                res = environment.getProperty(propertyKey);
            }
        }
        return res;
    }
}

package com.mono.component.common.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.mono.component.common.constant.AppConst;
import org.springframework.boot.web.reactive.context.StandardReactiveWebEnvironment;

/**
 * Spring env Utils
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
        String folder = getProperties(AppConst.COMMON_FOLDER);
        folder = ObjectUtil.isNotEmpty(folder) ? folder : "";
        return FileUtil.getUserHomePath() + folder;
    }

    /**
     * get by custom key
     *
     * @param propertyKey propertyKey
     * @return value
     * @author Mono 2022/9/1 22:04 gralves@163.com
     */
    public static String getProperties(String propertyKey) {
        String res = SpringUtil.getProperty(propertyKey);
        if (ObjectUtil.isEmpty(res)) {
            if (ObjectUtil.isNotEmpty(SpringUtil.getApplicationContext())) {
                StandardReactiveWebEnvironment environment = SpringUtil.getBean(StandardReactiveWebEnvironment.class);
                if (null != environment) {
                    res = environment.getProperty(propertyKey);
                }
            }
        }
        return res;
    }
}
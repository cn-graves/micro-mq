package com.mono.component.common.config;

import cn.hutool.core.io.FileUtil;
import com.mono.component.common.utils.EnvUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

@Slf4j
public class FolderExistChecker implements ApplicationListener<ApplicationReadyEvent> {

    /**
     * check folder exist while application is ready
     *
     * @param event ready evt
     */
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        String targetFolder = EnvUtils.getTargetFolder();
        if (!FileUtil.exist(targetFolder)) {
            FileUtil.mkdir(targetFolder);
        }
    }
}

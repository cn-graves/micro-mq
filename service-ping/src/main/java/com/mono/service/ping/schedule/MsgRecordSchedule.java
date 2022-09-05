package com.mono.service.ping.schedule;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.mono.component.common.utils.EnvUtils;
import groovy.util.logging.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

/**
 * Message record schedule
 * <p>
 * write message to folder every 1 second and name file by timestamp
 *
 * @author Mono 2022/9/1 21:03 gralves@163.com
 */
@Slf4j
@Component
public class MsgRecordSchedule {

    private static final String PAYLOAD = "hello";

    /**
     * task runner
     *
     * <font color='red'>* * * * * ?</font> => running every 1 second
     *
     * @author Mono 2022/9/1 21:05 gralves@163.com
     */
    @Scheduled(cron = "* * * * * ?")
    public void invoke() throws IOException {
        // get folder
        String targetFolder = EnvUtils.getTargetFolder();
        // current timestamp
        long timestamp = System.currentTimeMillis();
        // write content
        File file = new File(targetFolder + StrUtil.SLASH + timestamp);
        if (file.createNewFile()) {
            FileUtil.writeUtf8String(PAYLOAD, file);
        }
    }
}

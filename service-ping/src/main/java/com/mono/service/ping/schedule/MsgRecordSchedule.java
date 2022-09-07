package com.mono.service.ping.schedule;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.mono.component.common.cache.CacheManager;
import com.mono.component.common.constant.RedisConsts;
import com.mono.component.common.utils.EnvUtils;
import groovy.util.logging.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;

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

    private final CacheManager cacheManager;

    public MsgRecordSchedule(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    /**
     * task runner
     *
     * <font color='red'>* * * * * ?</font> => running every 1 second
     *
     * @author Mono 2022/9/1 21:05 gralves@163.com
     */
    @Scheduled(cron = "* * * * * ?")
    public void invoke() {
        // get folder
        String targetFolder = EnvUtils.getTargetFolder();
        // current timestamp
        long timestamp = System.currentTimeMillis();
        // generate request id
        String requestId = IdUtil.getSnowflakeNextIdStr();
        try {
            // setnx by lua
            if (cacheManager.distributedLock(String.valueOf(timestamp), requestId, RedisConsts.DEFAULT_EXPIRE_SECOND)) {
                // write content
                File file = new File(targetFolder + StrUtil.SLASH + timestamp);
                FileUtil.writeUtf8String(PAYLOAD, file);
            }
        } finally {
            // del key by lua
            cacheManager.unlock(String.valueOf(timestamp), requestId);
        }
    }
}

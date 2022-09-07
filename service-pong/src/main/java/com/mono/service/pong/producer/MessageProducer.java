package com.mono.service.pong.producer;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import com.lmax.disruptor.RingBuffer;
import com.mono.component.common.msg.FileMessage;
import com.mono.component.common.utils.EnvUtils;
import com.mono.service.pong.utils.LocalCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Message Producer
 *
 * @author mono 2022/9/5 10:29 Gralves@163.com
 */
@Configuration
public class MessageProducer implements ApplicationListener<ApplicationReadyEvent> {

    LocalCacheManager manager = new LocalCacheManager();

    Logger logger = LoggerFactory.getLogger(MessageProducer.class);

    private final RingBuffer<FileMessage> ringBuffer;

    public MessageProducer(RingBuffer<FileMessage> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    /**
     * start scan files in folder while application is ready
     *
     * @param event ready evt
     * @author Mono 2022/9/1 21:49 gralves@163.com
     */
    @Override
    public void onApplicationEvent(@SuppressWarnings("NullableProblems") ApplicationReadyEvent event) {
        String targetFolder = EnvUtils.getTargetFolder();
        ScheduledThreadPoolExecutor scheduledExecutor = ThreadUtil.createScheduledExecutor(1);
        scheduledExecutor.scheduleAtFixedRate(() -> {
            List<File> files = FileUtil.loopFiles(targetFolder);
            Optional.ofNullable(files).filter(ObjectUtil::isNotEmpty).ifPresent(fileList -> {
                for (File file : fileList) {
                    if (file.exists()) {
                        long sequence = ringBuffer.next();
                        try {
                            long fileName = Long.parseLong(file.getName());
                            if (!manager.containsKey(fileName)) {
                                manager.putAndPublish(fileName, file, f -> {
                                    ringBuffer.get(sequence).setPayload(FileUtil.readString(f, StandardCharsets.UTF_8));
                                    ringBuffer.publish(sequence);
                                    FileUtil.del(f);
                                });
                            }
                        } catch (Exception e) {
                            logger.error("File Perhaps has been removed, ErrorInfo: {}", e.getMessage());
                        }
                    }
                }
            });
        }, 0, 1, TimeUnit.MILLISECONDS);
    }
}

package com.mono.service.pong.producer;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import com.lmax.disruptor.RingBuffer;
import com.mono.component.common.msg.FileMessage;
import com.mono.component.common.utils.EnvUtils;
import com.mono.service.pong.handler.LocalCacheHandler;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Message Producer
 *
 * @author mono 2022/9/5 10:29 Gralves@163.com
 */
@Configuration
public class MessageProducer implements ApplicationListener<ApplicationReadyEvent> {

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
            ConcurrentHashMap<Long, FileMessage> instance = LocalCacheHandler.getInstance();
            List<File> files = FileUtil.loopFiles(targetFolder);
            Optional.ofNullable(files).filter(ObjectUtil::isNotEmpty).ifPresent(fileList -> {
                fileList.sort((o1, o2) -> Integer.parseInt(String.valueOf(Long.parseLong(o1.getName()) - Long.parseLong(o2.getName()))));
                for (File file : fileList) {
                    long sequence = ringBuffer.next();
                    FileMessage fileMessage = ringBuffer.get(sequence).setError(Boolean.FALSE);
                    try {
                        if (file.exists()) {
                            long key = file.getAbsolutePath().hashCode();
                            if (!instance.containsKey(key)) {
                                instance.put(key, fileMessage);
                                String payload = FileUtil.readString(file, StandardCharsets.UTF_8);
                                fileMessage.setPayload(payload).setPath(file.getAbsolutePath());
                            }
                        }
                    } catch (Exception ex) {
                        fileMessage.setPayload(ex.getMessage()).setError(Boolean.TRUE);
                    } finally {
                        fileMessage.setSequence(sequence);
                        ringBuffer.publish(sequence);
                    }
                }
            });
        }, 0, 1, TimeUnit.MILLISECONDS);
    }
}

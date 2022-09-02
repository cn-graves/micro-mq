package com.mono.service.pong.producer;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.NamedThreadFactory;
import cn.hutool.core.util.ObjectUtil;
import com.lmax.disruptor.RingBuffer;
import com.mono.component.common.models.msg.FileMessage;
import com.mono.component.common.utils.EnvUtils;
import com.mono.service.pong.handler.HandleMapping;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class MessageProducer implements ApplicationListener<ApplicationReadyEvent> {

    public static ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1, new NamedThreadFactory("MessageProducer-", Boolean.TRUE));

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
        // get scan target
        String targetFolder = EnvUtils.getTargetFolder();
        // start
        Optional.of(targetFolder).filter(ObjectUtil::isNotEmpty).ifPresent(folder -> executorService.scheduleAtFixedRate(() -> {
            try {
                // loop scan files
                List<File> files = FileUtil.loopFiles(targetFolder);
                if (ObjectUtil.isNotEmpty(files)) {
                    // sort asc by file name
                    files.sort((o1, o2) -> Integer.parseInt(String.valueOf(Long.parseLong(o1.getName()) - Long.parseLong(o2.getName()))));
                    // choose the oldest one as msg
                    File fileMsg = CollectionUtil.getFirst(files);
                    long next = ringBuffer.next();
                    try {
                        if (fileMsg.exists()) {
                            String fileName = fileMsg.getName();
                            HandleMapping.getInstance().add(fileName);
                            String payload = FileUtil.readString(fileMsg, StandardCharsets.UTF_8);
                            ringBuffer.get(next).setFileName(fileName).setPayload(payload).setRealPath(FileUtil.getAbsolutePath(fileMsg));
                        }
                    } catch (Exception ignore) {

                    } finally {
                        ringBuffer.publish(next);
                    }
                }
            } catch (Exception ignore) {

            }
        }, 0, 1, TimeUnit.MICROSECONDS));
    }
}

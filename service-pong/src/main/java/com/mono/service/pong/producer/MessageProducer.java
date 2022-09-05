package com.mono.service.pong.producer;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.NamedThreadFactory;
import cn.hutool.core.util.ObjectUtil;
import com.lmax.disruptor.RingBuffer;
import com.mono.component.common.msg.FileMessage;
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

/**
 * Message Producer
 *
 * @author mono 2022/9/5 10:29 Gralves@163.com
 */
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
        String targetFolder = EnvUtils.getTargetFolder();
        Optional.of(targetFolder).filter(ObjectUtil::isNotEmpty).ifPresent(folder -> {
            long next = ringBuffer.next();
            FileMessage fileMessage = ringBuffer.get(next);
            executorService.scheduleAtFixedRate(() -> {
                try {
                    List<File> files = FileUtil.loopFiles(targetFolder);
                    Optional.ofNullable(files).filter(ObjectUtil::isNotEmpty).ifPresent(fileList -> {
                        fileList.sort((o1, o2) -> Integer.parseInt(String.valueOf(Long.parseLong(o1.getName()) - Long.parseLong(o2.getName()))));
                        File fileMsg = CollectionUtil.getFirst(fileList);
                        if (fileMsg.exists()) {
                            String fileName = fileMsg.getName();
                            HandleMapping.getInstance().add(fileName);
                            String payload = FileUtil.readString(fileMsg, StandardCharsets.UTF_8);
                            fileMessage.setFileName(fileName).setPayload(payload).setRealPath(FileUtil.getAbsolutePath(fileMsg));
                        }
                    });
                } catch (Exception ex) {
                    fileMessage.setPayload(ex.getMessage());
                } finally {
                    ringBuffer.publish(next);
                }
            }, 0, 1, TimeUnit.MICROSECONDS);
        });
    }
}

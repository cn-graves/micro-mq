package com.mono.service.pong.producer;

import cn.hutool.core.io.FileUtil;
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
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

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
    public void onApplicationEvent(ApplicationReadyEvent event) {
        // get scan target
        String targetFolder = EnvUtils.getTargetFolder();
        // start
        Optional.ofNullable(targetFolder).filter(ObjectUtil::isNotEmpty).ifPresent(folder -> {
            //noinspection InfiniteLoopStatement
            while (true) {
                // loop scan files
                List<File> files = FileUtil.loopFiles(targetFolder, pathname -> {
                    // file name not in handle mapping
                    return !HandleMapping.getInstance().contains(pathname.getName());
                });
                if (ObjectUtil.isNotEmpty(files)) {
                    // sort asc by file name
                    files.sort(Comparator.comparingInt(o -> Integer.parseInt(o.getName())));
                    for (File file : files) {
                        if (file.exists()) {
                            long next = ringBuffer.next();
                            try {
                                String fileName = file.getName();
                                HandleMapping.getInstance().add(fileName);
                                String payload = FileUtil.readString(file, StandardCharsets.UTF_8);
                                ringBuffer.get(next).setFileName(fileName).setPayload(payload).setRealPath(FileUtil.getAbsolutePath(file));
                            } finally {
                                ringBuffer.publish(next);
                            }
                        }
                    }
                }
            }
        });
    }
}

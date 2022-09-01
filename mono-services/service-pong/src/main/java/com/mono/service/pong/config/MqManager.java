package com.mono.service.pong.config;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.mono.component.common.constant.AppConst;
import com.mono.component.common.models.msg.FileMessage;
import com.mono.component.common.utils.EnvUtils;
import com.mono.service.pong.handler.FileMessageHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ThreadFactory;

/**
 * Message configuration
 *
 * @author Mono 2022/9/1 22:00 gralves@163.com
 */
@Configuration
public class MqManager {

    /**
     * ringBuffer initializer
     *
     * @return ringBuffer
     * @author Mono 2022/9/1 22:14 gralves@163.com
     */
    @Bean
    public RingBuffer<FileMessage> bufferInit() {
        // get buffer size
        String bufferSize = EnvUtils.getProperties(AppConst.BUFFER_SIZE);
        // create thread factory
        ThreadFactory threadFactory = ThreadUtil.createThreadFactory("BufferRing-");
        EventFactory<FileMessage> factory = FileMessage::getInstance;
        Disruptor<FileMessage> disruptor = new Disruptor<>(factory, Integer.parseInt(bufferSize), threadFactory, ProducerType.SINGLE, new BlockingWaitStrategy());
        disruptor.handleEventsWith(SpringUtil.getBean(FileMessageHandler.class));
        // start disruptor thread
        disruptor.start();
        return disruptor.getRingBuffer();
    }
}

package com.mono.service.pong.producer

import cn.hutool.core.thread.ThreadUtil
import com.google.common.util.concurrent.ListenableFuture
import com.mono.component.common.utils.AsyncExecutorUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import spock.lang.Specification

@AutoConfigureMockMvc
@TestPropertySource(value = "classpath:application.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MessageProducerTest extends Specification {

    @Autowired
    private MessageProducer producer;

    def "handlerMappingTest"() {
        expect: "Target Folder is not empty and scan files success and publish message by producer success";
        // submit future task , this is a loop thread, need interrupt by cancel call
        ListenableFuture<Object> future = AsyncExecutorUtils.submit(() -> {
            // scan file and publish
            producer.onApplicationEvent(null);
            return null;
        });
        ThreadUtil.safeSleep(1000L);
        if (null != future && !future.isCancelled()) {
            // cancel task or it will loop forever
            future.cancel(Boolean.TRUE);
        }
    }
}

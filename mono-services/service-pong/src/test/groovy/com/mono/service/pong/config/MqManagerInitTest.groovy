package com.mono.service.pong.config

import cn.hutool.core.lang.Assert
import cn.hutool.extra.spring.SpringUtil
import com.lmax.disruptor.RingBuffer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import spock.lang.Specification

@AutoConfigureMockMvc
@TestPropertySource(value = "classpath:application.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MqManagerInitTest extends Specification {

    @Autowired
    private MqManager mqManager;

    def "bufferInitTest"() {
        expect: "RingBuffer is not null";
        // invoke method
        mqManager.bufferInit();
        // bean exist
        Assert.notNull(SpringUtil.getBean(RingBuffer.class));
    }
}

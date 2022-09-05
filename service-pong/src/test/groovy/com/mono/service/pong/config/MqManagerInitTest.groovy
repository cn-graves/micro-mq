package com.mono.service.pong.config


import cn.hutool.core.util.ObjectUtil
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
    private MqManager mqManager

    def "bufferInitTest"() {
        mqManager.bufferInit()
        expect:
        ObjectUtil.isNotNull(SpringUtil.getBean(RingBuffer.class))
    }
}

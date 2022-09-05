package com.mono.service.pong.handler

import com.mono.component.common.msg.FileMessage
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import spock.lang.Specification

import java.util.concurrent.ConcurrentHashMap

@AutoConfigureMockMvc
@TestPropertySource(value = "classpath:application.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HandlerMappingTest extends Specification {

    def "handlerMappingTest"() {
        ConcurrentHashMap<Long, FileMessage> mapping = LocalCacheHandler.getInstance()
        expect:
        null != mapping
    }
}

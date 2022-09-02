package com.mono.service.pong.handler

import cn.hutool.core.lang.Assert
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import spock.lang.Specification

@AutoConfigureMockMvc
@TestPropertySource(value = "classpath:application.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HandlerMappingTest extends Specification {

    def "handlerMappingTest"() {
        expect: "get instance success and instance is not null";
        // invoke method
        HashSet<String> set = HandleMapping.getInstance()
        // bean exist
        Assert.isTrue(null != set);
    }
}

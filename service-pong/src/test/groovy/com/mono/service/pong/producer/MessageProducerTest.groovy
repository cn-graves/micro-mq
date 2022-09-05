package com.mono.service.pong.producer

import cn.hutool.core.io.FileUtil
import cn.hutool.core.thread.ThreadUtil
import cn.hutool.core.util.StrUtil
import com.mono.component.common.utils.AsyncExecutorUtils
import com.mono.component.common.utils.EnvUtils
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
    private MessageProducer producer

    def "handlerMappingTest"() {
        for (int idx = 0; idx < 5; idx++) {
            // get folder
            String targetFolder = EnvUtils.getTargetFolder()
            // current timestamp
            long timestamp = System.currentTimeMillis()
            ThreadUtil.safeSleep(1)
            // write content
            File file = new File(targetFolder + StrUtil.SLASH + timestamp)
            if (file.createNewFile()) {
                FileUtil.writeUtf8String("hello", file)
            }
        }
        // submit future task , this is a loop thread, need interrupt by cancel call
        AsyncExecutorUtils.submit(() -> {
            producer.onApplicationEvent(null)
            return null
        })
        expect: "Target Folder is not empty and scan files success and publish message by producer success"
    }
}

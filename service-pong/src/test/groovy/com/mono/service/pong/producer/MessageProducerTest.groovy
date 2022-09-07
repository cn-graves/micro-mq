package com.mono.service.pong.producer

import cn.hutool.core.io.FileUtil
import cn.hutool.core.thread.ThreadUtil
import cn.hutool.core.util.StrUtil
import com.mono.component.common.msg.FileMessage
import com.mono.component.common.utils.EnvUtils
import com.mono.service.pong.utils.LocalCacheManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import spock.lang.Specification

import java.util.function.Consumer

@AutoConfigureMockMvc
@TestPropertySource(value = "classpath:bootstrap.properties")
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
        producer.onApplicationEvent(null);
        expect: "Target Folder is not empty and scan files success and publish message by producer success"
    }

    def "testLocalCacheManager"() {
        LocalCacheManager manager = new LocalCacheManager();
        Long key = System.currentTimeMillis();
        for (i in 0..<100) {
            new Thread(new Task(manager, key)).start()
        }
        expect: "write lock block the logic"
    }

    class Task implements Runnable {

        public Task(LocalCacheManager manager, Long key) {
            this.manager = manager;
            this.key = key;
        }

        LocalCacheManager manager;

        Long key;

        /**
         * When an object implementing interface {@code Runnable} is used
         * to create a thread, starting the thread causes the object's
         * {@code run} method to be called in that separately executing
         * thread.
         * <p>
         * The general contract of the method {@code run} is that it may
         * take any action whatsoever.
         *
         * @see java.lang.Thread#run()
         */
        @Override
        void run() {
            manager.putAndPublish(key, new File(""), new Consumer<File>() {
                @Override
                void accept(File file) {

                }
            })
        }
    }
}


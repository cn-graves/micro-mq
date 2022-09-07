package com.mono.service.ping.schedule

import cn.hutool.core.io.FileUtil
import cn.hutool.core.lang.Assert
import cn.hutool.core.util.IdUtil
import cn.hutool.core.util.ObjectUtil
import com.mono.component.common.constant.RedisConsts
import com.mono.component.common.utils.EnvUtils
import com.mono.service.ping.components.RedisCacheManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import spock.lang.Specification

/**
 * record logic tester
 *
 * @author Mono 2022/9/2 09:49 gralves@163.com
 */
@AutoConfigureMockMvc
@TestPropertySource(value = "classpath:bootstrap.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MsgRecorderTest extends Specification {

    @Autowired
    private MsgRecordSchedule msgRecordSchedule;

    @Autowired
    private RedisCacheManager redisCacheManager;

    /**
     * test output files
     *
     * @author Mono 2022/9/2 09:58 gralves@163.com
     */
    def "invoke"() {
        String targetFolder = EnvUtils.getTargetFolder()
        Assert.notNull(targetFolder)
        msgRecordSchedule.invoke()
        List<File> files = FileUtil.loopFiles(targetFolder)
        expect:
        ObjectUtil.isNotEmpty(files)
    }

    /**
     * test lock and unlock
     */
    def "testLockAndReleaseLock"() {
        String key = IdUtil.getSnowflakeNextIdStr();
        String requestId = IdUtil.getSnowflakeNextIdStr();
        boolean lockResult = redisCacheManager.distributedLock(key, requestId, RedisConsts.DEFAULT_EXPIRE_SECOND)
        redisCacheManager.unlock(key, requestId)
        expect:
        lockResult
    }
}

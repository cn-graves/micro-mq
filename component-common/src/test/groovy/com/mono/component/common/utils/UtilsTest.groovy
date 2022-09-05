package com.mono.component.common.utils

import cn.hutool.core.thread.ThreadUtil
import cn.hutool.core.util.ObjectUtil
import cn.hutool.core.util.RandomUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Specification

/**
 *  FileUtils test
 *
 * @author Mono 2022/9/2 09:49 gralves@163.com
 */
class UtilsTest extends Specification {

    Logger logger = LoggerFactory.getLogger(UtilsTest.class)

    def "AsyncExecutorUtils"() {
        String randomStr = null
        AsyncExecutorUtils.submit(() -> {
            randomStr = RandomUtil.randomString(10)
            logger.info(randomStr)
            return randomStr
        })
        AsyncExecutorUtils.submit(() -> {
            randomStr = RandomUtil.randomString(10)
            logger.info(randomStr)
            return randomStr
        }, result -> logger.info(result), ex -> {
            logger.info(ex.getMessage())
        })
        AsyncExecutorUtils.submit(() -> {
            randomStr = RandomUtil.randomString(10)
            logger.info(randomStr)
            throw new RuntimeException(randomStr)
            return randomStr
        }, result -> logger.info(result), ex -> {
            logger.info(ex.getMessage())
        })
        ThreadUtil.safeSleep(1000)
        expect:
        ObjectUtil.isNotEmpty(randomStr)
    }

    def "envUtilTest"() {
        EnvUtils utils = new EnvUtils()
        String folderPath = EnvUtils.getTargetFolder()
        expect:
        ObjectUtil.isNotEmpty(folderPath)
    }
}

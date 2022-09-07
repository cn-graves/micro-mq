package com.mono.component.common.utils


import cn.hutool.core.util.ObjectUtil
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

    def "envUtilTest"() {
        EnvUtils utils = new EnvUtils()
        String folderPath = EnvUtils.getTargetFolder()
        expect:
        ObjectUtil.isNotEmpty(folderPath)
    }
}

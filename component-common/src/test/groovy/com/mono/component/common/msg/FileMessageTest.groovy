package com.mono.component.common.msg

import cn.hutool.core.bean.BeanUtil
import cn.hutool.core.lang.Assert
import cn.hutool.core.util.RandomUtil
import spock.lang.Specification

/**
 *  FileMessage instantiate test and properties populate
 *
 * @author Mono 2022/9/2 09:49 gralves@163.com
 */
class FileMessageTest extends Specification {

    def "testFileMessageInit"() {
        FileMessage message = FileMessage.getInstance()
        Assert.notNull(message)
        message.setPayload(RandomUtil.randomString(10))
        message.getPayload()
        message.toString()
        message.hashCode()
        message == message
        FileMessage message2 = new FileMessage()
        BeanUtil.copyProperties(message, message2)
        message.equals(message2)
        message.canEqual(message2)
        expect:
        message != null
    }
}

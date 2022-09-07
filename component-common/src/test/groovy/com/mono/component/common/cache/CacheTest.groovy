package com.mono.component.common.cache

import cn.hutool.core.util.IdUtil
import com.mono.component.common.constant.RedisConsts
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Specification

/**
 * Cache Test
 *
 * @author Mono 2022/9/2 09:49 gralves@163.com
 */
class CacheTest extends Specification {

    Logger logger = LoggerFactory.getLogger(CacheTest.class)

    def "testCacheManager"() {
        CacheManager manager = new CacheManager() {
            @Override
            boolean distributedLock(String key, String unionKey, Long expireSecond) {
                logger.info("key : {}, requestId: {}, expireSecond: {}", key, unionKey, expireSecond)
                return true;
            }

            @Override
            void unlock(String key, String unionKey) {
                logger.info("key : {}, requestId: {}", key, unionKey)
            }
        }
        String key = IdUtil.getSnowflakeNextIdStr()
        String requestId = IdUtil.getSnowflakeNextIdStr()
        boolean result = manager.distributedLock(
                key,
                requestId,
                RedisConsts.DEFAULT_EXPIRE_SECOND
        );
        manager.unlock(key, requestId)
        expect:
        result
    }
}

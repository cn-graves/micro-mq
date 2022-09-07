package com.mono.service.ping.components;

import cn.hutool.core.collection.CollectionUtil;
import com.mono.component.common.cache.CacheManager;
import com.mono.component.common.constant.RedisLuaScripts;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Cache Manager implement
 *
 * @author Mono 2022/9/7 22:15 gralves@163.com
 */
@Component
public class RedisCacheManager extends CacheManager {

    /**
     * arguments serializer
     */
    private final RedisSerializer<String> argsSerializer = new StringRedisSerializer();

    /**
     * result serializer
     */
    private final RedisSerializer<String> resultSerializer = new StringRedisSerializer();

    private final StringRedisTemplate template;

    public RedisCacheManager(StringRedisTemplate template) {
        this.template = template;
    }

    /**
     * setnx by lua scripts
     *
     * @param key          lock key
     * @param requestId    unionKey
     * @param expireSecond expire seconds
     * @return lock result
     * @author Mono 2022/9/7 22:20 gralves@163.com
     */
    @Override
    public boolean distributedLock(String key, String requestId, Long expireSecond) {
        List<String> keys = CollectionUtil.newArrayList(key);
        String flag = template.execute(
                RedisScript.of(RedisLuaScripts.LOCK_SCRIPT, String.class),
                argsSerializer,
                resultSerializer,
                keys,
                requestId,
                expireSecond.toString()
        );
        return Boolean.parseBoolean(flag);
    }

    /**
     * release lock
     *
     * @param key      key
     * @param unionKey unionKey
     * @author Mono 2022/9/7 22:20 gralves@163.com
     */
    @Override
    public void unlock(String key, String unionKey) {
        List<String> keys = CollectionUtil.newArrayList(key);
        template.execute(RedisScript.of(RedisLuaScripts.RELEASE_SCRIPT, String.class), argsSerializer, resultSerializer, keys, unionKey);
    }
}

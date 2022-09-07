package com.mono.component.common.constant;

/**
 * Redis lua scripts
 *
 * @author Mono 2022/9/7 22:23 gralves@163.com
 */
public class RedisLuaScripts {

    /**
     * set lock scripts
     */
    public static final String LOCK_SCRIPT = "if redis.call('setnx', KEYS[1], ARGV[1]) == 1 then redis.call('expire', KEYS[1], ARGV[2]) return 'true' else return 'false' end";

    /**
     * release lock scripts
     */
    public static final String RELEASE_SCRIPT = "if redis.call('get', KEYS[1]) == ARGV[1] then redis.call('del', KEYS[1]) end return 'true' ";
}

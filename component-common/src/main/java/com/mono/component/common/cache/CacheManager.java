package com.mono.component.common.cache;

/**
 * Cache Manager interface
 *
 * @author Mono 2022/9/7 22:14 gralves@163.com
 */
public abstract class CacheManager {

    /**
     * setnx by lua scripts
     *
     * @param key          lock key
     * @param unionKey     unionKey
     * @param expireSecond expire seconds
     * @return lock result
     * @author Mono 2022/9/7 22:20 gralves@163.com
     */
    public abstract boolean distributedLock(String key, String unionKey, Long expireSecond);

    /**
     * release lock
     *
     * @param key      key
     * @param unionKey unionKey
     * @author Mono 2022/9/7 22:20 gralves@163.com
     */
    public abstract void unlock(String key, String unionKey);
}

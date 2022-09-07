package com.mono.service.pong.utils;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;

/**
 * Local Cache Utility
 *
 * @author Mono 2022/9/7 23:23 gralves@163.com
 * @see java.util.concurrent.locks.ReentrantReadWriteLock
 */
public class LocalCacheManager {

    /**
     * file cache map
     */
    private final HashMap<Long, File> FILE_MAP = new HashMap<>();

    /**
     * ReentrantReadWriteLock init
     */
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock(Boolean.TRUE);

    /**
     * read lock
     */
    private final ReentrantReadWriteLock.ReadLock readLock = lock.readLock();

    /**
     * write lock
     */
    private final ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();

    /**
     * take value from map
     *
     * @param key   key
     * @param value value
     * @author Mono 2022/9/7 23:34 gralves@163.com
     */
    public void putAndPublish(Long key, File value, Consumer<File> consumer) {
        try {
            writeLock.lock();
            if (!this.containsKey(key)) {
                FILE_MAP.put(key, value);
                consumer.accept(value);
            }
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * get from map
     *
     * @param key key
     * @return value
     * @author Mono 2022/9/7 23:41 gralves@163.com
     */
    public Boolean containsKey(Long key) {
        try {
            readLock.lock();
            return FILE_MAP.containsKey(key);
        } finally {
            readLock.unlock();
        }
    }
}

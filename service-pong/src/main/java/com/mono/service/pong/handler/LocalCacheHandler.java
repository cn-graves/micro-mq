package com.mono.service.pong.handler;

import cn.hutool.core.io.FileUtil;
import com.mono.component.common.msg.FileMessage;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;

/**
 * handler mapping
 *
 * @author Mono 2022/9/1 22:24 gralves@163.com
 */
public class LocalCacheHandler {

    private volatile static ConcurrentHashMap<Long, FileMessage> instance;

    /**
     * forbidden instantiate by constructor
     */
    private LocalCacheHandler() {
        throw new RuntimeException();
    }

    /**
     * getInstance
     *
     * @return instance
     * @author Mono 2022/9/1 22:26 gralves@163.com
     */
    public static ConcurrentHashMap<Long, FileMessage> getInstance() {
        if (instance == null) {
            synchronized (LocalCacheHandler.class) {
                if (instance == null) {
                    instance = new ConcurrentHashMap<>(256);
                }
            }
        }
        return instance;
    }

    /**
     * remove sync
     *
     * @param sequence sequence
     * @author mono 2022/9/5 14:23 Gralves@163.com
     */
    public static void removeSequence(long sequence) {
        synchronized (FileMessageHandler.class) {
            ConcurrentHashMap<Long, FileMessage> instance = getInstance();
            FileMessage fileMessage = instance.get(sequence);
            if (null != fileMessage && !fileMessage.getError()) {
                File file = FileUtil.file(fileMessage.getPath());
                if (file.exists()) {
                    //noinspection ResultOfMethodCallIgnored
                    file.delete();
                }
            }
            instance.remove(sequence);
        }
    }
}

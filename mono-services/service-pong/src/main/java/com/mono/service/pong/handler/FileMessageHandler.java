package com.mono.service.pong.handler;

import cn.hutool.core.io.FileUtil;
import com.lmax.disruptor.EventHandler;
import com.mono.component.common.models.msg.FileMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;


/**
 * Message Handler
 *
 * @author Mono 2022/9/1 22:16 gralves@163.com
 */
@Component
public class FileMessageHandler implements EventHandler<FileMessage> {

    Logger logger = LoggerFactory.getLogger(FileMessageHandler.class);

    /**
     * message handler
     *
     * @param fileMessage message
     * @param l           sequence
     * @param b           endOfBatch
     * @author Mono 2022/9/1 22:36 gralves@163.com
     */
    @Override
    public void onEvent(FileMessage fileMessage, long l, boolean b) {
        Optional.ofNullable(fileMessage).ifPresent(msg -> {
            logger.info("[MessageHandler] handle message => payload: {}", msg.getPayload());
            // remove file
            if (FileUtil.exist(msg.getRealPath())) {
                FileUtil.del(msg.getRealPath());
            }
            // remove handle cache
            HandleMapping.getInstance().remove(msg.getFileName());
        });
    }
}

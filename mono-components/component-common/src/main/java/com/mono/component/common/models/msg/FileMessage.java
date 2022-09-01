package com.mono.component.common.models.msg;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * File Message Model
 *
 * @author Mono 2022/9/1 22:01 gralves@163.com
 */
@Data
@Accessors(chain = true)
@ApiModel("File message model")
public class FileMessage {

    @ApiModelProperty("message payload")
    private String payload;

    @ApiModelProperty("file name")
    private String fileName;

    @ApiModelProperty("file path")
    private String realPath;

    /**
     * get new instance
     *
     * @return instance
     * @author Mono 2022/9/1 22:08 gralves@163.com
     */
    public static FileMessage getInstance() {
        return new FileMessage();
    }
}

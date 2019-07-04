package com.berry.common;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Berry_Cooper.
 * @date 2019-07-03 21:51
 * fileName：OssException
 * Use：
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OssException extends RuntimeException {

    private int code;

    private String msg;

    public OssException(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}

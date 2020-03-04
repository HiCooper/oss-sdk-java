package com.berry.common;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.IOException;

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
public class OssException extends IOException {

    public OssException() {
        super();
    }

    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public OssException(String message) {
        super(message);
    }
}

package com.berry.storage.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Berry_Cooper.
 * @date 2019-06-16 00:04
 * fileName：GenerateUrlWithSignedVo
 * Use：
 */
@Data
@Accessors(chain = true)
public class GenerateUrlWithSigned {
    private String url;
    private String signature;
}

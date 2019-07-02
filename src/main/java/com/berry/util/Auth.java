package com.berry.util;

import com.alibaba.fastjson.JSONObject;
import com.berry.common.Constants;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Berry_Cooper.
 * @date 2019-06-29 11:03
 * fileName：Auth
 * Use：
 */
public final class Auth {

    /**
     * 加密json 中 请求ip key
     */
    private static final String ENCODE_JSON_REQUEST_IP_KEY = "requestIp";
    /**
     * 加密json 中 授权目标方法 key
     */
    private static final String ENCODE_JSON_TARGET_URL_KEY = "targetUrl";
    /**
     * 加密json 中 token过期时间 key
     */
    private static final String ENCODE_JSON_DEADLINE_KEY = "deadline";

    private static final String MAC_ALGORITHM = "HmacSHA1";


    private final String accessKeyId;
    private final SecretKeySpec secretKeySpec;

    private Auth(String accessKeyId, SecretKeySpec secretKeySpec) {
        this.accessKeyId = accessKeyId;
        this.secretKeySpec = secretKeySpec;
    }

    public static Auth create(String accessKeyId, String accessKeySecret) {
        if (StringUtils.isBlank(accessKeyId) || StringUtils.isBlank(accessKeySecret)) {
            throw new IllegalArgumentException("empty key");
        }
        byte[] sk = StringUtils.utf8Bytes(accessKeySecret);
        SecretKeySpec secretKeySpec = new SecretKeySpec(sk, MAC_ALGORITHM);
        return new Auth(accessKeyId, secretKeySpec);
    }


    /**
     * 获取授权 口令，
     * <p>注意：该口令拥有对应账户的所有权限</p>
     *
     * @param expires 有效时长，单位秒。默认3600s
     * @return token
     */
    public String accessToken(long expires, String requestIp, String targetUrl) {
        long deadline = System.currentTimeMillis() / 1000 + expires;
        // 这里保存了 过期时间信息,允许访问的url ，请求ip
        StringMap map = new StringMap();
        map.put(ENCODE_JSON_DEADLINE_KEY, deadline);
        map.put(ENCODE_JSON_REQUEST_IP_KEY, requestIp);
        map.put(ENCODE_JSON_TARGET_URL_KEY, targetUrl);
        // 1.过期时间 和 bucket 转 json 再 base64 编码 得到 encodeJson
        String json = Json.encode(map);
        String encodeJson = Base64Util.encode(StringUtils.utf8Bytes(json));

        // 2.对 encodeJson 进行 mac 加密 再进行 base64 编码 得到 encodedSign
        Mac mac = this.createMac();
        String encodedSign = Base64Util.encode(mac.doFinal(StringUtils.utf8Bytes(encodeJson)));

        // 3.拼接 accessKeyId encodedSign 和 encodeJson，用英文冒号隔开
        return this.accessKeyId + ":" + encodedSign + ":" + encodeJson;
    }

//    public static void main(String[] args) {
//        // 生成 token
//        Auth auth = Auth.create("yRdQE7hybEfPD5Kgt4fXCe", "wkZ2RvEnuom/Pa4RTQGmPdFVd6g7/CO");
//        String token = auth.accessToken(3600, "192.168.2.194", "/test/api");
//        System.out.println(token);
//    }


    private Mac createMac() {
        Mac mac;
        try {
            mac = Mac.getInstance(MAC_ALGORITHM);
            mac.init(secretKeySpec);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e);
        }
        return mac;
    }
}

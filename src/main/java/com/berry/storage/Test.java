package com.berry.storage;

import com.berry.util.Auth;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Berry_Cooper.
 * @date 2019-06-30 18:30
 * fileName：Test
 * Use：
 */
public class Test {

    public static void main(String[] args) throws IllegalAccessException {
        // 生成 token
        Auth auth = Auth.create("yRdQE7hybEfPD5Kgt4fXCe", "wkZ2RvEnuom/Pa4RTQGmPdFVd6g7/CO");
        String token = auth.accessToken(3600);
        System.out.println(token);
    }
}

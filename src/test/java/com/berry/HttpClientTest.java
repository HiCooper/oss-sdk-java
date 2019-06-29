package com.berry;

import com.berry.http.HttpClient;
import com.berry.http.Response;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Berry_Cooper.
 * @date 2019-06-29 22:10
 * fileName：HttpClientTest
 * Use：
 */
public class HttpClientTest {


    public static void main(String[] args) {
        Response response = HttpClient.get("http://www.baidu.com");
        if (response.isSuccessful()) {
            System.out.println(response.bodyString());
        } else {
            System.out.println("请求错误");
        }
    }
}

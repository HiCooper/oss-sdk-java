package com.berry.http;

import com.berry.util.StringUtils;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.ResponseBody;

import javax.annotation.Nullable;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Berry_Cooper.
 * @date 2019-06-29 21:06
 * fileName：Response
 * Use：
 */
public class Response {

    private final int code;
    private final String message;
    private final String error;
    private final Headers headers;
    private final String url;
    private final String contentType;
    private byte[] body;

    private Response(int code, String message, Headers headers, byte[] body, String url, String contentType, String error) {
        this.code = code;
        this.message = message;
        this.headers = headers;
        this.body = body;
        this.error = error;
        this.url = url;
        this.contentType = contentType;
    }

    static Response create(okhttp3.Response response, @Nullable String errorMsg) {
        if (response == null || StringUtils.isNoneBlank(errorMsg)) {
            return new Response(-1, null, null, null, null, null, errorMsg);
        }
        ResponseBody body = response.body();
        byte[] bodyArr = new byte[0];
        String error = null;
        try {
            bodyArr = body == null ? null : body.bytes();
        } catch (IOException e) {
            error = e.getLocalizedMessage();
        }
        String contentType = getContentType(response);
        String url = response.request().url().toString();
        response.close();
        return new Response(response.code(),
                response.message(),
                response.headers(),
                bodyArr,
                url,
                contentType,
                error);
    }

    private static String getContentType(okhttp3.Response response) {
        ResponseBody body = response.body();
        if (body != null) {
            MediaType mediaType = body.contentType();
            if (mediaType != null) {
                return mediaType.type() + "/" + mediaType.subtype();
            }
        }
        return "";
    }

    public boolean isSuccessful() {
        return code >= 200 && code < 300;
    }

    public String bodyString() {
        return StringUtils.utf8String(body);
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getError() {
        return error;
    }

    public Headers getHeaders() {
        return headers;
    }

    public String getUrl() {
        return url;
    }

    public String getContentType() {
        return contentType;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }
}

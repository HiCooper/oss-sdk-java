package com.berry.storage;


import com.berry.common.Constants;
import com.berry.http.HttpClient;
import com.berry.http.Response;
import com.berry.storage.dto.GenerateUrlWithSignedVo;
import com.berry.storage.dto.ObjectInfoVo;
import com.berry.storage.dto.Result;
import com.berry.storage.url.UrlFactory;
import com.berry.util.Auth;
import com.berry.util.Json;
import com.berry.util.StringMap;
import com.berry.util.StringUtils;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.io.File;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Berry_Cooper.
 * @date 2019-06-30 18:30
 * fileName：ObjectManage
 * Use：
 */
public final class ObjectManage {

    private static final Logger logger = LoggerFactory.getLogger(BucketManage.class);

    private final Auth auth;
    private final Config config;

    public ObjectManage(Auth auth, Config config) {
        this.auth = auth;
        this.config = config;
    }

    /**
     * upload object byte data
     */
    public ObjectInfoVo upload(String bucket, String acl, @Nullable String filePath, String fileName, byte[] fileData) {
        // 验证acl 规范
        if (!Constants.AclType.ALL_NAME.contains(acl)) {
            throw new IllegalArgumentException("illegal acl, enum [" + Constants.AclType.ALL_NAME + "]");
        }
        StringMap params = new StringMap();
        params.put("bucket", bucket);
        params.put("acl", acl);
        if (StringUtils.isNotBlank(filePath)) {
            params.put("filePath", filePath);
        }
        params.put("fileName", fileName);
        params.put("data", fileData);
        String url = String.format("%s%s", config.getAddress(), UrlFactory.ObjectUrl.upload_byte.getUrl());
        StringMap header = auth.authorization(url);
        Response response = HttpClient.postComplex(url, params, header);
        Result result = response.jsonToObject(Result.class);
        if (result.getCode().equals(Constants.API_SUCCESS_CODE) && result.getMsg().equals(Constants.API_SUCCESS_MSG)) {
            return Json.decode(Json.encode(result.getData()), ObjectInfoVo.class);
        }
        logger.error("request error, code:{}, msg:{}", result.getCode(), result.getMsg());
        return null;
    }

    /**
     * upload opject
     *
     * @param bucket   bucket name
     * @param acl      对象acl
     * @param filePath 对象存储路径
     * @param file     文件
     */
    public ObjectInfoVo upload(String bucket, String acl, @Nullable String filePath, File file) {
        // 验证acl 规范
        if (!Constants.AclType.ALL_NAME.contains(acl)) {
            throw new IllegalArgumentException("illegal acl, enum [" + Constants.AclType.ALL_NAME + "]");
        }
        StringMap fields = new StringMap();
        fields.put("bucket", bucket);
        fields.put("acl", acl);
        if (StringUtils.isNotBlank(filePath)) {
            fields.put("filePath", filePath);
        }
        String url = String.format("%s%s", config.getAddress(), UrlFactory.ObjectUrl.create.getUrl());

        StringMap header = auth.authorization(url);
        Response response = HttpClient.multipartPost(url, fields, "file", file.getName(), file, Constants.MULTIPART_MIME, header);
        Result result = response.jsonToObject(Result.class);
        if (result.getCode().equals(Constants.API_SUCCESS_CODE) && result.getMsg().equals(Constants.API_SUCCESS_MSG)) {
            return Json.decode(Json.encode(result.getData()), ObjectInfoVo.class);
        }
        logger.error("request error, code:{}, msg:{}", result.getCode(), result.getMsg());
        return null;
    }

    public ObjectInfoVo upload(String bucket, String acl, @Nullable String filePath, String fileName, String base64Data) {
        // 验证acl 规范
        if (!Constants.AclType.ALL_NAME.contains(acl)) {
            throw new IllegalArgumentException("illegal acl, enum [" + Constants.AclType.ALL_NAME + "]");
        }
        StringMap params = new StringMap();
        params.put("bucket", bucket);
        params.put("acl", acl);
        if (StringUtils.isNotBlank(filePath)) {
            params.put("filePath", filePath);
        }
        params.put("fileName", fileName);
        params.put("data", base64Data);
        String url = String.format("%s%s", config.getAddress(), UrlFactory.ObjectUrl.upload_base64.getUrl());
        StringMap header = auth.authorization(url);
        Response response = HttpClient.postComplex(url, params, header);
        Result result = response.jsonToObject(Result.class);
        if (result.getCode().equals(Constants.API_SUCCESS_CODE) && result.getMsg().equals(Constants.API_SUCCESS_MSG)) {
            return Json.decode(Json.encode(result.getData()), ObjectInfoVo.class);
        }
        logger.error("request error, code:{}, msg:{}", result.getCode(), result.getMsg());
        return null;
    }

    /**
     * 读取对象
     *
     * @param bucket         bucket name
     * @param fullObjectPath 对象全路径 不已 '/' 开头
     * @return 对象二进制数组
     */
    public byte[] getObject(String bucket, String fullObjectPath) {
        if (fullObjectPath.startsWith("/")) {
            throw new IllegalArgumentException("object full path not allow start with / ");
        }
        String url = String.format(config.getAddress() + UrlFactory.ObjectUrl.get_object.getUrl(), bucket, fullObjectPath);
        Response response = get(url);
        System.out.println(response.getContentType());
        if (response.isSuccessful() && response.getContentType().startsWith(Constants.DEFAULT_MIME)) {
            return response.getBody();
        }
        logger.error(response.bodyString());
        return null;
    }

    /**
     * 创建目录
     * 不允许使用表情符，请使用符合要求的 UTF-8 字符
     * '/' 用于分割路径，可快速创建子目录，但不要以 '/' 或 '\' 打头，不要出现连续的 '/'
     * 不允许出现名为'..'的子目录
     * 总长度控制在 1-254 个字符
     *
     * @param bucket 存储空间名
     * @param folder 全路径 如 a/b/c
     * @return 成功与否
     */
    public boolean createFolder(String bucket, String folder) {
        if (folder.matches(Constants.FILE_PATH_REG)) {
            throw new IllegalArgumentException("目录名不符合规则");
        }
        StringMap params = new StringMap();
        params.put("bucket", bucket);
        params.put("folder", folder);
        String url = String.format("%s%s", config.getAddress(), UrlFactory.ObjectUrl.create_folder.getUrl());
        Response response = post(url, params);
        Result result = response.jsonToObject(Result.class);
        return result.getCode().equals(Constants.API_SUCCESS_CODE) && result.getMsg().equals(Constants.API_SUCCESS_MSG);
    }

    /**
     * 删除对象或目录
     *
     * @param bucket  存储空间名
     * @param objects 对象或目录全路径 如 /a/b/c.jpg 或 /a/c
     * @return 成功与否
     */
    public boolean removeObjectOrFolder(String bucket, String objects) {
        StringMap params = new StringMap();
        params.put("bucket", bucket);
        params.put("objects", objects);
        String url = String.format("%s%s", config.getAddress(), UrlFactory.ObjectUrl.delete_objects.getUrl());
        Response response = post(url, params);
        Result result = response.jsonToObject(Result.class);
        return result.getCode().equals(Constants.API_SUCCESS_CODE) && result.getMsg().equals(Constants.API_SUCCESS_MSG);
    }

    /**
     * 获取对象临时访问链接
     *
     * @param bucket     bucket name
     * @param objectPath 对象全路径
     * @param timeout    链接有效时间
     * @return url
     */
    public String getObjectTempAccessUrlWithExpired(String bucket, String objectPath, Integer timeout) {
        if (timeout == null || timeout < 60 || timeout > 64800) {
            throw new IllegalArgumentException("timeout must between 60 and 64800");
        }
        StringMap params = new StringMap();
        params.put("bucket", bucket);
        params.put("objectPath", objectPath);
        params.put("timeout", timeout);
        String url = String.format("%s%s", config.getAddress(), UrlFactory.ObjectUrl.generate_url_with_signed.getUrl());
        Response response = post(url, params);
        Result result = response.jsonToObject(Result.class);
        if (result.getCode().equals(Constants.API_SUCCESS_CODE) && result.getMsg().equals(Constants.API_SUCCESS_MSG)) {
            GenerateUrlWithSignedVo vo = new Gson().fromJson(Json.encode(result.getData()), GenerateUrlWithSignedVo.class);
            return vo.getUrl() + "?" + vo.getSignature();
        }
        logger.error("request error, code:{}, msg:{}", result.getCode(), result.getMsg());
        return null;
    }

    private Response get(String url) {
        logger.debug("request url:" + url);
        StringMap header = auth.authorization(url);
        System.out.println("header:" + Json.encode(header));
        return HttpClient.get(url, header);
    }

    private Response post(String url, StringMap params) {
        logger.debug("request url:" + url);
        StringMap header = auth.authorization(url);
        System.out.println("header:" + Json.encode(header));
        return HttpClient.post(url, params.jsonString(), header);
    }
}

package com.berry.storage;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.berry.common.Constants;
import com.berry.common.OssException;
import com.berry.http.HttpClient;
import com.berry.http.Response;
import com.berry.storage.dto.GenerateUrlWithSigned;
import com.berry.storage.dto.ObjectInfo;
import com.berry.storage.dto.Result;
import com.berry.storage.url.UrlFactory;
import com.berry.util.Auth;
import com.berry.util.Json;
import com.berry.util.StringMap;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.io.File;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Berry_Cooper.
 * @date 2019-06-30 18:30
 * fileName：ObjectManage
 * Use：
 */
public final class ObjectManage {

    private static final Logger logger = LoggerFactory.getLogger(ObjectManage.class);

    private final String errorIMsgTemp = "request fail,stateCode:{}, msg:{}";

    private static final String illegalAclMsg = "illegal acl, enum [" + Constants.AclType.ALL_NAME + "]";

    private final Auth auth;
    private final Config config;
    private final HttpClient client;

    public ObjectManage(Auth auth, Config config) {
        this.auth = auth;
        this.config = config;
        this.client = new HttpClient(config.getUploadTimeout());
    }

    /**
     * upload object byte data
     */
    public ObjectInfo upload(String bucket, String acl, @Nullable String filePath, String fileName, byte[] fileData) throws OssException {
        // 验证acl 规范
        if (!Constants.AclType.ALL_NAME.contains(acl)) {
            throw new IllegalArgumentException(illegalAclMsg);
        }
        StringMap params = new StringMap();
        params.put("bucket", bucket);
        params.put("acl", acl);
        if (isNotBlank(filePath)) {
            params.put("filePath", filePath);
        }
        params.put("fileName", fileName);
        params.put("data", fileData);
        String url = String.format("%s%s", config.getAddress(), UrlFactory.ObjectUrl.upload_byte.getUrl());
        StringMap header = auth.authorization(url);
        Response response = client.postComplex(url, params, header);
        Result result = response.jsonToObject(Result.class);
        if (result.getCode().equals(Constants.API_SUCCESS_CODE) && result.getMsg().equals(Constants.API_SUCCESS_MSG)) {
            return Json.decode(Json.encode(result.getData()), ObjectInfo.class);
        }
        logger.error(errorIMsgTemp, result.getCode(), result.getMsg());
        throw new OssException(result.getMsg());
    }

    /**
     * single upload file
     *
     * @param bucket   bucket name
     * @param acl      对象acl
     * @param filePath 对象存储路径
     * @param file     文件
     */
    public JSONArray upload(String bucket, String acl, @Nullable String filePath, File file) throws OssException {
        File[] files = {file};
        return upload(bucket, acl, filePath, files);
    }

    /**
     * batch upload file
     *
     * @param bucket   bucket name
     * @param acl      对象acl
     * @param filePath 对象存储路径
     * @param files    文件
     */
    public JSONArray upload(String bucket, String acl, @Nullable String filePath, File[] files) throws OssException {
        // 验证acl 规范
        if (!Constants.AclType.ALL_NAME.contains(acl)) {
            throw new IllegalArgumentException(illegalAclMsg);
        }
        StringMap fields = new StringMap();
        fields.put("bucket", bucket);
        fields.put("acl", acl);
        if (isNotBlank(filePath)) {
            fields.put("filePath", filePath);
        }
        String url = String.format("%s%s", config.getAddress(), UrlFactory.ObjectUrl.create.getUrl());

        StringMap header = auth.authorization(url);
        Response response = client.multipartPost(url, fields, "file", files, header);
        Result result = response.jsonToObject(Result.class);
        if (result.getCode().equals(Constants.API_SUCCESS_CODE)
                && result.getMsg().equals(Constants.API_SUCCESS_MSG)
                && result.getData() != null) {
            return JSON.parseArray(JSON.toJSONString(result.getData()));
        }
        logger.error(errorIMsgTemp, result.getCode(), result.getMsg());
        throw new OssException(result.getMsg());
    }

    public ObjectInfo upload(String bucket, String acl, @Nullable String filePath, String fileName, String base64Data) throws OssException {
        // 验证acl 规范
        if (!Constants.AclType.ALL_NAME.contains(acl)) {
            throw new IllegalArgumentException(illegalAclMsg);
        }
        StringMap params = new StringMap();
        params.put("bucket", bucket);
        params.put("acl", acl);
        if (isNotBlank(filePath)) {
            params.put("filePath", filePath);
        }
        params.put("fileName", fileName);
        params.put("data", base64Data);
        String url = String.format("%s%s", config.getAddress(), UrlFactory.ObjectUrl.upload_base64.getUrl());
        StringMap header = auth.authorization(url);
        Response response = client.postComplex(url, params, header);
        Result result = response.jsonToObject(Result.class);
        if (result.getCode().equals(Constants.API_SUCCESS_CODE)
                && result.getMsg().equals(Constants.API_SUCCESS_MSG)
                && result.getData() != null) {
            return Json.decode(Json.encode(result.getData()), ObjectInfo.class);
        }
        logger.error(errorIMsgTemp, result.getCode(), result.getMsg());
        throw new OssException(result.getMsg());
    }

    /**
     * 读取对象
     *
     * @param bucket         bucket name
     * @param fullObjectPath 对象全路径 不已 '/' 开头
     * @return 对象二进制数组
     */
    public byte[] getObject(String bucket, String fullObjectPath) throws OssException {
        if (fullObjectPath.startsWith("/")) {
            throw new IllegalArgumentException("object full path not allow start with / ");
        }
        String url = String.format(config.getAddress() + UrlFactory.ObjectUrl.get_object.getUrl(), bucket, fullObjectPath);
        Response response = get(url);
        if (response.isSuccessful() && response.getContentType().startsWith(Constants.DEFAULT_MIME)) {
            return response.getBody();
        }
        Result result = response.jsonToObject(Result.class);
        logger.error(errorIMsgTemp, result.getCode(), result.getMsg());
        throw new OssException(result.getMsg());
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
    public boolean createFolder(String bucket, String folder) throws OssException {
        if (folder.matches(Constants.FILE_PATH_REG)) {
            throw new IllegalArgumentException("目录名不符合规则");
        }
        StringMap params = new StringMap();
        params.put("bucket", bucket);
        params.put("folder", folder);
        String url = String.format("%s%s", config.getAddress(), UrlFactory.ObjectUrl.create_folder.getUrl());
        Response response = post(url, params);
        Result result = response.jsonToObject(Result.class);
        boolean res = result.getCode().equals(Constants.API_SUCCESS_CODE) && result.getMsg().equals(Constants.API_SUCCESS_MSG);
        if (!res) {
            throw new OssException(result.getMsg());
        }
        return true;
    }

    /**
     * 资源访问  url 签名
     *
     * @param pureUrl
     * @return
     */
    public String getSignedObjUrl(String pureUrl) {
        String sep = "?";
        if (pureUrl.contains("?")) {
            sep = "&";
        }
        return pureUrl + sep + "token=" + auth.getSign(pureUrl);
    }

    /**
     * 删除对象或目录
     *
     * @param bucket    存储空间名
     * @param objectIds 对象id,多个用 英文逗号隔开
     * @return 成功与否
     */
    public boolean removeObjectOrFolder(String bucket, String objectIds) throws OssException {
        StringMap params = new StringMap();
        params.put("bucket", bucket);
        params.put("objectIds", objectIds);
        String url = String.format("%s%s", config.getAddress(), UrlFactory.ObjectUrl.delete_objects.getUrl());
        Response response = post(url, params);
        Result result = response.jsonToObject(Result.class);
        boolean res = result.getCode().equals(Constants.API_SUCCESS_CODE) && result.getMsg().equals(Constants.API_SUCCESS_MSG);
        if (!res) {
            throw new OssException(result.getMsg());
        }
        return true;
    }

    /**
     * 获取对象临时访问链接
     *
     * @param bucket     bucket name
     * @param objectPath 对象全路径
     * @param timeout    链接有效时间
     * @return url
     */
    public String getObjectTempAccessUrlWithExpired(String bucket, String objectPath, Integer timeout) throws OssException {
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
            GenerateUrlWithSigned vo = new Gson().fromJson(Json.encode(result.getData()), GenerateUrlWithSigned.class);
            return vo.getUrl() + "?" + vo.getSignature();
        }
        logger.error(errorIMsgTemp, result.getCode(), result.getMsg());
        throw new OssException(result.getMsg());
    }

    private Response get(String url) throws OssException {
        StringMap header = auth.authorization(url);
        logger.debug("request url:{}, header:{}", url, header.map());
        String withTokenUrl = url + "?token=" + header.get(Auth.OSS_SDK_AUTH_HEAD_NAME);
        return client.get(withTokenUrl, header);
    }

    private Response post(String url, StringMap params) throws OssException {
        StringMap header = auth.authorization(url);
        logger.debug("request url:{}, header:{}", url, header.map());
        return client.post(url, params.jsonString(), header);
    }
}

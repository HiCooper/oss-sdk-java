package com.berry.storage;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.berry.common.Constants;
import com.berry.http.HttpClient;
import com.berry.http.Response;
import com.berry.storage.dto.BucketInfo;
import com.berry.storage.dto.Result;
import com.berry.storage.url.UrlFactory;
import com.berry.util.Auth;
import com.berry.util.Json;
import com.berry.util.StringMap;
import com.berry.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Berry_Cooper.
 * @date 2019-06-30 18:30
 * fileName：ObjectManage
 * Use：
 */
public final class BucketManage {

    private static final Logger logger = LoggerFactory.getLogger(BucketManage.class);

    private final Auth auth;
    private final Config config;
    private final HttpClient client;

    public BucketManage(Auth auth, Config config) {
        this.auth = auth;
        this.config = config;
        this.client = new HttpClient(config.getUploadTimeout());
    }

    /**
     * 获取 bucket 列表，
     *
     * @param bucketName bucketName 可空
     * @return 列表
     */
    public List<BucketInfo> queryBucket(@Nullable String bucketName) {
        String url = String.format("%s%s", config.getAddress(), UrlFactory.BucketUr.list.getUrl());
        StringMap params = new StringMap();
        params.putNotNull("name", bucketName);
        Response response = get(url, params.size() > 1 ? params : null);
        if (response.isSuccessful()) {
            Result result = response.jsonToObject(Result.class);
            if (result == null || !result.getCode().equals(Constants.API_SUCCESS_CODE) || !result.getMsg().equals(Constants.API_SUCCESS_MSG)) {
                logger.error(result == null ? "empty result" : result.getMsg());
                return null;
            }
            List<BucketInfo> vos = new ArrayList<>();
            JSONArray array = JSONArray.parseArray(JSON.toJSONString(result.getData()));
            BucketInfo vo;
            for (int i = 0; i < array.size(); i++) {
                JSONObject o = array.getJSONObject(i);
                vo = Json.decode(JSON.toJSONString(o), BucketInfo.class);
                vos.add(vo);
            }
            return vos;
        } else {
            logger.error(response.getCode() + "," + response.getError());
        }
        return null;
    }

    /**
     * 创建 bucket
     *
     * @param name   名称
     * @param region 区域
     * @param acl    ACL 权限,为空时 默认私有
     * @return true or false
     */
    public Boolean createBucket(String name, String region, @Nullable String acl) {
        if (StringUtils.isAnyBlank(name, region)) {
            throw new IllegalArgumentException("name and region cannot be blank!");
        }
        if (!name.matches(Constants.BUCKET_NAME_PATTERN)) {
            throw new IllegalArgumentException("bucket name illegal, 只允许小写字母、数字、中划线（-），且不能以短横线开头或结尾,长度3-63");
        }
        StringMap params = new StringMap();
        params.put("name", name);
        params.put("region", region);
        if (StringUtils.isNotBlank(acl)) {
            // 验证acl 规范
            if (!Constants.AclType.ALL_NAME.contains(acl)) {
                throw new IllegalArgumentException("illegal acl enum [PRIVATE, PUBLIC_READ, PUBLIC_READ_WRITE]");
            }
            params.put("acl", acl);
        }
        String url = String.format("%s%s", config.getAddress(), UrlFactory.BucketUr.new_create_bucket.getUrl());
        return getResult(url, params);
    }

    /**
     * 更新 bucket acl
     *
     * @param bucket bucket name
     * @param acl    acl
     * @return true or false
     */
    public Boolean updateAcl(String bucket, String acl) {
        if (StringUtils.isAnyBlank(bucket, acl)) {
            throw new IllegalArgumentException("bucket and acl cannot be blank!");
        }
        String url = String.format("%s%s", config.getAddress(), UrlFactory.BucketUr.set_acl.getUrl());
        StringMap params = new StringMap();
        params.put("bucket", bucket);
        params.put("acl", acl);
        return getResult(url, params);
    }

    /**
     * 删除 bucket
     *
     * @param bucket bucket name
     * @return true or false
     */
    public Boolean delete(String bucket) {
        if (StringUtils.isBlank(bucket)) {
            throw new IllegalArgumentException("bucket cannot be blank!");
        }
        String url = String.format("%s%s", config.getAddress(), UrlFactory.BucketUr.delete_bucket.getUrl());
        StringMap params = new StringMap();
        params.put("bucket", bucket);
        return getResult(url, params);
    }

    /**
     * 获取 boolean 响应
     *
     * @param url    url
     * @param params 参数
     * @return true or false
     */
    private Boolean getResult(String url, StringMap params) {
        Response response = post(url, params);
        Result result = response.jsonToObject(Result.class);
        if (result.getCode().equals(Constants.API_SUCCESS_CODE) && result.getMsg().equals(Constants.API_SUCCESS_MSG)) {
            return true;
        }
        logger.error("request error, code:{}, msg:{}", result.getCode(), result.getMsg());
        return false;
    }

    private Response get(String url, StringMap params) {
        StringMap header = auth.authorization(url);
        return client.get(url, params, header);
    }

    private Response post(String url, StringMap params) {
        System.out.println("request url:" + url);
        StringMap header = auth.authorization(url);
        System.out.println(header.jsonString());
        return client.post(url, params.jsonString(), header);
    }
}

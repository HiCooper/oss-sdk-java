package com.berry;

import com.alibaba.fastjson.JSON;
import com.berry.storage.BucketManage;
import com.berry.storage.ObjectManage;
import com.berry.storage.dto.BucketInfoVo;
import com.berry.storage.dto.GenerateUrlWithSignedVo;
import com.berry.util.Auth;
import com.berry.util.Json;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Berry_Cooper.
 * @date 2019-06-29 22:10
 * fileName：StorageTest
 * Use：
 */
public class StorageTest {

    /**
     * 根据 密钥对 获取 管理 用户 test 的 xxManage
     */
    private static final String accessKeyId = "UmAWuGv6aC5pE7bQ6il8wO";
    private static final String accessKeySecret = "URbe6TvfdF5XhEeRXiB7yewYcU5PFEe";
    private final BucketManage bucketManage = new BucketManage(Auth.create(accessKeyId, accessKeySecret));
    private final ObjectManage objectManage = new ObjectManage(Auth.create(accessKeyId, accessKeySecret));


    /**
     * 获取bucket 列表测试
     */
    @Test
    public void listBucketTest() {
        List<BucketInfoVo> bucketInfoVos = bucketManage.queryBucket(null);
        System.out.println(JSON.toJSONString(bucketInfoVos));
    }

    /**
     * 创建 bucket 测试
     */
    @Test
    public void createBucketTest() {
        Boolean result = bucketManage.createBucket("hello", "oss-shanghai-1", null);
        System.out.println(result);
    }

    /**
     * 更新bucket 权限设置
     */
    @Test
    public void setBucketAclTest() {
        Boolean result = bucketManage.updateAcl("hello", "PUBLIC_READ");
        System.out.println(result);
    }

    /**
     * 删除bucket 测试
     */
    @Test
    public void deleteBucketTest() {
        Boolean result = bucketManage.delete("hello");
        System.out.println(result);
    }


    /**
     * 上传 对象 测试
     */
    @Test
    public void createObjectTest() {
        File file = new File("./demo.png");
        if (file.isFile() && file.exists()) {
            Boolean upload = objectManage.upload("cooper", "PUBLIC_READ", null, new File("./timg.jpg"));
            if (upload) {
                System.out.println("上传成功！");
            }
        } else {
            System.out.println("file not exist");
        }
    }

    /**
     * 获取 对象 测试
     * @throws IOException
     */
    @Test
    public void getObjectTest() throws IOException {
        byte[] data = objectManage.getObject("cooper", "demo.png");
        if (data != null) {
            FileOutputStream outputStream = new FileOutputStream(new File("./test2.png"));
            outputStream.write(data);
            outputStream.close();
            System.out.println("get object success");
        } else {
            System.out.println("get object fail");
        }
    }

    /**
     * 获取对象 临时访问 url 测试
     */
    @Test
    public void getObjectTempAccessUrlWithExpiredTest() {
        GenerateUrlWithSignedVo vo = objectManage.getObjectTempAccessUrlWithExpired("cooper", "demo.png", 3600);
        System.out.println(Json.encode(vo));
    }

}

package com.berry;

import com.alibaba.fastjson.JSON;
import com.berry.storage.BucketManage;
import com.berry.storage.ObjectManage;
import com.berry.storage.dto.BucketInfoVo;
import com.berry.util.Auth;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
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
    private static final String accessKeyId = "VQ5Brcc6CDJJcH5v.ybXI7";
    private static final String accessKeySecret = "NLghXz00gli26IZtvV8dTbOMHjqpIbN";
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
            Boolean upload = objectManage.upload("cooper", "PUBLIC_READ", null, file);
            if (upload) {
                System.out.println("上传成功！");
            }
        } else {
            System.out.println("file not exist");
        }
    }

    @Test
    public void uploadObjectByteDataTest() throws IOException {
        File file = new File("./demo.png");
        if (file.isFile() && file.exists()) {
            FileInputStream inputStream = new FileInputStream(file);
            final int fileSize = inputStream.available();
            byte[] fileData = new byte[fileSize];
            int read = inputStream.read(fileData, 0, fileSize);
            if (read != fileSize) {
                throw new IOException("not enough bytes read");
            }
            inputStream.close();
            Boolean upload = objectManage.upload("test", "PUBLIC_READ", null, "byte_test.png", fileData);
            if (upload) {
                System.out.println("上传成功！");
            }
        } else {
            System.out.println("file not exist");
        }
    }

    /**
     * 获取 对象 测试
     *
     * @throws IOException
     */
    @Test
    public void getObjectTest() throws IOException {
        byte[] data = objectManage.getObject("test", "NotFound-260-260.png");
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
        String url = objectManage.getObjectTempAccessUrlWithExpired("cooper", "demo.png", 3600);
        System.out.println(url);
    }

}

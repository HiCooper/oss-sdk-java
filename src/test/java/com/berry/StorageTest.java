package com.berry;

import com.alibaba.fastjson.JSON;
import com.berry.storage.BucketManage;
import com.berry.storage.dto.BucketInfoVo;
import com.berry.util.Auth;
import org.junit.Test;

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


    @Test
    public void BucketListTest() {
        BucketManage bucketManage = new BucketManage(Auth.create("yRdQE7hybEfPD5Kgt4fXCe", "wkZ2RvEnuom/Pa4RTQGmPdFVd6g7/CO"));
        List<BucketInfoVo> bucketInfoVos = bucketManage.queryBucket(null);
        System.out.println(JSON.toJSONString(bucketInfoVos));
    }

    @Test
    public void BucketCreateTest() {
        BucketManage bucketManage = new BucketManage(Auth.create("yRdQE7hybEfPD5Kgt4fXCe", "wkZ2RvEnuom/Pa4RTQGmPdFVd6g7/CO"));

        Boolean hello = bucketManage.createBucket("hello", "oss-shanghai-1", null);
        System.out.println(hello);
    }

}

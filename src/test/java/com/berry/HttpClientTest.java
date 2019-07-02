package com.berry;

import com.berry.storage.BucketManage;
import com.berry.util.Auth;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Berry_Cooper.
 * @date 2019-06-29 22:10
 * fileName：HttpClientTest
 * Use：
 */
public class HttpClientTest {


    @Test
    public void BucketManageTest() {
        BucketManage bucketManage = new BucketManage(Auth.create("yRdQE7hybEfPD5Kgt4fXCe", "wkZ2RvEnuom/Pa4RTQGmPdFVd6g7/CO"));
        bucketManage.queryBucket(null);
    }
}

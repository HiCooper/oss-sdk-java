# oss-sdk-java
java sdk for oss

## Bucket 管理

### 初始化，根据用户密钥对 初始化 `BucketManage`
````java
private static final String accessKeyId = "UmAWuGv6aC5pE7bQ6il8wO";
private static final String accessKeySecret = "URbe6TvfdF5XhEeRXiB7yewYcU5PFEe";
private final BucketManage bucketManage = new BucketManage(Auth.create(accessKeyId, accessKeySecret));
````

### 1.查询 Bucket 列表
````java
List<BucketInfoVo> bucketInfoVos = bucketManage.queryBucket(null);
System.out.println(JSON.toJSONString(bucketInfoVos));
````

### 2.创建 Bucket
````java
Boolean result = bucketManage.createBucket("hello", "oss-shanghai-1", null);
System.out.println(result);
````

### 3.更新 Bucket 权限设置
````java
Boolean result = bucketManage.updateAcl("hello", "PUBLIC_READ");
System.out.println(result);
````

### 4.删除 Bucket
````java
Boolean result = bucketManage.delete("hello");
System.out.println(result);
````

### 5.上传 对象
````java
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
````

### 6. 字节数组格式创建 对象
````java
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
        Boolean upload = objectManage.upload("cooper", "PUBLIC_READ", null, "byte_test.png", fileData);
        if (upload) {
            System.out.println("上传成功！");
        }
    } else {
        System.out.println("file not exist");
    }
}
````

### 6. 获取 对象
````java
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
````

### 7.获取对象 临时访问 url
````java
String url = objectManage.getObjectTempAccessUrlWithExpired("cooper", "demo.png", 3600);
System.out.println(url);
````

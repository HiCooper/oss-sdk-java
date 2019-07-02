package com.berry.storage.url;

/**
 * Title UrlFactory
 * Description
 * Copyright (c) 2019
 *
 * @author berry_cooper
 * @version 1.0
 * @date 2019/7/2 10:52
 */
public class UrlFactory {

    public enum BucketUr {
        /**
         * bucket 操作
         */
        list("/ajax/bucket/list.json", "获取 Bucket 列表", "GET"),
        new_create_bucket("/ajax/bucket/new_create_bucket.json", "创建 Bucket", "POST"),
        overview("/ajax/bucket/overview.json", "Bucket 概览", "GET"),
        set_acl("/ajax/bucket/set_acl.json", "更新 Bucket 读写权限", "POST"),
        delete_bucket("/ajax/bucket/delete_bucket.json", "删除Bucket", "POST"),
        get_referer("/ajax/bucket/get_referer.json", "获取 Bucket 防盗链设置", "GET"),
        update_referer("/ajax/bucket/update_referer.json", "更新 Bucket 防盗链设置", "POST"),
        get_last_thirty_day_file_access_data("/ajax/bucket/get_last_thirty_day_file_access_data.json", "30天文件访问统计", "GET"),
        get_last_thirty_day_hot_data("/ajax/bucket/get_last_thirty_day_hot_data.json", "30天热点数据", "GET");

        private String url;
        private String desc;
        private String method;

        BucketUr(String url, String desc, String method) {
            this.url = url;
            this.desc = desc;
            this.method = method;
        }

        public String getUrl() {
            return url;
        }

        public String getDesc() {
            return desc;
        }

        public String getMethod() {
            return method;
        }
    }

    public enum ObjectUrl {

        /**
         * 对象操作
         */
        get_object("/ajax/bucket/file/?/?", "获取对象(私有对象，需要临时口令，且限时访问；公开对象，直接访问，第一个 '?' 为 bucket name 第二个 '?' 为对象相对根路径的全路径，包含对象名)", "GET"),
        create("/ajax/bucket/file/create", "创建对象", "POST"),
        create_folder("/ajax/bucket/file/create_folder.json", "新建目录，支持同事创建多级目录", "POST"),
        generate_url_with_signed("/ajax/bucket/file/generate_url_with_signed.json", "根据过期时间 生成对象临时访问url", "POST"),
        delete_objects("/ajax/bucket/file/delete_objects.json", "删除对象", "POST"),
        head_object("/ajax/bucket/file/head_object.json", "获取文件头部信息", "GET"),
        list_objects("/ajax/bucket/file/list_objects.json", "获取 Object 列表", "GET"),
        set_object_acl("/ajax/bucket/file/set_object_acl.json", "更新对象读写权限", "POST");

        private String url;
        private String desc;
        private String method;

        ObjectUrl(String url, String desc, String method) {
            this.url = url;
            this.desc = desc;
            this.method = method;
        }

        public String getUrl() {
            return url;
        }

        public String getDesc() {
            return desc;
        }

        public String getMethod() {
            return method;
        }
    }

    public enum StatisUrl {
        /**
         * 首页概览统计数据
         */
        home_overview("/ajax/statis/overview.json", "获取首页概览数据", "GET");

        private String url;
        private String desc;
        private String method;

        StatisUrl(String url, String desc, String method) {
            this.url = url;
            this.desc = desc;
            this.method = method;
        }

        public String getUrl() {
            return url;
        }

        public String getDesc() {
            return desc;
        }

        public String getMethod() {
            return method;
        }
    }
}

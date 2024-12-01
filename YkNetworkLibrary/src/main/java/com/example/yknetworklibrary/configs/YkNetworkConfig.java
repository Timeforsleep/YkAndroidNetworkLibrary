package com.example.yknetworklibrary.configs;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class YkNetworkConfig {

    // 配置项
    private long writeTimeout = 15000;
    private long readTimeout = 15000;
    private long connectTimeout = 15000;
    private int maxRequests = 5;
    private int maxRequestsPerHost = 5;

    // 单例实例
    private static final YkNetworkConfig INSTANCE = new YkNetworkConfig();

    // 获取单例
    public static YkNetworkConfig getInstance() {
        return INSTANCE;
    }

    // 获取写超时时间
    public long getWriteTimeout() {
        return writeTimeout;
    }

    // 设置写超时时间
    public void setWriteTimeout(long writeTimeout) {
        this.writeTimeout = writeTimeout;
    }

    // 获取读超时时间
    public long getReadTimeout() {
        return readTimeout;
    }

    // 设置读超时时间
    public void setReadTimeout(long readTimeout) {
        this.readTimeout = readTimeout;
    }

    // 获取连接超时时间
    public long getConnectTimeout() {
        return connectTimeout;
    }

    // 设置连接超时时间
    public void setConnectTimeout(long connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    // 获取最大请求并发数量
    public int getMaxRequests() {
        return maxRequests;
    }

    // 设置最大请求并发数量
    public void setMaxRequests(int maxRequests) {
        this.maxRequests = maxRequests;
    }

    // 获取主机最大并发请求数
    public int getMaxRequestsPerHost() {
        return maxRequestsPerHost;
    }

    // 设置主机最大并发请求数
    public void setMaxRequestsPerHost(int maxRequestsPerHost) {
        this.maxRequestsPerHost = maxRequestsPerHost;
    }
}
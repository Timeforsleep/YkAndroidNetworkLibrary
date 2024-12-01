package com.example.yknetworklibrary;

import com.example.yknetworklibrary.configs.YkNetworkConfig;

public class YkNetworkManager {

    // OkHttpManager 实例
    private OkHttpManager okHttpManager;

    // 构造方法
    private YkNetworkManager() {
    }

    // 单例模式，保证全局只有一个实例
    private static final YkNetworkManager _instance = new YkNetworkManager();

    public static YkNetworkManager getInstance() {
        return _instance;
    }

    public OkHttpManager getOkHttpManager() {
        return okHttpManager;
    }

    // 初始化 OkHttpManager
    public void init() {
        // 获取单例配置
        YkNetworkConfig config = YkNetworkConfig.getInstance();

        this.okHttpManager = new OkHttpManager.Builder()
                .writeTimeout(config.getWriteTimeout())
                .connectTimeout(config.getConnectTimeout())
                .readTimeout(config.getReadTimeout())
                .maxRequests(config.getMaxRequests())
                .maxRequestsPerHost(config.getMaxRequestsPerHost())
                .build();
    }
}